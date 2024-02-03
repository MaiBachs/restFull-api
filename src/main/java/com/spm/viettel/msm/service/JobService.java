package com.spm.viettel.msm.service;

import com.spm.viettel.msm.Constant;
import com.spm.viettel.msm.dto.request.JobRequest;
import com.spm.viettel.msm.dto.request.PlanJobRequest;
import com.spm.viettel.msm.dto.response.ViewChecklistResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.smartphone.JobRepository;
import com.spm.viettel.msm.repository.smartphone.PlanJobRepository;
import com.spm.viettel.msm.repository.smartphone.PlanRepository;
import com.spm.viettel.msm.repository.smartphone.PlanResultRepository;
import com.spm.viettel.msm.repository.smartphone.entity.Job;
import com.spm.viettel.msm.repository.smartphone.entity.Plan;
import com.spm.viettel.msm.repository.smartphone.entity.PlanJob;
import com.spm.viettel.msm.repository.smartphone.entity.PlanResult;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobService extends BaseService {
    private final Logger loggerFactory = LoggerFactory.getLogger(JobService.class);


    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlanJobRepository planJobRepository;

    @Autowired
    private PlanResultRepository planResultRepository;

    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private ResponseFactory responseFactory;
    @Qualifier("smartphoneSessionFactory")
    private SessionFactory smartPhoneSessionFactory;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    public List<Job> findJobsByChannelTypeIdAndParentId(Long channelTypeId, Long parentId) {
        return findJobsByChannelTypeIdAndParent(channelTypeId, parentId);
    }

    public List<Job> findJobsByAudit() {
        return evaluationService.findJobsByAudit();
    }

    public List<Job> findEvaluation() {
//        List<Long> channelTypeIds = planRepository.getChannelTypeIdsOfCcAudit();
        return jobRepository.findEvaluation();
    }

    public List<Job> findGroup() {
//        List<Long> channelTypeIds = planRepository.getChannelTypeIdsOfCcAudit();
        return jobRepository.findGroup();
    }

    public List<Job> findJobByParentIdAndStatus(Long parentId) {
        return jobRepository.findJobByParentIdAndStatus(parentId, Constants.STATUS_ACTIVE);
    }

    public List<Job> findJobByParentIdInAndStatus(List<Long> parentIds) {
        return jobRepository.findJobByParentIdInAndStatus(parentIds, Constants.STATUS_ACTIVE);
    }

    public Job findById(Long jobId) {
        return jobRepository.findById(jobId).orElse(null);
    }

    public Job isJob(List<Job> allJob, String jobCode) {
        for (Job job : allJob) {
            if (jobCode.equalsIgnoreCase(job.getCode())) {
                return job;
            }
        }
        return null;
    }

    public List<Job> findJobsByChannelTypeIdAndParent(Long channelTypeId, Long parentId) {
        Session smartphoneSession = null;
        List<Job> jobs = new ArrayList<>();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("parentId", parentId);
            model.put("channelTypeId", channelTypeId);
            smartphoneSession = smartPhoneSessionFactory.openSession();
            Query query = getSessionFactory(getClass(), "JobResult.sftl", model, smartphoneSession);
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

    //HieuNd: Checklist

    public List<JobRequest> getFillCheckListLevel1HaveLevel2AndLevel3() {
        return jobRepository.getFillCheckListLevel1HaveLevel2AndLevel3();
    }

    public JobRequest fillCheckListByJobId(JobRequest request) {
        return jobRepository.getFillCheckListByJobId(request.getJobId());
    }

    public List<JobRequest> fillCheckListByParentId(JobRequest request) {
        return jobRepository.getFillCheckListByParentId(request.getParentId());
    }

    public List<JobRequest> fillCheckListLv3NotAssignByParentId(JobRequest request) {
        return jobRepository.getFillCheckListLv3NotAssignByParentId(request.getParentId(), request.getPlanId());
    }

    public List<JobRequest> fillCheckListLevel1() {
        return jobRepository.getFillCheckListLevel1();
    }

    public ViewChecklistResponse searchJobByParentIdAndNameAndCode(JobRequest request) {
        List<JobRequest> jobs = jobRepository.searchJobByNameAndCodeAndParentId(request.getName().toLowerCase().trim()
                , request.getCode().toLowerCase().trim(), request.getParentId());
        ViewChecklistResponse resultChecklistRespone = new ViewChecklistResponse();
        if (request.isPaging()) {
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
            int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 0;
            Long countResults = (long) jobs.size();
            int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
            if (currentPage < 0) {
                currentPage = 0;
            } else if (currentPage > lastPageNumber) {
                currentPage = lastPageNumber;
            }
            resultChecklistRespone.setTotalRecord(countResults);
            resultChecklistRespone.setTotalPage(lastPageNumber);
            resultChecklistRespone.setCurrentPage(currentPage);
            int startIndex = currentPage * pageSize;
            int endIndex = Math.min(startIndex + pageSize, jobs.size());
            resultChecklistRespone.setSearchChecklistRespones(jobs.subList(startIndex, endIndex));
        }
        return resultChecklistRespone;
    }

    public ResponseEntity<?> addNewCheckList(JobRequest request) {
        Job job = new Job();
        job.setCategory(0L);
        job.setStatus(Constants.STATUS_ACTIVE);
        if (StringUtils.isEmpty(request.getName().trim())) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.CHECKLIST_NAME_NOT_NULL, null, locale));
        }
        if (StringUtils.isEmpty(request.getCode().trim())) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.CHECKLIST_CODE_NOT_NULL, null, locale));
        }
        Job jobCheckCode = jobRepository.getCheckListByCode(request.getCode().trim());
        if (jobCheckCode != null ) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.CODE_IS_EXISTED, null, locale));
        }
        job.setName(request.getName().trim());
        job.setCode(request.getCode().trim());
        job.setParentId(request.getParentId());
        job.setResultDataType(1L);//1-photo
        job.setCreatedDate(new Date());
        job.setLastUpdate(new Date());
        jobRepository.save(job);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    public ResponseEntity<?> editCheckList(JobRequest request) {
        Job job = this.findById(request.getJobId());
        if (job == null) {
            return responseFactory.error(HttpStatus.NOT_FOUND, String.valueOf(HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND.toString());
        }
        if (StringUtils.isEmpty(request.getName().trim())) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.CHECKLIST_NAME_NOT_NULL, null, locale));
        }
        if (StringUtils.isEmpty(request.getCode().trim())) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.CHECKLIST_CODE_NOT_NULL, null, locale));
        }
        Job jobCheckCode = jobRepository.getCheckListByCode(request.getCode().trim());
        if ((jobCheckCode != null && !jobCheckCode.getJobId().equals(request.getJobId()))) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.CODE_IS_EXISTED, null, locale));
        }
        job.setLastUpdate(new Date());
        job.setName(request.getName().trim());
        job.setCode(request.getCode().trim());
        job.setParentId(request.getParentId());
        jobRepository.save(job);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteCheckList(JobRequest request) {
        Job jobLv1 = jobRepository.findChecklistLv1(request.getJobId());
        Job jobLv2 = jobRepository.findChecklistLv2(request.getJobId());
        Job jobLv3 = jobRepository.findChecklistLv3(request.getJobId());
        if (jobLv1 == null) {
            //<editor-fold defaultstate="collapsed" desc="JOB IS LEVEL 3">
            if (jobLv2 == null) {
                if (jobLv3 == null) {
                    return responseFactory.error(HttpStatus.NOT_FOUND, String.valueOf(HttpStatus.NOT_FOUND.value()),
                            HttpStatus.NOT_FOUND.toString());
                } else {
                    PlanJob planJobLv3 = planJobRepository.getDataAssignChecklist(jobLv3.getJobId());
                    if (planJobLv3 == null) {
                        jobRepository.delete(jobLv3);
                    } else {
                        PlanResult planResult = planResultRepository.checkPlanResultUsingAssignChecklist(planJobLv3.getPlanJobId());
                        if (planResult == null) {
                            planJobRepository.delete(planJobLv3);
                            jobRepository.delete(jobLv3);
                        } else {
                            planResult.setStatus(Constants.INACTIVE);
                            planResultRepository.save(planResult);
                            jobLv3.setStatus(Constants.INACTIVE);
                            jobRepository.save(jobLv3);
                        }
                    }
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
            //</editor-fold
            //<editor-fold defaultstate="collapsed" desc="JOB IS LEVEL 2">
            else {
                List<Job> jobLv3s = jobRepository.findJobByParentIdAndStatus(jobLv2.getJobId(), Constants.STATUS_ACTIVE);
                PlanJob planJobLv2 = planJobRepository.getDataAssignChecklist(jobLv2.getJobId());
                if (jobLv3s.isEmpty()) {
                    if (planJobLv2 != null) {
                        planJobRepository.delete(planJobLv2);
                    }
                    jobRepository.delete(jobLv2);
                } else {
                    jobLv3s.forEach(j3 -> {
                        PlanJob planJobLv3 = planJobRepository.getDataAssignChecklist(j3.getJobId());
                        if (planJobLv3 == null) {
                            if (planJobLv2 != null) {
                                planJobRepository.delete(planJobLv2);
                            }
                            jobRepository.delete(jobLv2);
                            jobRepository.delete(j3);
                        } else {
                            PlanResult planResult = planResultRepository.checkPlanResultUsingAssignChecklist(planJobLv3.getPlanJobId());
                            if (planResult == null) {
                                planJobRepository.delete(planJobLv2);
                                planJobRepository.delete(planJobLv3);
                                jobRepository.delete(jobLv2);
                                jobRepository.delete(j3);
                            } else {
                                planResult.setStatus(Constants.INACTIVE);
                                planResultRepository.save(planResult);
                                jobLv2.setStatus(Constants.INACTIVE);
                                jobRepository.save(jobLv2);
                                j3.setStatus(Constants.INACTIVE);
                                jobRepository.save(j3);
                                planJobLv2.setStatus(Constants.INACTIVE);
                                planJobLv3.setStatus(Constants.INACTIVE);
                                planJobRepository.save(planJobLv2);
                                planJobRepository.save(planJobLv3);
                            }
                        }
                    });
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
            //</editor-fold>
        }
        //<editor-fold defaultstate="collapsed" desc="JOB IS LEVEL 1">
        else {
            List<Job> jobLv2s = jobRepository.findJobByParentIdAndStatus(jobLv1.getJobId(), Constants.STATUS_ACTIVE);
            PlanJob planJobLv1 = planJobRepository.getDataAssignChecklist(jobLv1.getJobId());
            if (jobLv2s.isEmpty()) {
                if (planJobLv1 != null) {
                    planJobRepository.delete(planJobLv1);
                }
                jobRepository.delete(jobLv1);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                List<Job> jobLv3s = jobRepository.findListChecklistLv3Active();
                jobLv2s.forEach(j2 -> {
                    PlanJob planJobLv2 = planJobRepository.getDataAssignChecklist(j2.getJobId());
                    List<Job> jobLv3sToDelete = jobLv3s.stream()
                            .filter(lv3 -> lv3.getParentId().equals(j2.getJobId()))
                            .collect(Collectors.toList());
                    if (jobLv3sToDelete.isEmpty()) {
                        if (planJobLv2 != null) {
                            planJobRepository.delete(planJobLv2);
                        }
                        if (planJobLv1 != null) {
                            planJobRepository.delete(planJobLv1);
                        }
                        jobRepository.delete(j2);
                        jobRepository.delete(jobLv1);
                    } else {
                        jobLv3sToDelete.forEach(j3 -> {
                            PlanJob planJobLv3 = planJobRepository.getDataAssignChecklist(j3.getJobId());
                            if (planJobLv3 == null) {
                                if (planJobLv2 != null) {
                                    planJobRepository.delete(planJobLv2);
                                }
                                if (planJobLv1 != null) {
                                    planJobRepository.delete(planJobLv1);
                                }
                                if (planJobLv3 != null) {
                                    planJobRepository.delete(planJobLv3);
                                }
                                jobRepository.delete(j3);
                                jobRepository.delete(j2);
                                jobRepository.delete(jobLv1);
                            } else {
                                PlanResult planResult = planResultRepository.checkPlanResultUsingAssignChecklist(planJobLv3.getPlanJobId());
                                if (planResult == null) {
                                    if (planJobLv2 != null) {
                                        planJobRepository.delete(planJobLv2);
                                    }
                                    if (planJobLv1 != null) {
                                        planJobRepository.delete(planJobLv1);
                                    }
                                    if (planJobLv3 != null) {
                                        planJobRepository.delete(planJobLv3);
                                    }
                                    jobRepository.delete(j3);
                                    jobRepository.delete(j2);
                                    jobRepository.delete(jobLv1);
                                } else {
                                    planResult.setStatus(Constants.INACTIVE);
                                    planResultRepository.save(planResult);
                                    jobLv1.setStatus(Constants.INACTIVE);
                                    jobRepository.save(jobLv1);
                                    jobLv2.setStatus(Constants.INACTIVE);
                                    jobRepository.save(jobLv2);
                                    j3.setStatus(Constants.INACTIVE);
                                    jobRepository.save(j3);
                                    planJobLv1.setStatus(Constants.INACTIVE);
                                    planJobLv2.setStatus(Constants.INACTIVE);
                                    planJobLv3.setStatus(Constants.INACTIVE);
                                    planJobRepository.save(planJobLv1);
                                    planJobRepository.save(planJobLv2);
                                    planJobRepository.save(planJobLv3);
                                }
                            }
                        });
                    }
                });
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //</editor-fold>
    }

}
