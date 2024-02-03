package com.spm.viettel.msm.service;

import com.spm.viettel.msm.controller.EvaluationController;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.*;
import com.spm.viettel.msm.dto.response.ResultEvaluationResp;
import com.spm.viettel.msm.repository.sm.ChannelTypeRepository;
import com.spm.viettel.msm.repository.smartphone.*;
import com.spm.viettel.msm.repository.smartphone.ChannelTypeSmartPhoneRepository;
import com.spm.viettel.msm.repository.smartphone.entity.ChannelType;
import com.spm.viettel.msm.repository.smartphone.entity.Job;
import com.spm.viettel.msm.repository.smartphone.entity.MapChannelTypeCheckList;
import com.spm.viettel.msm.repository.smartphone.entity.Plan;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.DateUtil;
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
import javax.persistence.Query;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.validator.routines.DateValidator;

@Service
public class EvaluationService extends BaseService{
    private final Logger loggerFactory = LoggerFactory.getLogger(EvaluationController.class);
    private final PlanRepository planRepository;
    private final ChannelTypeSmartPhoneRepository channelTypeRepositorySmartPhone;
    private final com.spm.viettel.msm.repository.sm.ChannelTypeRepository channelTypeRepositorySM;
    private final JobRepository jobRepository;
    private final ShopService shopService;
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public EvaluationService(PlanRepository planRepository, ChannelTypeSmartPhoneRepository channelTypeRepository,
                             ChannelTypeRepository channelTypeRepositorySM, JobRepository jobRepository,
                             ShopService shopService, EvaluationRepository evaluationRepository) {
        this.planRepository = planRepository;
        this.channelTypeRepositorySmartPhone = channelTypeRepository;
        this.channelTypeRepositorySM = channelTypeRepositorySM;
        this.jobRepository = jobRepository;
        this.shopService = shopService;
        this.evaluationRepository = evaluationRepository;
    }
    @Autowired
    @Qualifier("smartphoneSessionFactory")
    private SessionFactory smartPhoneSessionFactory;

    @Autowired
    private MessageSource messageSource;


    @Value("${TEMPLATE_PATH}")
    private String templateFolder;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;


    public Boolean checkTimeImportConfig(Set<String> dateConfigs) {
        //<editor-fold defaultstate="collapsed" desc="check thời gian đánh giá phải cùng tháng và năm">
        Date previousDate = null;
        boolean allSameMonth = true;
        for (String dateConfig : dateConfigs) {
            Date currentDate = DateValidator.getInstance().validate(dateConfig, Constants.DD_MM_YYYY);
            if (currentDate == null) {
                continue; // Bỏ qua ngày null
            }

            if (previousDate == null) {
                previousDate = currentDate;
            } else {
                if (currentDate.getMonth() != previousDate.getMonth() ||
                        currentDate.getYear() != previousDate.getYear()) {
                    allSameMonth = false;
                    break;
                }
            }
        }
        return allSameMonth;
        //</editor-fold>
    }

//    public List<EvaluationConfig> executeRemoveFile(List<EvaluationConfig> dataExcel){
//
//        for (int index = 0; index < dataExcel.size(); index++) {
//
//        }
//    }

    public List<EvaluationConfig> executeFileImportDelete(List<EvaluationConfig> dataExcel) {
        Locale locale = LocaleContextHolder.getLocale();
//        Set<EvaluationConfig> duplicateRows = new HashSet<>();
//        List<EvaluationConfig> evaluationConfigNoComment = new ArrayList<>();
        List<ShopTreeDTO>  listShopBr = shopService.listShopTree(Constants.VTP_SHOP_ID, 3,
                Constants.VTP_SHOP_ID, null);
        List<ChannelType> channelBean = getChannelTypeAudit();
        List<Job> jobs = findJobsByAudit();
        for (int index = 0; index < dataExcel.size(); index++) {
            EvaluationConfig evaluationConfig = dataExcel.get(index);
            String brCode = StringUtils.trim(evaluationConfig.getBranchCode());
            String channel = StringUtils.trim(evaluationConfig.getChannelTypeName());
            String jobName = StringUtils.trim(evaluationConfig.getJobName());
            String date_valuation_1 = DateUtil.datemmDDyyyToString(StringUtils.trim(evaluationConfig.getDate_evaluation_1()));
            String date_valuation_2 = DateUtil.datemmDDyyyToString(StringUtils.trim(evaluationConfig.getDate_evaluation_2()));
            String date_valuation_3 = DateUtil.datemmDDyyyToString(StringUtils.trim(evaluationConfig.getDate_evaluation_3()));
            String date_valuation_4 =DateUtil.datemmDDyyyToString(StringUtils.trim(evaluationConfig.getDate_evaluation_4()));
            evaluationConfig.setDate_evaluation_1(date_valuation_1);
            evaluationConfig.setDate_evaluation_2(date_valuation_2);
            evaluationConfig.setDate_evaluation_3(date_valuation_3);
            evaluationConfig.setDate_evaluation_4(date_valuation_4);
            ShopTreeDTO br = shopService.getShopInListShop(listShopBr,brCode);
            if(br == null){
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BRANCH_NOT_FOUND, null, locale));
            }else {
                evaluationConfig.setBranchId(br.getShopId());
                ChannelType channelType = getChannelTypeByName(channelBean,channel);
                if(channelType == null){
                    evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_CHANNEL_TYPE_INVALID, null, locale));
                }else {
                    evaluationConfig.setChannelTypeId(channelType.getChannelTypeId());
                    Job job = isJob(jobs, jobName);
                    if(job == null){
                        evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_JOB_NAME_IS_DOES_NOT_EXIST, null, locale));
                    }else {
                        evaluationConfig.setJobId(job.getJobId());
                    }
                }
            }
        }
        return dataExcel;
    }

    public List<EvaluationConfig> executeFileImport(List<EvaluationConfig> dataExcel) throws ParseException {
        //<editor-fold defaultstate="collapsed" desc="xử lý file từ người dùng">
        Locale locale = LocaleContextHolder.getLocale();
        Set<EvaluationConfig> duplicateRows = new HashSet<>();
        List<EvaluationConfig> evaluationConfigNoComment = new ArrayList<>();
        List<ShopTreeDTO>  listShopBr = shopService.listShopTree(Constants.VTP_SHOP_ID, 3,
                Constants.VTP_SHOP_ID, null);
        List<ChannelType> channelBean = getChannelTypeAudit();
        List<Job> jobs = findJobsByAudit();
        for (int index = 0; index < dataExcel.size(); index++) {
            Set<String> duplicateDate = new HashSet<>();
            EvaluationConfig evaluationConfig = dataExcel.get(index);
            String brCode = StringUtils.trim(evaluationConfig.getBranchCode());
            String channel = StringUtils.trim(evaluationConfig.getChannelTypeName());
            String jobName = StringUtils.trim(evaluationConfig.getJobName());
            Long qtt_month = evaluationConfig.getQuantity_per_month()==null ? 0l : evaluationConfig.getQuantity_per_month();
            Float approval_core = evaluationConfig.getApproval_score();
            String date_valuation_1 = DateUtil.datemmDDyyyToString(StringUtils.trim(evaluationConfig.getDate_evaluation_1()));
            String date_valuation_2 = DateUtil.datemmDDyyyToString(StringUtils.trim(evaluationConfig.getDate_evaluation_2()));
            String date_valuation_3 = DateUtil.datemmDDyyyToString(StringUtils.trim(evaluationConfig.getDate_evaluation_3()));
            String date_valuation_4 =DateUtil.datemmDDyyyToString(StringUtils.trim(evaluationConfig.getDate_evaluation_4()));
            evaluationConfig.setDate_evaluation_1(date_valuation_1);
            evaluationConfig.setDate_evaluation_2(date_valuation_2);
            evaluationConfig.setDate_evaluation_3(date_valuation_3);
            evaluationConfig.setDate_evaluation_4(date_valuation_4);
            if (StringUtils.isEmpty(brCode)) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BR_CODE_REQUIRED, null, locale));
            } else if (StringUtils.isEmpty(channel)) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_CHANNEL_TYPE_IS_REQUIRED, null, locale));
            } else if (StringUtils.isEmpty(jobName)) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_JOB_NAME_IS_REQUIRED, null, locale));
            } else if (qtt_month <= 0) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_QTT_MONTH_FORMAT, null, locale));
            } else if (qtt_month > 4) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_QTT_MONTH_FORMAT, null, locale));
            } else if (StringUtils.isEmpty(Long.toString(qtt_month))) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_QTT_MONTH_IS_REQUIRED, null, locale));
            } else if (StringUtils.isEmpty(Float.toString(approval_core))) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_APPROVAL_CODE_REQUIRED, null, locale));
            } else if (approval_core.equals(0f)) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_APPROVAL_CODE_FORMAT, null, locale));
            } else if (approval_core > 100) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_APPROVAL_CODE_FORMAT, null, locale));
            } else if (approval_core <= 0) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_APPROVAL_CODE_FORMAT, null, locale));
            } else if (StringUtils.isEmpty(date_valuation_1)) {
                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_FORMAT, null, locale));
            }else{
                int countDate = 1;
                duplicateDate.add(date_valuation_1);
                if (StringUtils.isNotEmpty(date_valuation_2)) {
                    countDate = 2;
                    duplicateDate.add(date_valuation_2);
                }else {
                    evaluationConfig.setDate_evaluation_2("");
                }
                if (StringUtils.isNotEmpty(date_valuation_3)) {
                    duplicateDate.add(date_valuation_3);
                    countDate = 3;
                }else {
                    evaluationConfig.setDate_evaluation_3("");
                }
                if (StringUtils.isNotEmpty(date_valuation_4)) {
                    duplicateDate.add(date_valuation_4);
                    countDate = 4;
                }else {
                    evaluationConfig.setDate_evaluation_4("");
                }
                if (countDate != qtt_month) {
                    evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_QTT_PER_MONTH, null, locale));
                }else{
                    if(duplicateDate.size() == countDate){
                        Boolean checkDate = checkTimeImportConfig(duplicateDate); // check thời gian phải cùng tháng và năm
                        if(!checkDate){
                            evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_NOT_SAME_MONTH_EVALUATION_FORMAT, null, locale));
                        }else{
                            if (duplicateDate.size() != countDate) { //  đang có ngày bị trùng
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_QTT_MONTH_IS_REQUIRED, null, locale));
                            } else if (StringUtils.isEmpty(date_valuation_2) && qtt_month >= 2) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_FORMAT, null, locale));
                            } else if (!StringUtils.isEmpty(date_valuation_2) && qtt_month < 2) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_FORMAT, null, locale));
                            } else if (StringUtils.isEmpty(date_valuation_3) && qtt_month >= 3) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_FORMAT, null, locale));
                            } else if (!StringUtils.isEmpty(date_valuation_3) && qtt_month < 3) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_FORMAT, null, locale));
                            } else if (StringUtils.isEmpty(date_valuation_4) && qtt_month == 4) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_FORMAT, null, locale));
                            } else if (!StringUtils.isEmpty(date_valuation_4) && qtt_month < 4) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_FORMAT, null, locale));
                            }else if (!StringUtils.isEmpty(date_valuation_2) && DateUtil.convertStringToDate(date_valuation_2).getTime() < DateUtil.convertStringToDate(date_valuation_1).getTime()){
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_DATE_FORMAT, null, locale));
                            }else if (!StringUtils.isEmpty(date_valuation_3) && DateUtil.convertStringToDate(date_valuation_3).getTime() < DateUtil.convertStringToDate(date_valuation_2).getTime()){
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_DATE_FORMAT, null, locale));
                            }else if (!StringUtils.isEmpty(date_valuation_4) && DateUtil.convertStringToDate(date_valuation_4).getTime() < DateUtil.convertStringToDate(date_valuation_3).getTime()){
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_DATE_FORMAT, null, locale));
                            }else if (duplicateRows.contains(evaluationConfig)) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_DUPLICATE_ROW, null, locale));
                            }else if (!StringUtils.isEmpty(date_valuation_1) && DateUtil.convertStringToDate(date_valuation_1).getTime() < new Date().getTime()) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.Date_IMPORT_CONFIG_DATE_IS_AFTER_NOW, null, locale));
                            } else if (!StringUtils.isEmpty(date_valuation_2) && DateUtil.convertStringToDate(date_valuation_2).getTime() < new Date().getTime()) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.Date_IMPORT_CONFIG_DATE_IS_AFTER_NOW, null, locale));
                            } else if (!StringUtils.isEmpty(date_valuation_3) && DateUtil.convertStringToDate(date_valuation_3).getTime() < new Date().getTime()) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.Date_IMPORT_CONFIG_DATE_IS_AFTER_NOW, null, locale));
                            } else if (!StringUtils.isEmpty(date_valuation_4) && DateUtil.convertStringToDate(date_valuation_4).getTime() < new Date().getTime()) {
                                evaluationConfig.setComment(messageSource.getMessage(MessageKey.Date_IMPORT_CONFIG_DATE_IS_AFTER_NOW, null, locale));
                            }else {
                                ShopTreeDTO br = shopService.getShopInListShop(listShopBr,brCode);
                                if(br == null){
                                    evaluationConfig.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BRANCH_NOT_FOUND, null, locale));
                                }else {
                                    evaluationConfig.setBranchId(br.getShopId());
                                    ChannelType channelType = getChannelTypeByName(channelBean,channel);
                                    if(channelType == null){
                                        evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_CHANNEL_TYPE_INVALID, null, locale));
                                    }else {
                                        evaluationConfig.setChannelTypeId(channelType.getChannelTypeId());
                                        Job job = isJob(jobs, jobName);
                                        if(job == null){
                                            evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_JOB_NAME_IS_DOES_NOT_EXIST, null, locale));
                                        }else {
                                            evaluationConfig.setJobId(job.getJobId());
                                        }
                                    }
                                }
                                if(evaluationConfig.getComment() == null || evaluationConfig.getComment().equals("") || evaluationConfig.getComment().isEmpty()){
                                    EvaluationConfig checkDuplicate = new EvaluationConfig();
                                    checkDuplicate.setBranchCode(evaluationConfig.getBranchCode());
                                    checkDuplicate.setChannelTypeName(evaluationConfig.getChannelTypeName());
                                    checkDuplicate.setJobName(evaluationConfig.getJobName());
                                    checkDuplicate.setQuantity_per_month(evaluationConfig.getQuantity_per_month());
                                    checkDuplicate.setApproval_score(evaluationConfig.getApproval_score());
                                    checkDuplicate.setDate_evaluation_1(date_valuation_1);
                                    checkDuplicate.setDate_evaluation_2(date_valuation_2);
                                    checkDuplicate.setDate_evaluation_3(date_valuation_3);
                                    checkDuplicate.setDate_evaluation_4(date_valuation_4);
                                    duplicateRows.add(checkDuplicate);
                                }
                            }
                        }
                    }else{
                        evaluationConfig.setComment(messageSource.getMessage(MessageKey.EVALUATION_IMPORT_CONFIG_DATE_EVALUATION_QTT_PER_MONTH, null, locale));
                    }
                }
            }
        }
        return dataExcel;
        //</editor-fold>
    }

    public ChannelType getChannelTypeByName(List<ChannelType> allChannelType, String channelName) {
        for (ChannelType channelType : allChannelType) {
                if (channelName.equalsIgnoreCase(channelType.getName())) {
                    return channelType;
                }
            }
        return null;
    }

    public List<ChannelType> getChannelTypeAudit() {
        //<editor-fold defaultstate="collapsed" desc="lấy ra channelType được cấu hình trong bang plan">
        try {
            List<ChannelType> channelTypeByAuditor = channelTypeRepositorySmartPhone.getPlansByCcAudit();
            return channelTypeByAuditor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        //</editor-fold>
    }

    public List<ChannelTypeDTO> getListChannelTypeToPlan() {
        //<editor-fold defaultstate="collapsed" desc="lấy ra channelType được cấu hình trong appParams">
        try {
            List<Plan> Plans = planRepository.getPlansByCcAudit(1l);
            List<Long> channelIds = new ArrayList<>();
            for (Plan plan : Plans) {
                channelIds.add(plan.getChannelTypeId());
            }
            List<ChannelTypeDTO> channelTypes = channelTypeRepositorySmartPhone.findChannelByListId(channelIds);
            return channelTypes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        //</editor-fold>
    }

//    public List<ChannelTypeDTO> getListChannelTypeToAppParams(){
//        //<editor-fold defaultstate="collapsed" desc="lấy ra List channelType trong bảng appParams">
//        try {
//            AppParams appParams = appParamsRepository.getAppParamsByTypeAndCode("CHANNEL_CHECK_LIST", "LIST_CHANNEL_TYP");
//            String[] numberArray = appParams.getValue().split(";");
//            List<ChannelTypeDTO> channelTypes = new ArrayList<>();
//            Long[] channelId = new Long[numberArray.length];
//            for (int i = 0; i < numberArray.length; i++) {
//                channelId[i] = Long.parseLong(numberArray[i]);
//            }
//            for (Long number : channelId) {
//                ChannelTypeDTO channelType = channelTypeRepository.findChannelByIDAndName(number, null);
//                channelTypes.add(channelType);
//            }
//            return channelTypes;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//        //</editor-fold>
//    }

    public List<Job> findByParentIdOrChannel(Long parentId, Long channelTypeId) {
        return findJobsByChannelTypeIdAndParentId(channelTypeId,parentId);
    }

    public List<MapChannelTypeCheckList> processExcelData(List<EvaluationConfig> data, UserTokenDto userProfile, ActionChannelType action, List<MapChannelTypeCheckList> allEvaluationConfig) {
        List<MapChannelTypeCheckList> checkList;
        try {
            checkList = evaluationAction(convertBeantoEntityEvaluation(data,userProfile),action,userProfile,allEvaluationConfig);
            return checkList;
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    public List<Job> findJobsByChannelTypeIdAndParentId(Long channelTypeId, Long parentId){
        Session smartphoneSession = null;
        List<Job> jobs = new ArrayList<>();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("parentId", parentId);
            model.put("channelTypeId", channelTypeId);
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(),"JobResult.sftl",model,smartphoneSession);
            if (channelTypeId != null) {
                query.setParameter("channelTypeId", channelTypeId);
            } else {
                if (parentId != null) {
                    query.setParameter("parentId", parentId);
                }
            }
            jobs = query.getResultList();
        } catch (Exception ex) {
            loggerFactory.error(ex.getMessage());
        } finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return jobs;
    }

    public List<Job> findJobsByAudit(){
        List<Job> jobs = new ArrayList<>();
        try {
            jobs = jobRepository.getJobByCCAudit();
        } catch (Exception ex) {
            loggerFactory.error(ex.getMessage());
        }
        return jobs;
    }

    public Job getJobByCode(String code) {
        // Tìm kiếm job theo mã code
        try {
            Job jobs = jobRepository.getJobByCode(code);
            return jobs;
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    public List<MapChannelTypeCheckList> convertBeantoEntityEvaluation(List<EvaluationConfig> checklistsConfig, UserTokenDto userProfile) throws ParseException {
        List<MapChannelTypeCheckList> checklists = new ArrayList<>();
        for (EvaluationConfig evaluationConfig : checklistsConfig) {
            MapChannelTypeCheckList resultEntity = new MapChannelTypeCheckList();
            resultEntity.setId(evaluationConfig.getId());
            resultEntity.setBranchId(evaluationConfig.getBranchId());
            resultEntity.setBranchCode(StringUtils.toRootUpperCase(evaluationConfig.getBranchCode()));
            resultEntity.setChannelTypeId(evaluationConfig.getChannelTypeId());
            resultEntity.setJobId(evaluationConfig.getJobId());
            resultEntity.setQuantityPerMonth(evaluationConfig.getQuantity_per_month());
            resultEntity.setApprovalScore(evaluationConfig.getApproval_score());
            resultEntity.setDateEvaluation1(DateUtil.convertStringToDate(evaluationConfig.getDate_evaluation_1()));
            if(StringUtils.isNotEmpty(evaluationConfig.getDate_evaluation_2())){
                resultEntity.setDateEvaluation2(DateUtil.convertStringToDate(evaluationConfig.getDate_evaluation_2()));
            }
            if(StringUtils.isNotEmpty(evaluationConfig.getDate_evaluation_3())){
                resultEntity.setDateEvaluation3(DateUtil.convertStringToDate(evaluationConfig.getDate_evaluation_3()));
            }
            if(StringUtils.isNotEmpty(evaluationConfig.getDate_evaluation_4())){
                resultEntity.setDateEvaluation4(DateUtil.convertStringToDate(evaluationConfig.getDate_evaluation_4()));
            }
            resultEntity.setCreatedBy(userProfile.getStaffCode());
            resultEntity.setCreatedDate(new Date());
            resultEntity.setUpdatedBy(userProfile.getStaffCode());
            resultEntity.setUpdatedDate(new Date());
            checklists.add(resultEntity);
        }

        return checklists;
    }

    public List<EvaluationConfig> checkEvaluationConfigInDatabase(List<EvaluationConfig> dataEvaluationConfig,List<MapChannelTypeCheckList> allCheckList){
        List<EvaluationConfig> resultCheckList = new ArrayList<>();
        try {
            String date1 = null;
            String date2 = null;
            String date3 = null;
            String date4 = null;

            for (EvaluationConfig checkListConfig : dataEvaluationConfig) {
                Optional<MapChannelTypeCheckList> foundEvaluation = allCheckList.stream()
                        .filter(evaluation ->
                                evaluation.getChannelTypeId().equals(checkListConfig.getChannelTypeId())
                                        && evaluation.getBranchId().equals(checkListConfig.getBranchId())
                                        && evaluation.getJobId().equals(checkListConfig.getJobId())
                                        && evaluation.getApprovalScore().equals(checkListConfig.getApproval_score())
                                        && evaluation.getQuantityPerMonth().equals(checkListConfig.getQuantity_per_month())
                                        && DateUtil.date2ddMMyyyyString(evaluation.getDateEvaluation1()).equals(checkListConfig.getDate_evaluation_1()) // chuển sang về cùng 1 kiểu
                                        && DateUtil.date2ddMMyyyyString(evaluation.getDateEvaluation2()).equals(checkListConfig.getDate_evaluation_2())
                                        && DateUtil.date2ddMMyyyyString(evaluation.getDateEvaluation3()).equals(checkListConfig.getDate_evaluation_3())
                                        && DateUtil.date2ddMMyyyyString(evaluation.getDateEvaluation4()).equals(checkListConfig.getDate_evaluation_4())
                        )
                        .findFirst();
                if(foundEvaluation.isPresent()){
                    checkListConfig.setId(foundEvaluation.get().getId());
                }else {
                    checkListConfig.setComment("not found in Database");
                }
                resultCheckList.add(checkListConfig);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultCheckList;
    }

    public List<MapChannelTypeCheckList> getAllEvaluationConfig(){
        List<MapChannelTypeCheckList> result = evaluationRepository.findAll();
        return result;
    }


    public List<MapChannelTypeCheckList> evaluationAction(List<MapChannelTypeCheckList> checkLists, ActionChannelType action, UserTokenDto userProfile,List<MapChannelTypeCheckList> allEvaluationCheckList) {
        MapChannelTypeCheckList channelTypeCheckList;
        try {
            if (checkLists.size()>0) {
                if(action == ActionChannelType.DELETE){
                    for (MapChannelTypeCheckList checkList : checkLists) {
                        evaluationRepository.deleteMapChannelTypeCheckListById(checkList.getId());
                    }
                    return checkLists;
                }else if(action == ActionChannelType.EDIT){
                    evaluationRepository.saveAll(checkLists);
                    return checkLists;
                }else {
                    if(allEvaluationCheckList.size() > 0 ){
                        List<MapChannelTypeCheckList> evaluationCheckLists = checkLists;
                        for (MapChannelTypeCheckList checkList : evaluationCheckLists) {
                            MapChannelTypeCheckList finalCheckList = checkList;
                            MapChannelTypeCheckList foundEvaluation = allEvaluationCheckList.stream()
                                    .filter(evaluation ->
                                            evaluation.getChannelTypeId().equals(finalCheckList.getChannelTypeId())
                                                    && evaluation.getBranchId().equals(finalCheckList.getBranchId())
                                                    && evaluation.getJobId().equals(finalCheckList.getJobId())
                                                    && evaluation.getDateEvaluation1().getMonth() == finalCheckList.getDateEvaluation1().getMonth()
                                                    && evaluation.getDateEvaluation1().getYear() == finalCheckList.getDateEvaluation1().getYear()
                                    )
                                    .findFirst().orElse(null);
                            if (foundEvaluation != null) {
                                channelTypeCheckList = foundEvaluation;
                                channelTypeCheckList.setQuantityPerMonth(checkList.getQuantityPerMonth());
                                channelTypeCheckList.setApprovalScore(checkList.getApprovalScore());
                                channelTypeCheckList.setDateEvaluation1(checkList.getDateEvaluation1());
                                channelTypeCheckList.setDateEvaluation2(checkList.getDateEvaluation2());
                                channelTypeCheckList.setDateEvaluation3(checkList.getDateEvaluation3());
                                channelTypeCheckList.setDateEvaluation4(checkList.getDateEvaluation4());
                                channelTypeCheckList.setUpdatedBy(userProfile.getStaffCode());
                                channelTypeCheckList.setUpdatedDate(new Date());
                                checkList = channelTypeCheckList;
                                evaluationRepository.updateEvaluation(checkList.getQuantityPerMonth(), checkList.getApprovalScore(),checkList.getDateEvaluation1(),checkList.getDateEvaluation2(),checkList.getDateEvaluation3(),checkList.getDateEvaluation4(),checkList.getUpdatedBy(),checkList.getUpdatedDate(),checkList.getId());
                            }
                             else {
                                checkList.setUpdatedBy(userProfile.getStaffCode());
                                checkList.setUpdatedDate(new Date());
                                evaluationRepository.save(checkList);
                            }
                        }
                        return checkLists;
                    }else {
                        evaluationRepository.saveAll(checkLists);
                        return checkLists;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void isInsertEvaluationConfig(List<MapChannelTypeCheckList> data){
        for (MapChannelTypeCheckList checkListConfig : data) {
            evaluationRepository.insertMapChannelTypeCheckList(
                    checkListConfig.getBranchId(),
                    checkListConfig.getBranchCode(),
                    checkListConfig.getChannelTypeId(),
                    checkListConfig.getJobId(),
                    checkListConfig.getQuantityPerMonth(),
                    checkListConfig.getApprovalScore(),
                    DateUtil.date2ddMMyyyyString(checkListConfig.getDateEvaluation1()),
                    DateUtil.date2ddMMyyyyString(checkListConfig.getDateEvaluation2()),
                    DateUtil.date2ddMMyyyyString(checkListConfig.getDateEvaluation3()),
                    DateUtil.date2ddMMyyyyString(checkListConfig.getDateEvaluation4()),
                    checkListConfig.getCreatedBy(),
                    DateUtil.date2ddMMyyyyHHMMss(checkListConfig.getCreatedDate()),
                    checkListConfig.getUpdatedBy(),
                    DateUtil.date2ddMMyyyyHHMMss(checkListConfig.getUpdatedDate()));
        }
    }


    public ResultEvaluationResp searchEvaluationConfig(SearchEvaluationRequest request){
        Session smartphoneSession = null;
        List<MapChannelTypeCheckList> mapChannelTypeCheckLists;
        ResultEvaluationResp evaluationResponse = new ResultEvaluationResp();

        try {
            Map<String, Object> model = new HashMap<>();
            model.put("branchId", request.getBranchId());
            model.put("channelTypeId", request.getChannelTypeId());
            model.put("jobId", request.getJobId());
            model.put("fromDate", request.getFromDate());
            model.put("toDate", request.getToDate());
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(),"searchEvaluationConfig.sftl",model,smartphoneSession);
            if (request.getBranchId() != null) {
                query.setParameter("branchId", request.getBranchId());
            }
            if (request.getChannelTypeId() != null) {
                query.setParameter("channelTypeId", request.getChannelTypeId());
            }
            if (request.getJobId() != null) {
                query.setParameter("jobId", request.getJobId());
            }
            if (StringUtils.isNotEmpty(request.getFromDate())) {
                query.setParameter("fromDate", request.getFromDate());
            }
            if (StringUtils.isNotEmpty(request.getToDate())) {
                query.setParameter("toDate", request.getToDate());
            }
            mapChannelTypeCheckLists = query.getResultList();
            if(mapChannelTypeCheckLists != null){
                evaluationResponse.setEvaluations(mapChannelTypeCheckLists);
            }
            if (request.isPaging()) {
                int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
                int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() - 1 : 0;
                Long countResults = (long) mapChannelTypeCheckLists.size();
                int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
                if (currentPage < 0) {
                    currentPage = 0;
                } else if (currentPage > lastPageNumber) {
                    currentPage = lastPageNumber;
                }
                evaluationResponse.setTotalRecord(countResults);
                evaluationResponse.setTotalPage(lastPageNumber);
                evaluationResponse.setCurrentPage(currentPage);
                query.setMaxResults(pageSize);
                query.setFirstResult(currentPage * pageSize);
                mapChannelTypeCheckLists = query.getResultList();
                evaluationResponse.setEvaluations(mapChannelTypeCheckLists);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        return evaluationResponse;
    }

    public MapChannelTypeCheckList getEvaluationDetails(Long evaluationId){
        MapChannelTypeCheckList evaluationDetail = null;
        try {
            evaluationDetail = evaluationRepository.getMapChannelTypeCheckListById(evaluationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return evaluationDetail;
    }

    public String evaluationSearchResultExport(SearchEvaluationRequest request) {

        Session smartphoneSession = null;
        List<EvaluationDetailsDTO> mapChannelTypeCheckLists = new ArrayList<>();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("branchId", request.getBranchId());
            model.put("channelTypeId", request.getChannelTypeId());
            model.put("jobId", request.getJobId());
            model.put("fromDate", request.getFromDate());
            model.put("toDate", request.getToDate());
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(),"exportEvaluationConfig.sftl",model,smartphoneSession);
            if (request.getBranchId() != null) {
                query.setParameter("branchId", request.getBranchId());
            }
            if (request.getChannelTypeId() != null) {
                query.setParameter("channelTypeId", request.getChannelTypeId());
            }
            if (request.getJobId() != null) {
                query.setParameter("jobId", request.getJobId());
            }
            if (StringUtils.isNotEmpty(request.getFromDate())) {
                query.setParameter("fromDate", request.getFromDate());
            }
            if (StringUtils.isNotEmpty(request.getToDate())) {
                query.setParameter("toDate", request.getToDate());
            }
            mapChannelTypeCheckLists = query.getResultList();
        } catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }finally {
            if (smartphoneSession != null && smartphoneSession.isOpen()) {
                smartphoneSession.close();
            }
        }
        String templateFolder = this.templateFolder;
        String fileName = "evaluation_search_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = fileNameFullFolder + File.separator + fileName; // đừng dãn file và thư mục để down về máy
        String fileTemplate = "template_evaluation_search_result.xls";
        File templateFile = new File(templateFolder, fileTemplate); // đường dẫn file mẫu
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("data", mapChannelTypeCheckLists);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            loggerFactory.error("An error occurred while searching for review results", e);
        }
        return null;
    }

    /**
     * Updates the evaluation configuration based on the given request and user token.
     *
     * @param request the map check list request
     * @param userTokenDto the user token DTO
     * @return a map containing the error message, if any
     */
    public List<String> updateEvaluationConfig(MapCheckListRequest request, UserTokenDto userTokenDto) throws ParseException {
        //<editor-fold defaultstate="collapsed" desc="update data evaluation">
        List<MapChannelCheckListDTO> result = request.getChannelTypeCheckListBeans();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        MapChannelTypeCheckList data_new = new MapChannelTypeCheckList();
        List<String> listErrors = new ArrayList<>();
        List<MapChannelTypeCheckList> dataUpdate = new ArrayList<>();
        int countDate = 1;
        UserTokenDto userInformation = userTokenDto;
        for (MapChannelCheckListDTO data_old : result) {
            Boolean checkDate = true;
            Set<String> duplicateDate = new HashSet<>();
            // data_old là FE đẩy lên
            // data_new là được BE xử lý để update vào DB
            if (StringUtils.isNotEmpty(data_old.getDateEvaluation1())) {
                data_new.setDateEvaluation1(dateFormat.parse(data_old.getDateEvaluation1()));
                duplicateDate.add(data_old.getDateEvaluation1());
            }
            if (StringUtils.isNotEmpty(data_old.getDateEvaluation2())) {
                data_new.setDateEvaluation2(dateFormat.parse(data_old.getDateEvaluation2()));
                duplicateDate.add(data_old.getDateEvaluation2());
                countDate = 2;
            }
            if (StringUtils.isNotEmpty(data_old.getDateEvaluation3())) {
                data_new.setDateEvaluation3(dateFormat.parse(data_old.getDateEvaluation3()));
                duplicateDate.add(data_old.getDateEvaluation3());
                countDate = 3;
            }
            if (StringUtils.isNotEmpty(data_old.getDateEvaluation4())) {
                data_new.setDateEvaluation4(dateFormat.parse(data_old.getDateEvaluation4()));
                duplicateDate.add(data_old.getDateEvaluation4());
                countDate = 4;
            }
            if (duplicateDate.size() == countDate && countDate == data_old.getQuantityPerMonth()) {
                if (countDate >= 2 && dateFormat.parse(data_old.getDateEvaluation2()).getTime() < dateFormat.parse(data_old.getDateEvaluation1()).getTime()) {
                    checkDate = false;
                } else if (countDate >= 3 && dateFormat.parse(data_old.getDateEvaluation3()).getTime() < dateFormat.parse(data_old.getDateEvaluation2()).getTime()) {
                    checkDate = false;
                } else if (countDate >= 4 && dateFormat.parse(data_old.getDateEvaluation4()).getTime() < dateFormat.parse(data_old.getDateEvaluation3()).getTime()) {
                    checkDate = false;
                }
                if (checkDate) {
                    data_new.setId(data_old.getId());
                    data_new.setBranchId(data_old.getBranchId());
                    data_new.setBranchCode(data_old.getBranchCode());
                    data_new.setChannelTypeId(data_old.getChannelTypeId());
                    data_new.setQuantityPerMonth(data_old.getQuantityPerMonth());
                    if (data_old.getApprovalScore() != null ? (data_old.getApprovalScore() > 0 && data_old.getApprovalScore() <= 100) : false) {
                        data_new.setApprovalScore(data_old.getApprovalScore());
                        data_new.setJobId(data_old.getJobId());
                        data_new.setCreatedBy(data_old.getCreatedBy());
                        data_new.setCreatedDate(dateFormat.parse(data_old.getCreatedDate()));
                        data_new.setUpdatedBy(userInformation.getStaffCode());
                        data_new.setUpdatedDate(new Date());
                        dataUpdate.add(data_new);
                    } else {
                        listErrors.add(data_old.getJobCode() + ": La puntuación debe ser mayor o igual a 0 y menor o igual a 100");
                    }
                } else {
                    listErrors.add(data_old.getJobCode() + ": El número de visitas en el próximo mes debe ser mayor que el mes anterior.");
                }
            } else {
                listErrors.add(data_old.getJobCode() + ": Las fechas no deben superponerse");
            }
            if(dataUpdate.size() > 0 ){
                evaluationAction(dataUpdate, ActionChannelType.EDIT, userInformation,null);
            }
        }
        return listErrors;
        //</editor-fold>
    }

    public Job isJob(List<Job> allJob, String jobCode) {
        for (Job job : allJob) {
            if (jobCode.equalsIgnoreCase(job.getCode())) {
                return job;
            }
        }
        return null;
    }
}
