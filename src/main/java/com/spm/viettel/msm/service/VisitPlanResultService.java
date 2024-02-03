package com.spm.viettel.msm.service;

import com.spm.viettel.msm.controller.StockController;
import com.spm.viettel.msm.dto.FTPProperties;
import com.spm.viettel.msm.dto.FileDto;
import com.spm.viettel.msm.dto.VisitPlanCategoryResultDTO;
import com.spm.viettel.msm.dto.VisitPlanResultDTO;
import com.spm.viettel.msm.dto.request.VisitPlanResultRequest;
import com.spm.viettel.msm.exceptions.NotFoundException;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.SmsRepository;
import com.spm.viettel.msm.repository.sm.entity.Sms;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import com.spm.viettel.msm.repository.smartphone.PlanResultRepository;
import com.spm.viettel.msm.repository.smartphone.entity.PlanResult;
import com.spm.viettel.msm.utils.Config;
import com.spm.viettel.msm.utils.FTPFileWriter;
import com.spm.viettel.msm.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = { SQLException.class })
public class VisitPlanResultService extends BaseService{
    private final Logger loggerFactory = LoggerFactory.getLogger(StockController.class);
    @Autowired
    private StaffService staffService;
    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private PlanResultRepository planResultRepository;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    @Qualifier("smartphoneSessionFactory")
    private SessionFactory smartPhoneSessionFactory;

    public boolean saveSmsWhenReject(String staffCode, String content){
        Sms sms = new Sms();
        Staff staff = staffService.getByStaffCode(staffCode);
        if(staff != null){
            sms.setIsDn(staff.getTel());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 5);
            sms.setSendTime(cal.getTime());
            sms.setApp("MSM");
            sms.setNumProcess(1);
            sms.setMaxProcess(3);
            sms.setStatus(3l);
            sms.setCreateUser(staffCode);
            sms.setCreateTime(Calendar.getInstance().getTime());
            sms.setContent(content);
            sms.setTypeSMS("0");
            sms.setSMSCode("9001");
            sms.setSendBy("BITEL");
            sms.setSendSMSCode("BITEL INFO");
            sms.setSendTypeSMS("NORMAL");
            try{
                smsRepository.save(sms);
                return true;
            }catch (Exception e){
                loggerFactory.error(e.getMessage());
            }
        }
        return false;
    }

    public List<VisitPlanCategoryResultDTO> getListCateResultSummary(VisitPlanResultRequest request) {
        // get menu item
//        System.out.println("===Step load item start:" + new Date());
//        System.out.println("----------Step 3 load item start:" + new Date());
        List<VisitPlanCategoryResultDTO> categoryLevel2 = getListCateResult(request.getPlanId(), Arrays.asList(request.getJobId()));
//        System.out.println("----------Step 3 load item end:" + new Date());
        categoryLevel2.forEach(cl2Item -> {
            // get checklist item
//            System.out.println("----------Step 4 load item result start:" + new Date());
            List<VisitPlanCategoryResultDTO> listCateL3 = getVisitPlanChecklistResult(request.getPlanId(), request.getVisitPlanId(), Arrays.asList(cl2Item.getJobId()));
//            System.out.println("----------Step 4 load item result end:" + new Date());
            Map<String, List<VisitPlanCategoryResultDTO>> listResultGroupByCode = listCateL3.stream().collect(Collectors.groupingBy(cl3Item -> cl3Item.getName()));
            List<VisitPlanCategoryResultDTO> listUniqueCateL3WithImages = new ArrayList<>();
            listResultGroupByCode.forEach((key, value)-> {
                VisitPlanCategoryResultDTO cateL3WithImages = new VisitPlanCategoryResultDTO();
                BeanUtils.copyProperties(value.get(value.size() - 1), cateL3WithImages);
                cateL3WithImages.setFilePath(null);
                List<String> listFilePath = value.stream().filter(i -> i.getRequireFile() == 1).map(VisitPlanCategoryResultDTO::getFilePath).collect(Collectors.toList());
                cateL3WithImages.setListFilePath(listFilePath);
                listUniqueCateL3WithImages.add(cateL3WithImages);
            });
            cl2Item.setSubItems(listUniqueCateL3WithImages);
        });
//        System.out.println("===Step load item end:" + new Date());
        return categoryLevel2;
    }

    public List<VisitPlanCategoryResultDTO> getListCateResult(Long planId, List<Long> parentIds) {
        Session smartphoneSession = null;
        List<VisitPlanCategoryResultDTO> visitPlanCategoryResultDTOS = new ArrayList<>();
        try{
            Map<String, Object> model = new HashMap<>();
            model.put("planId", planId);
            model.put("parentId", parentIds);
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(), "getVisitPlanCategoryResult.sftl", model, smartphoneSession);
            query.setParameter("planId", planId);
            if (parentIds != null) {
                for (int i = 0; i < parentIds.size(); i++) {
                    query.setParameter("parentId", parentIds.get(i));
                }
            }
            visitPlanCategoryResultDTOS = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return visitPlanCategoryResultDTOS;
    }

    public List<VisitPlanCategoryResultDTO> getVisitPlanChecklistResult(Long planId, Long visitPlanId, List<Long> planJobIds) {
        Session smartphoneSession = null;
        List<VisitPlanCategoryResultDTO> visitPlanCategoryResultDTOS = new ArrayList<>();
        try{
            Map<String, Object> model = new HashMap<>();
            model.put("planId", planId);
            model.put("visitPlanId", visitPlanId);
            model.put("planJobId", planJobIds);
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(), "getVisitPlanChecklistResult.sftl", model, smartphoneSession);
            query.setParameter("planId", planId);
            query.setParameter("visitPlanId", visitPlanId);
            if (planJobIds != null) {
                for (int i = 0; i < planJobIds.size(); i++) {
                    query.setParameter("planJobId", planJobIds.get(i));
                }
            }
            visitPlanCategoryResultDTOS = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return visitPlanCategoryResultDTOS;
    }

    public Long getMobilePlanId(Long visitPlanId) {
        Session smartphoneSession = null;
        Long mobilePlanId = null;
        try{
            Map<String, Object> model = new HashMap<>();
            model.put("visitPlanId", visitPlanId);
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(), "getMobilePlanId.sftl", model, smartphoneSession);
            query.setParameter("visitPlanId", visitPlanId);
            List<Long> mobilePlanIds = query.getResultList();
            if(!mobilePlanIds.isEmpty()){
                mobilePlanId = mobilePlanIds.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return mobilePlanId;
    }

    public List<VisitPlanCategoryResultDTO> getListCate(VisitPlanResultRequest request) {
//        System.out.println("----------Step 1 load Plan by visit plan start:" + new Date());
        Long mobilePlanId = this.getMobilePlanId(request.getVisitPlanId());
//        System.out.println("----------Step 1 load Plan by visit plan end:" + new Date());
        if (mobilePlanId != null) {
//            System.out.println("----------Step 2 load Plan start:" + new Date());
            List<VisitPlanCategoryResultDTO> lst = getListCateResult(mobilePlanId, null);
            lst.forEach(c -> {
                VisitPlanResultRequest requestResult = new VisitPlanResultRequest();
                requestResult.setVisitPlanId(request.getVisitPlanId());
                requestResult.setPlanId(mobilePlanId);
                requestResult.setJobId(c.getJobId());
                List<VisitPlanCategoryResultDTO> categoryLevel2 = getListCateResultSummary(requestResult);
                c.setSubItems(categoryLevel2);
            });
//            System.out.println("----------Step 2 load Plan end:" + new Date());
            return lst;
        }
        return new ArrayList<>();
    }

    public FileDto downloadCheckListResultFile(String filePath) {
        FileDto fileDto = new FileDto();
        FTPFileWriter ftpFileWriter = new FTPFileWriter();
        String ftpIsSsl = Config.getPropValues("FTP_IS_SSL");
        String ftpHost = Config.getPropValues("FTP_HOST");
        String ftpPort = Config.getPropValues("FTP_PORT");
        if (StringUtils.isEmpty(ftpPort)) {
            ftpPort = "21";
        }
        String ftpUser = Config.getPropValues("FTP_USER");
        String ftpPass = Config.getPropValues("FTP_PASS");
        String tmpdir = Config.getPropValues("FILE_TEMP_UPLOAD_PATH");
        try {
            String pdfData = "data:application/octet-stream;base64,";
            String pngData = "data:image/png;base64,";
            String jpegData = "data:image/png;base64,";
            if (StringUtils.isNotEmpty(filePath)) {
                FTPProperties properties = FTPProperties.builder().server(ftpHost)
                        .port(Integer.parseInt(ftpPort)).username(ftpUser).password(ftpPass)
                        .build();
                ftpFileWriter.open(properties);
                OutputStream outputstream = null;
                String[] fileNames = filePath.split("/");
                String fileName = fileNames[fileNames.length - 1];
                fileDto.setFileName(fileName);
                String finalFilePath = null;
                try {
                    File folder = new File(tmpdir);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    String ftpDir = filePath.replace("/" + fileName, "");
                    System.out.println("=====FTP File:" + fileName);
                    System.out.println("=====FTP Dir:" + ftpDir);
                    finalFilePath = tmpdir + File.separator + fileName;
                    outputstream = new FileOutputStream(finalFilePath);
                    ftpFileWriter.loadFile(filePath, outputstream);
                    outputstream.flush();
                } catch (Exception e) {
                    System.out.println("=====FTP File:" + filePath + " ===> Not found");
                    e.printStackTrace();
                    throw new NotFoundException();
                } finally {
                    try {
                        if (outputstream != null) {
                            outputstream.close();
                        }
                        if (StringUtils.isNotEmpty(finalFilePath)) {
                            StringBuilder data = new StringBuilder();
                            if (fileName.toLowerCase().endsWith("pdf")) {
                                data.append(pdfData);
                            } else if (fileName.toLowerCase().endsWith("png")) {
                                data.append(pngData);
                            } else {
                                data.append(jpegData);
                            }
                            String base64 = FileUtils.base64encoder(finalFilePath);
                            if (StringUtils.isNotEmpty(base64)) {
                                data.append(base64);
                                fileDto.setContent(data.toString());
                            }
                            org.apache.commons.io.FileUtils.forceDelete(new File(finalFilePath));
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        throw new NotFoundException();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException();
        }
        return fileDto;
    }

    public boolean checkVisitPlanResultIsOk(Long visitPlanId){
        boolean isOk = true;
        Integer count = planResultRepository.VisitPlanChecklistResult_CheckResultIsOk(visitPlanId);
        if (count > 0){
            isOk = false;
        }
        return isOk;
    }
}
