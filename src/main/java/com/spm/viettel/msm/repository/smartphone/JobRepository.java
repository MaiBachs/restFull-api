package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.dto.request.JobRequest;
import com.spm.viettel.msm.repository.smartphone.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Job getJobByCode(String code);

    @Query("SELECT j FROM Job j WHERE j.parentId IS NULL AND j.ccAudit = 1")
    List<Job> findEvaluation();
    @Query(value = "SELECT * FROM SMARTPHONE.JOB WHERE JOB_ID IN ( SELECT JOB_ID FROM SMARTPHONE.PLAN_JOB WHERE PLAN_ID IN ( SELECT PLAN_ID FROM SMARTPHONE.PLAN WHERE CHANNEL_TYPE_ID IN :channelTypeIds )) AND PARENT_ID IS NULL", nativeQuery = true)
    List<Job> findEvaluation(List<Long> channelTypeIds);

    @Query("SELECT g FROM Job g WHERE g.parentId IN (SELECT j.jobId FROM Job j WHERE j.parentId IS NULL AND j.ccAudit = 1)")
    List<Job> findGroup();
    @Query(value = "SELECT * FROM SMARTPHONE.JOB WHERE JOB_ID IN (SELECT JOB_ID FROM SMARTPHONE.JOB WHERE JOB_ID IN ( SELECT JOB_ID FROM SMARTPHONE.PLAN_JOB WHERE PLAN_ID IN ( SELECT PLAN_ID FROM SMARTPHONE.PLAN WHERE CHANNEL_TYPE_ID IN :channelTypeIds )) AND PARENT_ID IS NULL)", nativeQuery = true)
    List<Job> findGroup(List<Long> channelTypeIds);

    List<Job> findJobByParentIdAndStatus(Long parentId, Long status);

    @Query("SELECT job2 FROM Job job2 WHERE job2.parentId = :parentId " +
            "AND job2.jobId IN (SELECT ic.groupId FROM ItemConfig ic " +
            "JOIN PlanResult pr ON pr.itemConfigId = ic.id WHERE pr.visitPlanId = :visitPlanId)")
    List<Job> findJobByParentId(@Param("parentId") Long parentId,
                                @Param("visitPlanId") Long visitPlanId);

    List<Job> findJobByStatus(Long status);

    List<Job> findJobByParentIdInAndStatus(List<Long> parentIds, Long status);

    @Query(value = "SELECT j FROM Job j WHERE j.ccAudit = 1")
    List<Job> getJobByCCAudit();

    @Query(value = "SELECT new com.spm.viettel.msm.dto.request.JobRequest(j.jobId, j.parentId, j.name, j.code " +
            ", jpr.name,jpr.code) FROM Job j LEFT JOIN Job jpr ON j.parentId = jpr.jobId " +
            "WHERE j.status = 1 " +
            "AND (:name IS NULL OR lower(j.name) LIKE %:name%) " +
            "AND (:code IS NULL OR lower(j.code) LIKE %:code%) " +
            "AND (:parentId IS NULL OR j.parentId = :parentId) " +
            "ORDER BY  CASE WHEN j.lastUpdate IS NULL THEN 1 ELSE 0 END, j.lastUpdate DESC")
    List<JobRequest> searchJobByNameAndCodeAndParentId(@RequestParam("name") String name,
                                                       @RequestParam("code") String code,
                                                       @RequestParam("parentId") Long parentId);

    @Query(value = "SELECT DISTINCT new com.spm.viettel.msm.dto.request.JobRequest(j. jobId, j.code,j.name, j.parentId, " +
            "j.resultDataType) FROM Job j WHERE j.status = 1 AND j.parentId IS NULL")
    List<JobRequest> getFillCheckListLevel1();

    @Query(value = "SELECT DISTINCT new com.spm.viettel.msm.dto.request.JobRequest(j.jobId, j.code, j.name, j.parentId, " +
            "j.resultDataType) FROM Job j WHERE j.status = 1 AND j.parentId = :parentId")
    List<JobRequest> getFillCheckListByParentId(@RequestParam("parentId") Long parentId);

    @Query(value = "SELECT DISTINCT new com.spm.viettel.msm.dto.request.JobRequest(j.jobId, j.code, j.name, j.parentId, " +
            "j.resultDataType) FROM Job j " +
            "WHERE j.status = 1 AND j.parentId = :parentId " +
            "AND j.jobId NOT IN (SELECT pj.jobId FROM PlanJob pj WHERE pj.planId = :planId)")
    List<JobRequest> getFillCheckListLv3NotAssignByParentId(@RequestParam("parentId") Long parentId,
                                                            @RequestParam("planId") Long planId);

    @Query(value = "SELECT DISTINCT new com.spm.viettel.msm.dto.request.JobRequest(j.jobId, j.code, j.name, j.parentId, " +
            "j.resultDataType) FROM Job j WHERE j.status = 1 AND j.jobId = :jobId")
    List<JobRequest> getCheckListByJobId(@RequestParam("jobId") Long jobId);

    @Query(value = "SELECT DISTINCT new com.spm.viettel.msm.dto.request.JobRequest(j.jobId, j.code, j.name, j.parentId, " +
            "j.resultDataType) FROM Job j WHERE j.status = 1 AND j.jobId = :jobId")
    JobRequest getFillCheckListByJobId(@RequestParam("jobId") Long jobId);

    @Query(value = "SELECT j FROM Job j WHERE j.status = 1 AND lower(j.code) = lower(:code) ")
    Job getCheckListByCode(@RequestParam("code") String code);

    @Query("SELECT j.parentId FROM Job j WHERE j.jobId = :jobId")
    Long getParentIdUsingSearchAssignCheckList(@RequestParam("jobId") Long jobId);

    @Query("SELECT j.jobId FROM Job j WHERE j.parentId = :parentId AND j.status = 1")
    List<Long> findJobIdByParentIdAndStatus(@RequestParam("parentId") Long parentId);

    @Query(value = "SELECT DISTINCT new com.spm.viettel.msm.dto.request.JobRequest(j1. jobId, j1.code,j1.name, j1.parentId, " +
            "j1.resultDataType) FROM Job j1 JOIN Job j2 ON j2.parentId = j1.jobId " +
            "JOIN Job j3 ON j3.parentId = j2.jobId WHERE j1.status = 1 AND j1.parentId IS NULL " +
            "AND j2.status = 1 AND j3.status = 1")
    List<JobRequest> getFillCheckListLevel1HaveLevel2AndLevel3();


    @Query("SELECT j FROM Job j WHERE j.parentId IS NULL AND j.status = 1 AND j.jobId = :jobId")
    Job findChecklistLv1(@RequestParam("jobId") Long jobId);

    @Query("SELECT j FROM Job j WHERE j.jobId = :jobId AND j.parentId IN " +
            "(SELECT j.jobId FROM Job j WHERE j.parentId IS NULL AND j.status = 1) AND j.status = 1 ")
    Job findChecklistLv2(@RequestParam("jobId") Long jobId);

    @Query("SELECT j FROM Job j WHERE  j.status = 1 AND j.parentId IN (SELECT j.jobId FROM Job j WHERE j.parentId IN " +
            "(SELECT j.jobId FROM Job j WHERE j.parentId IS NULL AND j.status = 1) AND j.status = 1) AND j.jobId = :jobId")
    Job findChecklistLv3(@RequestParam("jobId") Long jobId);

    @Query("SELECT j FROM Job j WHERE  j.status = 1 AND j.parentId IN (SELECT j.jobId FROM Job j WHERE j.parentId IN " +
            "(SELECT j.jobId FROM Job j WHERE j.parentId IS NULL AND j.status = 1) AND j.status = 1)")
    List<Job> findListChecklistLv3Active();

    @Query("SELECT j FROM Job j WHERE j.jobId IN (SELECT ic.jobId FROM ItemConfig ic " +
            "JOIN PlanResult pr ON pr.itemConfigId = ic.id WHERE pr.visitPlanId = :visitPlanId)")
    List<Job> findListChecklistLv3(@Param("visitPlanId") Long visitPlanId);
}
