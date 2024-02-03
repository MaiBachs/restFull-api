package com.spm.viettel.msm.service;

import com.spm.viettel.msm.Constant;
import com.spm.viettel.msm.dto.request.PlanJobRequest;
import com.spm.viettel.msm.dto.request.PlanRequest;
import com.spm.viettel.msm.dto.response.AssignCheckListResponseLv2;
import com.spm.viettel.msm.dto.response.AssignChecklistResponseLv1;
import com.spm.viettel.msm.dto.response.ViewAssignChecklistResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.smartphone.JobRepository;
import com.spm.viettel.msm.repository.smartphone.PlanJobRepository;
import com.spm.viettel.msm.repository.smartphone.PlanRepository;
import com.spm.viettel.msm.repository.smartphone.PlanResultRepository;
import com.spm.viettel.msm.repository.smartphone.entity.Plan;
import com.spm.viettel.msm.repository.smartphone.entity.PlanJob;
import com.spm.viettel.msm.repository.smartphone.entity.PlanResult;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PlanJobService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlanResultRepository planResultRepository;

    @Autowired
    private PlanJobRepository planJobRepository;

    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    public List<PlanRequest> getFillPlanUsingAssignChecklist() {
        List<Plan> plan = planRepository.getFillPlanUsingAssignChecklist();
        List<PlanRequest> planRequests = new ArrayList<>();
        plan.forEach(pl -> {
            PlanRequest request = PlanRequest.builder()
                    .channelTypeId(pl.getChannelTypeId())
                    .planId(pl.getPlanId())
                    .planName(pl.getName())
                    .build();
            planRequests.add(request);
        });
        return planRequests;
    }

    public ResponseEntity<?> newAssignCheckList(List<PlanJobRequest> planJobRequests) {
        List<PlanJob> planJobs = new ArrayList<>();
        for (PlanJobRequest j : planJobRequests) {
            if (j.getPlanId() == null || j.getPlanId().toString().isEmpty()) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.PLAN_NAME_NOT_NULL, null, locale));
            }
            PlanJob assignCheckListExisted = planJobRepository.checkDataAssignChecklist(j.getJobId(), j.getPlanId());
            if (assignCheckListExisted == null) {
                PlanJob planJob = new PlanJob();
                planJob.setStatus(Constants.STATUS_ACTIVE);
                planJob.setPlanId(j.getPlanId());
                planJob.setJobId(j.getJobId());
                planJob.setRequired(j.getRequired());
                planJob.setRequiredFile(j.getRequiredFile());
                planJob.setIdx(j.getIdx());
                planJob.setCreatedDate(new Date());
                planJob.setExpiredInDay(j.getExpiredInDay());
                planJobs.add(planJob);
            } else {
                assignCheckListExisted.setStatus(Constants.STATUS_ACTIVE);
                assignCheckListExisted.setPlanId(j.getPlanId());
                assignCheckListExisted.setJobId(j.getJobId());
                assignCheckListExisted.setRequired(j.getRequired());
                assignCheckListExisted.setRequiredFile(j.getRequiredFile());
                assignCheckListExisted.setIdx(j.getIdx());
                assignCheckListExisted.setLastUpdate(new Date());
                assignCheckListExisted.setExpiredInDay(j.getExpiredInDay());
                planJobs.add(assignCheckListExisted);
            }
            planJobRepository.saveAll(planJobs);
        }
        return new ResponseEntity<>(planJobs, HttpStatus.OK);
    }

    public ResponseEntity<?> searchAssignCheckList(PlanJobRequest request) {
        ViewAssignChecklistResponse viewAssignChecklistResponse = new ViewAssignChecklistResponse();
        List<AssignChecklistResponseLv1> searchAssignCheckListResponsesLV1 = planJobRepository
                .getAssignCheckListPR1(request.getPlanId());
        List<AssignCheckListResponseLv2> searchAssignCheckListResponsesLV2 = planJobRepository
                .getAssignCheckListPR2();
        List<PlanJobRequest> getAssignCheckListPR3Views = planJobRepository
                .getAssignCheckListPR3();
        List<AssignChecklistResponseLv1> searchAssignCheckListResponsesLV1View = searchAssignCheckListResponsesLV1.stream()
                .filter(prj1 -> {
                    List<AssignCheckListResponseLv2> searchAssignCheckListResponsesLV2View = searchAssignCheckListResponsesLV2
                            .stream()
                            .filter(lv2 -> lv2.getPlanId().equals(prj1.getPlanId()) &&
                                    lv2.getParentId().equals(prj1.getCheckListParentId1()))
                            .collect(Collectors.toList());
                    prj1.setSearchAssignCheckListResponseLv2(searchAssignCheckListResponsesLV2View);

                    searchAssignCheckListResponsesLV2View.forEach(prj2 -> {
                        List<PlanJobRequest> getAssignCheckListPR3Views1 = getAssignCheckListPR3Views
                                .stream()
                                .filter(ac ->
                                        ac.getParentIdOfJob().equals(prj2.getCheckListParentId2()) &&
                                                ac.getPlanId().equals(prj2.getPlanId())
                                )
                                .collect(Collectors.toList());
                        prj2.setCheckListParent3s(getAssignCheckListPR3Views1);
                    });

                    return !searchAssignCheckListResponsesLV2View.isEmpty();
                })
                .collect(Collectors.toList());
        viewAssignChecklistResponse.setSearchAssignCheckListResponses(searchAssignCheckListResponsesLV1View);
//        if (request.isPaging()) {
//            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
//            int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 0;
//            Long countResults = (long) searchAssignCheckListResponses.size();
//            int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
//            if (currentPage < 0) {
//                currentPage = 0;
//            } else if (currentPage > lastPageNumber) {
//                currentPage = lastPageNumber;
//            }
//            viewAssignChecklistResponse.setTotalRecord(countResults);
//            viewAssignChecklistResponse.setTotalPage(lastPageNumber);
//            viewAssignChecklistResponse.setCurrentPage(currentPage);
//            int startIndex = currentPage * pageSize;
//            int endIndex = Math.min(startIndex + pageSize, searchAssignCheckListResponses.size());
//            viewAssignChecklistResponse.setSearchAssignCheckListResponses(searchAssignCheckListResponses.
//                    subList(startIndex, endIndex));
//        }
        return new ResponseEntity<>(viewAssignChecklistResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> editAssignCheckList(List<PlanJobRequest> jobRequests) {
        List<PlanJob> planJobs = new ArrayList<>();
        jobRequests.forEach(j -> {
            PlanJob assignCheckListExisted = planJobRepository.checkDataAssignChecklist(j.getJobId(), j.getPlanId());
            if (assignCheckListExisted != null) {
                assignCheckListExisted.setStatus(Constants.STATUS_ACTIVE);
                assignCheckListExisted.setPlanId(j.getPlanId());
                assignCheckListExisted.setJobId(j.getJobId());
                assignCheckListExisted.setRequired(j.getRequired());
                assignCheckListExisted.setRequiredFile(j.getRequiredFile());
                assignCheckListExisted.setIdx(j.getIdx());
                assignCheckListExisted.setLastUpdate(new Date());
                assignCheckListExisted.setExpiredInDay(j.getExpiredInDay());
                planJobs.add(assignCheckListExisted);
            }
            planJobRepository.saveAll(planJobs);
        });
        return new ResponseEntity<>(planJobs, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteAssignCheckList(List<PlanJobRequest> planJobRequests) {
        List<PlanJob> planJobs = planJobRepository.findAll();
        for (PlanJobRequest planJobRequest : planJobRequests) {
            PlanResult planResult = planResultRepository.checkPlanResultUsingAssignChecklist(planJobRequest.getPlanJobId());
            PlanJob planJob = planJobs.stream().filter(pl -> pl.getPlanJobId().equals(planJobRequest.getPlanJobId()))
                    .findFirst().orElse(null);
            if (planResult != null) {
                planResult.setStatus(Constants.INACTIVE);
                planResultRepository.save(planResult);
                planJob.setStatus(Constants.INACTIVE);
                planJobRepository.save(planJob);
            } else {
                planJobRepository.delete(planJob);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
