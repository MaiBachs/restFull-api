package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.SearchVisitPlanRequest;
import com.spm.viettel.msm.dto.response.SearchVisitPlanResponse;
import com.spm.viettel.msm.repository.sm.ShopRepository;
import com.spm.viettel.msm.repository.sm.StaffRepository;
import com.spm.viettel.msm.repository.sm.VisitPlanMapRepository;
import com.spm.viettel.msm.repository.sm.entity.ReportStaff;
import com.spm.viettel.msm.repository.sm.entity.Shop;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import com.spm.viettel.msm.repository.sm.entity.VisitPlanMap;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.JasperReportExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Connection;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisitPlanService {
    @Autowired
    private VisitPlanMapRepository visitPlanMapRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Value("${TEMPLATE_PATH}")
    private String templateFolder;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    @Autowired
    private MessageSource messageSource;

    @Value("${server_mode}")
    private String server_mode;
    private Locale locale = LocaleContextHolder.getLocale();

    public SearchVisitPlanResponse searchVisitPlan(SearchVisitPlanRequest request) throws ParseException {
        SearchVisitPlanResponse searchVisitPlanResponse = new SearchVisitPlanResponse();
        List<VisitPlanBean> visitPlanBeans = visitPlanMapRepository.searchVisitPlan(request.getBranchId(), request.getBcId(),
                request.getStaffId(), request.getPosCode(), request.getFromDate(), request.getToDate(), request.getIsVisited());
        if (request.isPaging()) {
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
            int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 1;
            Long countResults = (long) visitPlanBeans.size();
            int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
            if (currentPage < 1) {
                currentPage = 1;
            } else if (currentPage > lastPageNumber) {
                currentPage = lastPageNumber;
            }
            searchVisitPlanResponse.setTotalRecord(countResults);
            searchVisitPlanResponse.setTotalPage(lastPageNumber);
            searchVisitPlanResponse.setCurrentPage(currentPage);
            int startIndex = (currentPage - 1) * pageSize;
            if (startIndex < 0) {
                startIndex = 0;
            }
            int endIndex = Math.min(startIndex + pageSize, visitPlanBeans.size());
            searchVisitPlanResponse.setVisitPlanBeans(visitPlanBeans.subList(startIndex, endIndex));
        }
        return searchVisitPlanResponse;
    }

    public List<VisitPlanMap> processExcelData(List<VisitPlanMap> visitPlans, ActionChannelType actionType) {
        List<VisitPlanMap> plansAfterImported = new ArrayList<>();
        if (actionType == ActionChannelType.ADD) {
            plansAfterImported = saveVisitPlan(visitPlans);
        } else if (actionType == ActionChannelType.DELETE) {
            plansAfterImported = deleteVisitPlanByExcel(visitPlans);
        }
        return plansAfterImported;
    }

    public List<VisitPlanMap> saveVisitPlan(List<VisitPlanMap> visitPlans) {
        List<VisitPlanMap> plansOk = new ArrayList<>();
        if (visitPlans != null && !visitPlans.isEmpty()) {
            try {
                for (VisitPlanMap plan : visitPlans) {
                    if (StringUtils.isEmpty(plan.getComment())) {
                        visitPlanMapRepository.save(plan);
                        plansOk.add(plan);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return plansOk;
    }

    public List<VisitPlanMap> deleteVisitPlanByExcel(List<VisitPlanMap> visitPlans) {
        List<VisitPlanMap> plansDeleted = new ArrayList<>();
        if (!visitPlans.isEmpty()) {
            try {
                for (VisitPlanMap vi : visitPlans) {
                    if (StringUtils.isEmpty(vi.getComment())) {
                        List<VisitPlanMap> lst = visitPlanMapRepository.getVisitPlanByPlanSource(vi.getZonalCode(), vi.getPdvCode(), vi.getDatePlanText());
                        if (!lst.isEmpty()) {
                            for (VisitPlanMap viDb : lst) {
                                visitPlanMapRepository.delete(viDb);
                            }
                            plansDeleted.add(vi);
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return plansDeleted;
    }

    public List<VisitPlanMap> readExcelFile(File excel, ActionChannelType actionType) throws Exception {
//        FileInputStream excelFile = new FileInputStream(excel);
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        List<VisitPlanMap> visitPlans = new ArrayList<>();
//        UserProfile profile = getUserProfile();
//        List<ReportStaff> reportStaffs = VisitPlanBiz.getStaffsByUserProfile(sessionSM, profile, null);
//        Set<String> allUsers = reportStaffs.stream().map(s -> s.getZonalCode()).collect(Collectors.toSet());
//        for (ReportStaff rs : reportStaffs) {
//            allUsers.add(rs.getZonalCode());
//        }

        Workbook workbook = WorkbookFactory.create(excel);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            if (currentRow.getRowNum() == 0) {
                continue;
            }

            Iterator<Cell> cellIterator = currentRow.iterator();
            VisitPlanMap plan = new VisitPlanMap();
            plan.setStatus(0);

//            if (zonalBean != null) {
            StaffDto zonalDto = null;
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                //getCellTypeEnum shown as deprecated for version 3.15
                //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                switch (currentCell.getColumnIndex()) {
                    case 0:
                        Double celIndex = currentCell.getNumericCellValue();
                        plan.setIndex(celIndex.intValue());
                        break;
                    case 1:
                        plan.setBranchCode(currentCell.getStringCellValue());
                        break;
                    case 2:
                        String userCode = currentCell.getStringCellValue();
                        String userComment = "";
                        plan.setZonalCode(userCode);
                        if (StringUtils.isEmpty(userCode) || StringUtils.isEmpty(userCode.trim())) {
                            userComment = messageSource.getMessage("visit_plan.zonal.empty", null, locale);
                        } else {
                            userCode = userCode.trim();
                            plan.setZonalCode(userCode);
                            zonalDto = staffRepository.getUserImportInformation(userCode);
                            if (zonalDto == null) {
                                String[] codeError = {userCode};
                                userComment = messageSource.getMessage("user_not_found", codeError, locale);
                            } else {
                                plan.setZonalId(zonalDto.getId());
                            }
                        }
                        plan.setComment(userComment);

                        break;
                    case 3:
                        String pdv = currentCell.getStringCellValue();
                        String pvdComment = "";
                        if (StringUtils.isEmpty(pdv) || StringUtils.isEmpty(pdv.trim())) {
                            pvdComment = messageSource.getMessage("visit_plan.pvd.empty", null, locale);
                        } else {
                            if (zonalDto != null) {
                                if (StringUtils.isNotEmpty(zonalDto.getPosCode()) && staffRepository.CheckStaffMustValidateWhenImport(zonalDto.getPosCode()) > 0) {
                                    List<ReportStaff> pdvStaffs = new ArrayList<>();
                                    List<ReportStaff> pdvOfStaff = staffRepository.ListPVDWithZonal(Arrays.asList(zonalDto.getId()), zonalDto.getPosCode());
                                    pdvStaffs = pdvOfStaff.stream().filter(s -> s.getZonalCode().equalsIgnoreCase(plan.getZonalCode()) && s.getPdvCode().equalsIgnoreCase(pdv)).collect(Collectors.toList());
                                    if (pdvStaffs.isEmpty()) {
                                        pvdComment = messageSource.getMessage("visit_plan.import.error_pdv_not_belong_zonal", null, locale);
                                    } else {
                                        ReportStaff impStaff = pdvStaffs.get(0);
                                        if (StringUtils.isEmpty(impStaff.getBranchCode()) || !impStaff.getBranchCode().equalsIgnoreCase(plan.getBranchCode())) {
                                            pvdComment = messageSource.getMessage("invalid_branch_(a)", null, locale); // Branch invalid
                                        } else {
                                            plan.setPdvId(impStaff.getPvdId());
                                            plan.setPdvChannelObjectType(impStaff.getChannelObjectType());
                                            plan.setBranchId(impStaff.getBranchId());
                                            plan.setBcId(impStaff.getBcId());
                                            plan.setBcCode(impStaff.getBcCode());
                                        }
                                    }
                                } else {
                                    BranchBcDTO brUser = staffRepository.BranchBcOfStaff(zonalDto.getId());
                                    ShopDto shopUserBean = shopRepository.getShop(zonalDto.getShopId());
                                    if (!Constants.SHOP_TYPE_VTP.equalsIgnoreCase(shopUserBean.getShopType()) &&
                                            (StringUtils.isEmpty(brUser.getBranchCode()) || !brUser.getBranchCode().equalsIgnoreCase(plan.getBranchCode()))) {
                                        pvdComment = messageSource.getMessage("invalid_branch_(a)", null, locale); // Branch invalid
                                    }

                                    Staff staff = staffRepository.getByStaffCode(pdv);
                                    if (staff != null) {
//                                        BranchBcBean brChannel = StaffBiz.getBranchBcOfStaff(sessionSM, staff.getStaffId());

//                                        if (Constants.SHOP_TYPE_BC.equalsIgnoreCase(shopUserBean.getShopType()) && brUser.getBcId().longValue() != brChannel.getBcId().longValue()){
//                                            pvdComment = "El centro de negocios del usuario y el centro del canal no son lo mismo"; // BC invalid
//                                        }else {
                                        if (staff.getStatus().equals(0L)) {
                                            pvdComment = messageSource.getMessage("the_channel_is_inactive", null, locale);
                                        } else {
                                            plan.setPdvId(staff.getStaffId());
                                            plan.setPdvChannelObjectType("2");
                                            plan.setBranchId(brUser.getBranchId());
                                            plan.setBcId(brUser.getBcId());
                                            plan.setBcCode(brUser.getBcCode());
                                        }
                                    } else {
                                        Shop shop = shopRepository.getShopByShopCode(pdv);
                                        if (shop == null) {
                                            pvdComment = messageSource.getMessage("channel_not_found", null, locale);
                                        } else {
//                                            BranchBcBean brChannel = ShopBiz.getBranchBcOfShop(sessionSM, shop.getShopId());
//                                            if (Constants.SHOP_TYPE_BC.equalsIgnoreCase(shopUserBean.getShopType()) && brUser.getBcId().longValue() != brChannel.getBcId().longValue()){
//                                                pvdComment = "El centro de negocios del usuario y el centro del canal no son lo mismo"; // BC invalid
//                                            } else {
                                            if (shop.getStatus().equals(0L)) {
                                                pvdComment = messageSource.getMessage("the_channel_is_inactive", null, locale);
                                            } else {
                                                plan.setPdvId(shop.getShopId());
                                                plan.setPdvChannelObjectType("1");
                                                plan.setBranchId(brUser.getBranchId());
                                                plan.setBcId(brUser.getBcId());
                                                plan.setBcCode(brUser.getBcCode());
                                            }
                                        }
                                    }

                                }
                            }
                            plan.setPdvCode(pdv);
                        }
                        if (StringUtils.isEmpty(plan.getComment()) && !StringUtils.isEmpty(pvdComment)) {
                            plan.setComment(pvdComment);
                        } else if (!StringUtils.isEmpty(plan.getComment()) && !StringUtils.isEmpty(pvdComment)) {
                            plan.setComment(plan.getComment() + " - " + pvdComment);
                        }
                        break;
                    case 4:
                        CellType type = currentCell.getCellType();
                        String cellDate;
                        Date planDate = null;
                        try {
                            switch (type) {
                                case STRING:
                                    cellDate = currentCell.toString();
                                    try {
                                        if (cellDate != null) {
                                            cellDate = cellDate.trim();
                                        }
                                        planDate = DateValidator.getInstance().validate(cellDate, "dd/MM/yyyy");
                                        plan.setDatePlanText(cellDate);
                                    } catch (Exception e) {
                                        try {
                                            planDate = DateValidator.getInstance().validate(cellDate, "dd-MM-yyyy");
                                        } catch (Exception e1) {
                                            planDate = null;
                                        }
                                    }
                                    break;
                                case NUMERIC:
                                    planDate = currentCell.getDateCellValue();
                                    plan.setDatePlanText(DateFormatUtils.format(planDate, "dd/MM/yyyy"));
                                    break;
                            }
                        } catch (Exception e) {
                            String dateComment = messageSource.getMessage("visit_plan.date.invalid", null, locale);
                            if (StringUtils.isEmpty(plan.getComment())) {
                                plan.setComment(dateComment);
                            } else {
                                plan.setComment(plan.getComment() + " - " + dateComment);
                            }
                        }
                        if (planDate != null) {
                            try {
                                Date current = new Date();
                                int compare = DateUtils.truncatedCompareTo(planDate, current, Calendar.DAY_OF_MONTH);
                                if (compare < 0) {
                                    String[] importedArgs = {DateFormatUtils.format(planDate, Constants.DATE_FORMAT_SHORT)};
                                    String dateComment = messageSource.getMessage("visit_plan.import.error_date_before_current", importedArgs, locale);

                                    if (StringUtils.isEmpty(plan.getComment())) {
                                        plan.setComment(dateComment);
                                    } else {
                                        plan.setComment(plan.getComment() + " - " + dateComment);
                                    }
                                }
                                plan.setDatePlan(planDate);
                            } catch (Exception e) {
                                String dateComment = messageSource.getMessage("visit_plan.date.invalid", null, locale);
                                if (StringUtils.isEmpty(plan.getComment())) {
                                    plan.setComment(dateComment);
                                } else {
                                    plan.setComment(plan.getComment() + " - " + dateComment);
                                }
                            }
                        } else {
                            String dateComment = messageSource.getMessage("visit_plan.date.invalid", null, locale);
                            if (StringUtils.isEmpty(plan.getComment())) {
                                plan.setComment(dateComment);
                            } else {
                                plan.setComment(plan.getComment() + " - " + dateComment);
                            }
                        }
                        break;
                }

            }
            // check duplicate
            //<editor-fold defaultstate="collapsed" desc="add plan">
            if (!StringUtils.isEmpty(plan.getZonalCode()) && !StringUtils.isEmpty(plan.getPdvCode())) {
                List<VisitPlanMap> lst = visitPlanMapRepository.getVisitPlanByPlanSource(plan.getZonalCode(),
                        plan.getPdvCode(), plan.getDatePlanText());
                if (actionType == ActionChannelType.ADD && lst != null && !lst.isEmpty()) {
                    plan.setComment(messageSource.getMessage("visit_plan.import.error_plan_existed", null, locale));
                } else if (actionType == ActionChannelType.DELETE && (lst == null || lst.isEmpty())) {
                    plan.setComment(messageSource.getMessage("visit_plan.import.error_plan_not_found", null, locale));
                } else {
                    for (VisitPlanMap v : visitPlans) {
                        if (v.equals(plan)) {
                            String[] importedArgs = {plan.getPdvCode()};
                            plan.setComment(messageSource.getMessage("visit_plan.import.error_pdv_date_plan_is_duplicate", importedArgs, locale));
                            break;
                        }
                    }
                }
                visitPlans.add(plan);
            }
//</editor-fold>
        }
        return visitPlans;
    }

    public String buildImportResultFile(List<VisitPlanMap> visitPlan) {
        String templateFolder = this.templateFolder;
        String fileName = "visit_plan_imported_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
        File templateFile = new File(templateFolder, "visit_plan_template_result.xls");
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", visitPlan);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String buildSearchVisitPlanResultFile(SearchVisitPlanRequest request) {
        List<VisitPlanBean> visitPlanBeans = visitPlanMapRepository.searchVisitPlan(request.getBranchId(), request.getBcId(),
                request.getStaffId(), request.getPosCode(), request.getFromDate(), request.getToDate(), request.getIsVisited());
        String templateFolder = this.templateFolder;
        String fileName = "visit_plan_search_result.xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "visit_plan_search_result.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            for (VisitPlanBean visitPlanBean : visitPlanBeans) {
                if (visitPlanBean.getCheckedIn() != null) {
                    visitPlanBean.setVisited("Sí");
                    visitPlanBean.setCheckedInString(DateFormatUtils.format(visitPlanBean.getCheckedIn(), "dd/MM/yyyy HH:mm:ss"));
                } else {
                    visitPlanBean.setVisited("No");
                }
                if (visitPlanBean.getCreatedDate() != null) {
                    visitPlanBean.setCreatedDateString(DateFormatUtils.format(visitPlanBean.getCreatedDate(), "dd/MM/yyyy HH:mm:ss"));
                }
                if (visitPlanBean.getPlanDate() != null) {
                    visitPlanBean.setPlanDateString(DateFormatUtils.format(visitPlanBean.getPlanDate(), "dd/MM/yyyy"));
                }
            }
            Context context = new Context();
            context.putVar("staffs", visitPlanBeans);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> getVisitPlanByUser(Long branchId, Long bcId, Long staffId, String posCode, Date fromDate, Date toDate, Long isVisited) {
        Map<String, Object> result = new HashMap<>();
        List<VisitPlanBean> visitPlanBeans = null;
        visitPlanBeans = visitPlanMapRepository.getVisitPlanByUser(branchId, bcId, staffId, posCode, fromDate, toDate, isVisited);
        result.put("listResult", visitPlanBeans);
        return result;
    }

    public boolean isExcelFile(String fileName) {
        String extension = getFileExtension(fileName);
        return extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx");
    }

    public String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    //chuyển đổi MultipartFile thành File
    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertedFile;
    }

    public VisitPlanMap findById(Long id) {
        return visitPlanMapRepository.findById(id).orElse(null);
    }

    public List<VisitPlanBean> getDataExportVisitPlanByMonth(SearchVisitPlanRequest request) {
        List<VisitPlanBean> list = visitPlanMapRepository.SearchVisitChannelSummary(request.getExportMonth(),
                request.getBranchId(), request.getBcId(), request.getShopId());
        return list;
    }

    public String buildFileExportVisitPlanByMonth(List<VisitPlanBean> visitPlanBeans, String exportMonth) {
        Map<String, Object> paramsReport = new HashMap<>();
        paramsReport.put("exportMonth", exportMonth);
        paramsReport.put("p_sql", visitPlanBeans);
        String templateFolder = this.templateFolder;
        String fileName = "visit_channel_detail_report_" + Calendar.getInstance().getTimeInMillis() + ".xlsx";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;

        File templateFile = new File(templateFolder, "visit_plan_visited_count.jrxml");
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(templateFile.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramsReport);
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setDetectCellType(true); // Detect cell types (date and etc.)
            configuration.setWhitePageBackground(false); // No white background!
            configuration.setFontSizeFixEnabled(false);

            // No spaces between rows and columns
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setRemoveEmptySpaceBetweenColumns(true);

            JasperReportExporter instance = new JasperReportExporter();
            byte[] fileInBytes = instance.exportToXlsx(jasperPrint, "Visit channel summary");
            FileUtils.writeByteArrayToFile(new File(fileNameFull), fileInBytes);

            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<?> deleteVisitPlan(SearchVisitPlanRequest request) {
        Map<String, Object> response = new HashMap<>();
        String message;
        if (request.getVisitPlanId() != null && !request.getVisitPlanId().isEmpty()) {
            for (Long id : request.getVisitPlanId()) {
                VisitPlanMap plan = this.findById(id);
                Date yesterday = DateUtils.addDays(new Date(), -1);
                if (yesterday.before(plan.getDatePlan())) {
                    visitPlanMapRepository.delete(plan);
                    String[] messParam = {DateFormatUtils.format(plan.getDatePlan(), Constants.DATE_FORMAT_SHORT), plan.getPdvCode()};
                    message = messageSource.getMessage("visit_plan.delete.success", messParam, locale);
                } else {
                     message = messageSource.getMessage("visit_plan.delete.error_date_plan_before_current", null, locale);
                }
                response.put("message", message);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Map<String, Object> getLast10VisitPlan(Long channelId, String channelCode, Integer objectType) {
        String serverMode = this.server_mode;
        Map<String, Object> result = new HashMap<>();
//        System.out.println("----------getLast10VisitPlan:" + query.getQueryString());
        List<VisitPlanBean> visitPlanBeans = visitPlanMapRepository.searchLast10VisitPlan(serverMode, channelId, channelCode, objectType);
        result.put("listResult", visitPlanBeans);
//        System.out.println("----------getLast10VisitPlan:listResult:" + result.get("listResult"));
//        SQLQuery queryGetIds = SqlUtils.getSQLQueryFromTemplate(session, params, "GetListIdsOfVisitPlan.sftl");
//        List<BigDecimal> listIdsResult = queryGetIds.list();
//        if (CollectionUtils.isNotEmpty(listIdsResult)) {
//            List<Long> ids1 = listIdsResult.stream().map(id -> id.longValue()).collect(Collectors.toList());
//
//            String sqlGetListIdHaveFile = "SELECT pr.VISIT_PLAN_ID  FROM SMARTPHONE.PLAN_RESULT pr WHERE pr.VISIT_PLAN_ID IN (:visitPlanIds) AND pr.FILE_PATH IS NOT NULL GROUP BY pr.VISIT_PLAN_ID ";
//            SQLQuery queryGetListIdHaveFile = sessionSmartPhone.createSQLQuery(sqlGetListIdHaveFile);
//            queryGetListIdHaveFile.setParameterList("visitPlanIds", ids1);
//            List<BigDecimal> listIds2Result = queryGetListIdHaveFile.list();
//            if (CollectionUtils.isNotEmpty(listIds2Result)) {
//                List<Long> ids2 = listIds2Result.stream().map(id -> id.longValue()).collect(Collectors.toList());
//
//                String sqlGet10 = SqlUtils.getSqlFromTemplate(new HashMap<>(), "SearchLast10VisitPlan.sftl");
//
//                SQLQuery query = session.createSQLQuery(sqlGet10);
//                query.setParameterList("visitPlanIds", ids2);
//                query.setResultTransformer(Transformers.aliasToBean(VisitPlanBean.class));
//                query.addScalar("id", LongType.INSTANCE);
//                query.addScalar("userCode", StringType.INSTANCE);
//                query.addScalar("userId", LongType.INSTANCE);
//                query.addScalar("branchCode", StringType.INSTANCE);
//                query.addScalar("bcCode", StringType.INSTANCE);
//                query.addScalar("planDate", DateType.INSTANCE);
//                query.addScalar("checkedIn", TimestampType.INSTANCE);
//                result.put("listResult", query.list());
//            }
//        }
        return result;
    }

    public VisitPlanMap save(VisitPlanMap visitPlanMap){
        return visitPlanMapRepository.save(visitPlanMap);
    }
}
