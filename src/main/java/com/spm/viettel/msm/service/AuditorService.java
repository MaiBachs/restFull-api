package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.*;
import com.spm.viettel.msm.dto.response.ResultAuditorResponse;
import com.spm.viettel.msm.exceptions.BadRequestValidationException;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import com.spm.viettel.msm.repository.smartphone.AuditorRepository;
import com.spm.viettel.msm.repository.smartphone.entity.ChannelType;
import com.spm.viettel.msm.repository.smartphone.entity.MapAuditorCheckList;
import com.spm.viettel.msm.repository.smartphone.entity.StaffSmartPhone;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Query;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuditorService extends BaseService {
    private final Logger loggerFactory = LoggerFactory.getLogger(AuditorService.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private StaffService staffService;
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private ShopService shopService;
    @Autowired
    @Qualifier("smartphoneSessionFactory")
    private SessionFactory smartPhoneSessionFactory;

    @Autowired
    private AuditorRepository auditorRepository;

    @Value("${TEMPLATE_PATH}")
    private String templateFolder;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;


    public List<AuditorConfig> executeFileImport(List<AuditorConfig> dataExcel) {
        //<editor-fold defaultstate="collapsed" desc="xử lý file từ người dùng">
        Locale locale = LocaleContextHolder.getLocale();
        ShopDto channelShop;
        List<AuditorConfig> auditorConfigNoComment = new ArrayList<>();
        Set<AuditorConfig> duplicateRows = new HashSet<>();
        List<ShopTreeDTO>  listShopBr = shopService.listShopTree(Constants.VTP_SHOP_ID, 3,
                Constants.VTP_SHOP_ID, null);
        List<ChannelType> channelBean = evaluationService.getChannelTypeAudit();
        for (int index = 0; index < dataExcel.size(); index++) {
            AuditorConfig auditorConfig = dataExcel.get(index);
            String brCode = StringUtils.trim(auditorConfig.getBrCode()); // shop chi nhánh
            String channel = StringUtils.trim(auditorConfig.getChannelType()); // kênh
            String auditor = StringUtils.trim(auditorConfig.getAuditor()); // staff kiểm
            String channelCode = StringUtils.trim(auditorConfig.getShopChannel()); // shop thuộc channel

            if (StringUtils.isEmpty(brCode)) {
                auditorConfig.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BR_CODE_REQUIRED, null, locale));
            } else if (StringUtils.isEmpty(channel)) {
                auditorConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_CHANNEL_TYPE_IS_REQUIRED, null, locale));
            } else if (StringUtils.isEmpty(auditor)) {
                auditorConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_JOB_NAME_IS_REQUIRED, null, locale));
            } else if (StringUtils.isEmpty(channelCode)) {
                auditorConfig.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_CHANNEL_CODE_IS_REQUIRED, null, locale));
            } else if (StringUtils.isEmpty(channel)) {
                auditorConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_CHANNEL_TYPE_IS_REQUIRED, null, locale));
            }else {
                ChannelType channelType = evaluationService.getChannelTypeByName(channelBean,channel);
                if (channelType == null) {
                    auditorConfig.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_CHANNEL_TYPE_INVALID, null, locale));
                } else {
                    auditorConfig.setChannelTypeId(channelType.getChannelTypeId());
                    ShopTreeDTO br = shopService.getShopInListShop(listShopBr,brCode);
                    StaffSmartPhone auditor_staff = staffService.searchByUserNameOfSmartPhone(auditor);
                    if (br != null) {
                        auditorConfig.setBranchId(br.getShopId());
                        if (auditor_staff == null) {
                            auditorConfig.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_STAFF_CODE_IS_REQUIRED, null, locale));
                        }else {
                            auditorConfig.setAuditorId(auditor_staff.getStaffId());
                        }
                        channelShop = shopService.getChannelShopOfBranchSmartPhone(br.getShopId(),channelCode,channelType.getChannelTypeId());
                        if(channelShop == null){
                            auditorConfig.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_CHANNEL_CODE_IS_DO_NOT_EXIST, null, locale));
                        }else {
                            auditorConfig.setShopChannelId(channelShop.getShopId());
                        }
                    } else {
                        auditorConfig.setComment(messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_BR_CODE_INVALID, null, locale));
                    }
                }
                if(duplicateRows.contains(auditorConfig)){
                    auditorConfig.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_DUPLICATE_ROW, null, locale));
                }else if (auditorConfig.getComment() == null || auditorConfig.getComment().equals("") || auditorConfig.getComment().isEmpty()) {
                    auditorConfigNoComment.add(auditorConfig);
                    duplicateRows.add(auditorConfig);
                }
            }
        }
        return auditorConfigNoComment;
        //</editor-fold>
    }

    public List<MapAuditorCheckList> processExcelData(List<AuditorConfig> data, UserTokenDto userProfile, ActionChannelType action) {
        List<MapAuditorCheckList> auditorCheckLists = new ArrayList<>();
        try {
            auditorCheckLists = auditorAction(convertBeantoEntityEvaluation(data,userProfile),action,userProfile);
        } catch (Exception e) {
           loggerFactory.error(e.getMessage(), e);
        }
        return auditorCheckLists;
    }

    public List<MapAuditorCheckList> auditorAction(List<MapAuditorCheckList> auditorCheckList, ActionChannelType action, UserTokenDto userProfile) {
        List<MapAuditorCheckList> dataToDB = new ArrayList<>();
        List<MapAuditorCheckList> dataNew = new ArrayList<>();
        auditorCheckList.stream().forEach(config -> {
            Map<String, Object> model = new HashMap<>();
            model.put("branchId", config.getBranchId());
            model.put("channelTypeId", config.getChannelTypeId());
            model.put("shopId", config.getShopId());
            model.put("auditorId", config.getAuditorId());

            Session smartphoneSession = null;
            MapAuditorCheckList auditorCheckListImport = new MapAuditorCheckList();

            try {
                smartphoneSession = smartPhoneSessionFactory.openSession();
                Query query = getSessionFactory(getClass(), "ImportAuditorConfig.sftl", model, smartphoneSession);
                if (config.getBranchId() != null) {
                    query.setParameter("branchId", config.getBranchId());
                }
                if (config.getChannelTypeId() != null) {
                    query.setParameter("channelTypeId", config.getChannelTypeId());
                }
                if (config.getShopId() != null) {
                    query.setParameter("shopId", config.getShopId());
                }
                if (config.getAuditorId() != null) {
                    query.setParameter("auditorId", config.getAuditorId());
                }

                List<MapAuditorCheckList> resultList = query.getResultList();
                if (!resultList.isEmpty()) {
                    auditorCheckListImport = resultList.get(0);
                    auditorCheckListImport.setUpdatedBy(userProfile.getStaffCode());
                    auditorCheckListImport.setUpdatedDate(new Date());
                    dataToDB.add(auditorCheckListImport);
                } else {
                    dataNew.add(config);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (smartphoneSession != null && smartphoneSession.isOpen()) {
                    smartphoneSession.close();
                }
            }
        });
        switch (action) {
            case ADD:
                try {
                    if(dataToDB.size() > 0){
                        auditorRepository.saveAll(dataToDB);
                    }
                    if(dataNew.size() > 0){
                        auditorRepository.saveAll(dataNew);
                    }
                    return auditorCheckList;
                }catch (Exception e){
                   loggerFactory.error(e.getMessage(), e);
                }
                break;
            case DELETE:
                try {
                    auditorRepository.deleteAll(dataToDB);
                    return dataToDB;
                }catch (Exception e){
                    loggerFactory.error(e.getMessage(), e);
                }
                break;
        }
        return null;
    }


    public List<MapAuditorCheckList> convertBeantoEntityEvaluation(List<AuditorConfig> auditorConfig, UserTokenDto userProfile) {
        return auditorConfig.stream()
                .map(config -> {
                    MapAuditorCheckList resultEntity = new MapAuditorCheckList();
                    resultEntity.setBranchId(config.getBranchId());
                    resultEntity.setBranchCode(config.getBrCode());
                    resultEntity.setChannelTypeId(config.getChannelTypeId());
                    resultEntity.setAuditorId(config.getAuditorId());
                    resultEntity.setShopId(config.getShopChannelId());
                    resultEntity.setCreatedBy(userProfile.getStaffCode());
                    resultEntity.setCreatedDate(new Date());
                    resultEntity.setUpdatedBy(userProfile.getStaffCode());
                    resultEntity.setUpdatedDate(new Date());
                    return resultEntity;
                })
                .collect(Collectors.toList());
    }

    public int getCountAuditorCheckList(Long auditorId, Long branchId,Long channelTypeId,Long channelCodeId){
        int result = auditorRepository.getCountAuditor(auditorId,branchId,channelTypeId,channelCodeId);
        return result;
    }
    public ResultAuditorResponse searchAuditorConfig(SearchAuditorRequest request){
        Session smartphoneSession = null;
        List<AuditorCheckListDTO> mapAuditorCheckLists;
        ResultAuditorResponse auditorResponse = new ResultAuditorResponse();
       try {
           Map<String, Object> model = new HashMap<>();
           model.put("branchId", request.getBranchId());
           model.put("channelTypeId", request.getChannelTypeId());
           model.put("auditorId", request.getAuditorId());
           model.put("shopChannelId", request.getShopChannelId());
           model.put("fromDate", request.getFromDate());
           model.put("toDate", request.getToDate());
           smartphoneSession = smartPhoneSessionFactory.openSession();
           Query query = getSessionFactory(AuditorCheckListDTO.class,"searchAuditorConfig.sftl",model,smartphoneSession);
           if (request.getBranchId() != null) {
               query.setParameter("branchId", request.getBranchId());
           }
           if (request.getChannelTypeId() != null) {
               query.setParameter("channelTypeId", request.getChannelTypeId());
           }
           if (request.getAuditorId() != null) {
               query.setParameter("auditorId", request.getAuditorId());
           }
           if (request.getShopChannelId() != null) {
               query.setParameter("shopChannelId", request.getShopChannelId());
           }
           if (StringUtils.isNotEmpty(request.getFromDate())) {
               query.setParameter("fromDate", request.getFromDate());
           }
           if (StringUtils.isNotEmpty(request.getToDate())) {
               query.setParameter("toDate", request.getToDate());
           }
           mapAuditorCheckLists = query.getResultList();
           if(mapAuditorCheckLists != null){
               auditorResponse.setAuditorCheckLists(mapAuditorCheckLists);
           }
           if (request.isPaging()) {
               int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
               int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() -1 : 0;
               Long countResults = (long) mapAuditorCheckLists.size();
               int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
               if (currentPage < 0) {
                   currentPage = 0;
               } else if (currentPage > lastPageNumber) {
                   currentPage = lastPageNumber;
               }
               auditorResponse.setTotalRecord(countResults);
               auditorResponse.setTotalPage(lastPageNumber);
               auditorResponse.setCurrentPage(currentPage);
               query.setMaxResults(pageSize);
               query.setFirstResult(currentPage * pageSize);
               mapAuditorCheckLists = query.getResultList();
               auditorResponse.setAuditorCheckLists(mapAuditorCheckLists);
           }
       }catch (Exception e){
            loggerFactory.error(e.getMessage());
       }finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return auditorResponse;
    }

    public String auditorSearchResultExport(SearchAuditorRequest request) {
        Session smartphoneSession = null;
        List<ExportAuditorDto> mapAuditorCheckLists = new ArrayList<>();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("branchId", request.getBranchId());
            model.put("channelTypeId", request.getChannelTypeId());
            model.put("auditorId", request.getAuditorId());
            model.put("shopChannelId", request.getShopChannelId());
            model.put("fromDate", request.getFromDate());
            model.put("toDate", request.getToDate());
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(),"exportAuditorConfig.sftl",model,smartphoneSession);
            if (request.getBranchId() != null) {
                query.setParameter("branchId", request.getBranchId());
            }
            if (request.getChannelTypeId() != null) {
                query.setParameter("channelTypeId", request.getChannelTypeId());
            }
            if (request.getAuditorId() != null) {
                query.setParameter("auditorId", request.getAuditorId());
            }
            if (request.getShopChannelId() != null) {
                query.setParameter("shopChannelId", request.getShopChannelId());
            }
            if (StringUtils.isNotEmpty(request.getFromDate())) {
                query.setParameter("fromDate", request.getFromDate());
            }
            if (StringUtils.isNotEmpty(request.getToDate())) {
                query.setParameter("toDate", request.getToDate());
            }
            mapAuditorCheckLists = query.getResultList();
        } catch (Exception e) {
            loggerFactory.error("", e);
        }finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        String templateFolder = this.templateFolder;
        String fileName = "auditor_search_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = fileNameFullFolder + File.separator + fileName; // đừng dãn file và thư mục để down về máy
        String fileTemplate = "template_auditor_search_result.xls";
        File templateFile = new File(templateFolder, fileTemplate); // đường dẫn file mẫu
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("data", mapAuditorCheckLists);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            loggerFactory.error("An error occurred while searching for review results", e);
        }
        return null;
    }

    public List<AuditorCheckListDTO> getAuditorDetails(Long id){
        List<AuditorCheckListDTO> mapAuditorCheckLists = new ArrayList<>();
        Session smartphoneSession = null;
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("id", id);
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(),"getAuditorDetails.sftl",model,smartphoneSession);
            if (id != null) {
                query.setParameter("id", id);
            }
            mapAuditorCheckLists = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return mapAuditorCheckLists;
    }

    @Transactional
    public boolean editAuditor(List<AuditorUpdateRequest> dataAuditRequest,UserTokenDto userProfile){
        try {
            List<MapAuditorCheckList> listEntityAudit = dataAuditRequest.stream()
                    .map(m -> {
                        List<String> missingFields = new ArrayList<>();
                        if (m.getBranchId() == null) {
                            throw new BadRequestValidationException(ResponseStatusEnum.FIELD_MISSING);
                        }
                        if (m.getAuditorId() == null) {
                            throw new BadRequestValidationException(ResponseStatusEnum.FIELD_MISSING);
                        }
                        if (m.getChannelTypeId() == null) {
                            throw new BadRequestValidationException(ResponseStatusEnum.FIELD_MISSING);
                        }
                        if (m.getChannelCodeId() == null) {
                            throw new BadRequestValidationException(ResponseStatusEnum.FIELD_MISSING);
                        }
                        else {
                            int countAuditor =  getCountAuditorCheckList(m.getAuditorId(),m.getBranchId(),m.getChannelTypeId(),m.getChannelCodeId());
                            if(countAuditor > 0){
                                throw new BadRequestValidationException(ResponseStatusEnum.RECORD_ALREADY_EXISTS);
                            }
                        }
                        MapAuditorCheckList auditorCheckList = new MapAuditorCheckList();
                        auditorCheckList.setAuditorId(m.getAuditorId());
                        auditorCheckList.setBranchId(m.getBranchId());
                        auditorCheckList.setChannelTypeId(m.getChannelTypeId());
                        auditorCheckList.setShopId(m.getChannelCodeId());
                        auditorCheckList.setCreatedBy(userProfile.getStaffCode());
                        auditorCheckList.setCreatedDate(new Date());
                        auditorCheckList.setUpdatedBy(userProfile.getStaffCode());
                        auditorCheckList.setUpdatedDate(new Date());
                        return auditorCheckList;
                    })
                    .collect(Collectors.toList());
            auditorRepository.saveAll(listEntityAudit);
            return true;
        }catch (Exception e){
            loggerFactory.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean checkRelationship(Long branchId, Long channelTypeId,Long channelCodeId){
        return true;
    }

}
