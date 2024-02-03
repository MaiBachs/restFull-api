package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.DetailEvaluationManageDTO;
import com.spm.viettel.msm.dto.EvaluationManageDTO;
import com.spm.viettel.msm.dto.PlanResultForEvaluationDTO;
import com.spm.viettel.msm.dto.ReasonOfVisitPlanDTO;
import com.spm.viettel.msm.dto.request.EvaluationManageRequest;
import com.spm.viettel.msm.dto.response.DetailEvaluationManageResponse;
import com.spm.viettel.msm.dto.response.ReasonOfVisitPlanMapResponse;
import com.spm.viettel.msm.dto.response.ReasonOfVisitPlanMapViewResponse;
import com.spm.viettel.msm.dto.response.SearchEvaluationManageRespone;
import com.spm.viettel.msm.repository.sm.SmsRepository;
import com.spm.viettel.msm.repository.sm.VisitPlanMapRepository;
import com.spm.viettel.msm.repository.sm.entity.Sms;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import com.spm.viettel.msm.repository.sm.entity.VisitPlanMap;
import com.spm.viettel.msm.repository.smartphone.AppParamsRepository;
import com.spm.viettel.msm.repository.smartphone.JobRepository;
import com.spm.viettel.msm.repository.smartphone.PlanResultRepository;
import com.spm.viettel.msm.repository.smartphone.ReasonRepository;
import com.spm.viettel.msm.repository.smartphone.entity.AppParamsSmartPhone;
import com.spm.viettel.msm.repository.smartphone.entity.Job;
import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.DateUtil;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EvaluationManageService extends BaseService{
    private final Logger loggerFactory = LoggerFactory.getLogger(EvaluationManageService.class);
    public static final Long ActionPlan = 1L;
    public static final Integer In_process = 11;
    public static final Integer Pending = 13;
    public static final Integer Request_update = 14;
    @Autowired
    private VisitPlanMapRepository visitPlanMapRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlanResultRepository planResultRepository;

    @Autowired
    private ReasonRepository reasonRepository;

    @Autowired
    private AppParamsRepository appParamsRepository;

    @Autowired
    private StaffService staffService;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private MessageSource messageSource;

    protected Locale locale = Locale.US;

    public SearchEvaluationManageRespone searchEvaluationManage(EvaluationManageRequest request) {
        SearchEvaluationManageRespone searchEvaluationManageRespone = new SearchEvaluationManageRespone();
        List<EvaluationManageDTO> evaluationManageDTOS = visitPlanMapRepository.searchEvaluationManage(request.getBranchId(),
                request.getTypeChannel(), request.getChannelCode().trim(), request.getAuditor().trim(), request.getEvaluation(),
                request.getStatusEvaluation(), request.getToDate(), request.getFromDate());
        for (EvaluationManageDTO dto : evaluationManageDTOS) {
            dto.convertPeriod();
        }
        if (request.isPaging()) {
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
            int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 1;
            Long countResults = (long) evaluationManageDTOS.size();
            int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
            if (currentPage < 1) {
                currentPage = 1;
            } else if (currentPage > lastPageNumber) {
                currentPage = lastPageNumber;
            }
            searchEvaluationManageRespone.setTotalRecord(countResults);
            searchEvaluationManageRespone.setTotalPage(lastPageNumber);
            searchEvaluationManageRespone.setCurrentPage(currentPage);
            int startIndex = (currentPage - 1) * pageSize;
            if (startIndex < 0) {
                startIndex = 0;
            }
            int endIndex = Math.min(startIndex + pageSize, evaluationManageDTOS.size());
            searchEvaluationManageRespone.setEvaluationManageDTOS(evaluationManageDTOS.subList(startIndex, endIndex));
        }
        return searchEvaluationManageRespone;
    }

    public DetailEvaluationManageResponse detailEvaluation(EvaluationManageRequest request) {
        DetailEvaluationManageResponse detailEvaluationManageResponse = new DetailEvaluationManageResponse();
        List<ReasonOfVisitPlanMapViewResponse> reasonOfVisitPlanMapViewResponses = new ArrayList<>();
        Job evaluationCheck = jobRepository.findById(request.getJobId()).orElse(null);
        if (evaluationCheck != null) {
            List<DetailEvaluationManageDTO> detailEvaluations = new ArrayList<>();
            if (evaluationCheck.getCode().equals("CALIDAD")) {
                List<EvaluationManageDTO> staffOfChannels = visitPlanMapRepository.getVisitPlanById(request.getVisitPlanId());
                if (!staffOfChannels.isEmpty()) {
                    ReasonOfVisitPlanMapViewResponse reasonOfVisitPlanMapViewResponse = new ReasonOfVisitPlanMapViewResponse();
                    for (EvaluationManageDTO staffOfChannel : staffOfChannels) {
                        List<ReasonOfVisitPlanMapResponse> reasonOfVisitPlanMapResponseList = new ArrayList<>();
                        DetailEvaluationManageDTO detailEvaluationManageDTO = new DetailEvaluationManageDTO();
                        if (staffOfChannel.getCheckListResultComment() != null) {
                            detailEvaluationManageDTO.setReasonOfStaffNotEvaluated(staffOfChannel.getCheckListResultComment());
                        } else {
                            reasonOfVisitPlanMapResponseList = this.getReasonOfVisitPlanMap(staffOfChannel.getEvaluationId(), staffOfChannel.getId());
                        }

                        detailEvaluationManageDTO.setStaffOfChannel(staffOfChannel.getChannelCode());
                        detailEvaluationManageDTO.setReasonOfVisitPlanMapResponses(reasonOfVisitPlanMapResponseList);
                        detailEvaluations.add(detailEvaluationManageDTO);
                    }
                    reasonOfVisitPlanMapViewResponse.setDetailEvaluations(detailEvaluations);
                    reasonOfVisitPlanMapViewResponses.add(reasonOfVisitPlanMapViewResponse);
                }
            } else {
                List<EvaluationManageDTO> actionPlanList = visitPlanMapRepository.getVisitPlanByIdAndCheckActionPlan(request.getVisitPlanId(), ActionPlan);
                ReasonOfVisitPlanMapViewResponse reasonOfVisitPlanMapViewResponse = new ReasonOfVisitPlanMapViewResponse();
                DetailEvaluationManageDTO detailEvaluationManageDTO = new DetailEvaluationManageDTO();
                if (!actionPlanList.isEmpty()) {
                    for (EvaluationManageDTO actionPlan : actionPlanList) {
                        AppParamsSmartPhone statusActionPlan = appParamsRepository.getAppParamsByTypeAndCode("ACTION_PLAN_STATUS", String.valueOf(actionPlan.getStatus()));
                        actionPlan.setStatusActionPlan(statusActionPlan.getValue());
                        reasonOfVisitPlanMapViewResponse.setActionPlan(actionPlan);
                    }
                }
                List<ReasonOfVisitPlanMapResponse> reasonOfVisitPlanMapResponseList = this.getReasonOfVisitPlanMap(request.getJobId(), request.getVisitPlanId());
                detailEvaluationManageDTO.setReasonOfVisitPlanMapResponses(reasonOfVisitPlanMapResponseList);
                detailEvaluations.add(detailEvaluationManageDTO);
                reasonOfVisitPlanMapViewResponse.setDetailEvaluations(detailEvaluations);
                reasonOfVisitPlanMapViewResponses.add(reasonOfVisitPlanMapViewResponse);
            }
        }
        detailEvaluationManageResponse.setDetailEvaluationManageAndActionPlan(reasonOfVisitPlanMapViewResponses);

        return detailEvaluationManageResponse;
    }

    public List<ReasonOfVisitPlanMapResponse> getReasonOfVisitPlanMap(Long jobId, Long visitPlanId) {
        List<ReasonOfVisitPlanMapResponse> reasonOfVisitPlanMapResponseList = new ArrayList<>();
        List<Job> jobLv2s = jobRepository.findJobByParentId(jobId, visitPlanId);
        List<Job> jobLv3s = jobRepository.findListChecklistLv3(visitPlanId);
        List<PlanResultForEvaluationDTO> planResults = planResultRepository.getPlanResult(visitPlanId);
        for (Job job2 : jobLv2s) {
            ReasonOfVisitPlanMapResponse reasonOfVisitPlanMapResponse = new ReasonOfVisitPlanMapResponse();
            List<ReasonOfVisitPlanDTO> reasonOfVisitPlanDTOS = new ArrayList<>();
            List<Job> jobLv3List = jobLv3s.stream().filter(j3 -> j3.getParentId().equals(job2.getJobId())).collect(Collectors.toList());
            if (planResults != null) {
                for (Job job3 : jobLv3List) {
                    List<String> filePaths = new ArrayList<>();
                    ReasonOfVisitPlanDTO reasonOfVisitPlanDTO = new ReasonOfVisitPlanDTO();
                    if (job3 != null) {
                        reasonOfVisitPlanDTO.setJobName3(job3.getName());
                        reasonOfVisitPlanDTO.setJobCode3(job3.getCode());
                        for (PlanResultForEvaluationDTO planResult : planResults) {
                            if (planResult.getItemConfig() != null) {
                                if(planResult.getItemConfig().getJobId().equals(job3.getJobId())){
                                    reasonOfVisitPlanDTO.setOptionOfAnswer(planResult.getPlanResult().getResult());
                                    if (planResult.getPlanResult().getFilePath() != null ) {
                                        filePaths = Arrays.asList(planResult.getPlanResult().getFilePath().split("-"));
                                    }
                                    if(planResult.getPlanResult().getReasonId() != null){
                                        Reason reason = reasonRepository.findById(planResult.getPlanResult().getReasonId()).orElse(null);
                                        if(reason != null){
                                            reasonOfVisitPlanDTO.setReasonOfNotOK(reason.getName());
                                        }
                                    }
                                }
                            }
                        }
                        reasonOfVisitPlanDTO.setFilePaths(filePaths);
                        reasonOfVisitPlanDTOS.add(reasonOfVisitPlanDTO);
                    }
                }
            }
            reasonOfVisitPlanMapResponse.setJobName2(job2.getName());
            reasonOfVisitPlanMapResponse.setJobCode2(job2.getCode());
            reasonOfVisitPlanMapResponse.setReasonOfVisitPlanMapResponses(reasonOfVisitPlanDTOS);
            reasonOfVisitPlanMapResponseList.add(reasonOfVisitPlanMapResponse);
        }
        return reasonOfVisitPlanMapResponseList;
    }

    public VisitPlanMap editActionPlan(EvaluationManageRequest request) throws ParseException {
        VisitPlanMap visitPlanMap = visitPlanMapRepository.findById(request.getVisitPlanId()).orElse(null);
        if (visitPlanMap != null) {
            visitPlanMap.setCheckListResultStatus(request.getCheckRejectOrApprove());
            if(visitPlanMap.getCheckListResultStatus() == Constants.VISIT_PLAN_RESULT_REJECT){
                visitPlanMap.setActionPlanComment(request.getComment());
                String content = messageSource.getMessage(messageSource.getMessage(MessageKey.VISIT_PLAN_RESULT_REJECT, null, locale)
                        , new String[]{request.getChannelCode(), DateUtil.date2ddMMyyyyString(visitPlanMap.getVisitTime())}, locale);
                this.saveSmsWhenReject(request.getAuditor(), content);
            }else{
                if (visitPlanMap.getStatus().equals(In_process) || visitPlanMap.getStatus().equals(Pending) || visitPlanMap.getStatus().equals(Request_update)) {
                    visitPlanMap.setZonalId(request.getAuditorId());
                    visitPlanMap.setDatePlan(DateUtils.parseDate(request.getScheduledDate(), "dd/MM/yyyy"));
                }
            }
            visitPlanMapRepository.save(visitPlanMap);
            return visitPlanMap;
        }
        return null;
    }

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

}
