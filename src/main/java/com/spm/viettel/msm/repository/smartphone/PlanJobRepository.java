package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.dto.request.PlanJobRequest;
import com.spm.viettel.msm.dto.response.AssignCheckListResponseLv2;
import com.spm.viettel.msm.dto.response.AssignChecklistResponseLv1;
import com.spm.viettel.msm.repository.smartphone.entity.PlanJob;
import com.spm.viettel.msm.repository.smartphone.entity.PlanResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PlanJobRepository extends JpaRepository<PlanJob, Long> {

    @Query("SELECT pj FROM PlanJob pj WHERE pj.jobId = :jobId " +
            "AND pj.planId = :planId " +
            "AND pj.status = 1")
    PlanJob checkDataAssignChecklist(@RequestParam("jobId") Long jobId,
                                     @RequestParam("planId") Long planId);

    @Query("SELECT pj FROM PlanJob pj WHERE pj.jobId = :jobId ")
    PlanJob getDataAssignChecklist(@RequestParam("jobId") Long jobId);

    @Query("SELECT new com.spm.viettel.msm.dto.response.AssignChecklistResponseLv1( " +
            "pj1.planId, jpr1.jobId, pj1.planJobId, jpr1.name,jpr1.code, pj1.idx) " +
            "FROM Job jpr1 " +
            "JOIN PlanJob pj1 ON pj1.jobId = jpr1.jobId " +
            "JOIN Plan pl ON pl.planId = pj1.planId " +
            "WHERE (:planId IS NULL OR jpr1.jobId IN (SELECT pj.jobId FROM PlanJob pj " +
            "WHERE (:planId IS NULL OR pj.planId = :planId) AND pj.status = 1 )) " +
            "AND jpr1.status = 1 AND pj1.status = 1 AND jpr1.parentId IS NULL AND pl.status = 1 " +
            "AND (:planId IS NULL OR pj1.planId = :planId) " +
            "GROUP BY pj1.planId, jpr1.jobId,pj1.planJobId, jpr1.name,jpr1.code, pj1.idx " +
            "ORDER BY pj1.idx ")
    List<AssignChecklistResponseLv1> getAssignCheckListPR1(@RequestParam("planId") Long planId);

    @Query("SELECT new com.spm.viettel.msm.dto.response.AssignCheckListResponseLv2( " +
            "pj2.planId,jpr2.parentId, jpr2.jobId,pj2.planJobId, jpr2.name,jpr2.code, pj2.idx) " +
            "FROM Job jpr2 " +
            "JOIN PlanJob pj2 ON pj2.jobId = jpr2.jobId " +
            "JOIN Plan pl ON pl.planId = pj2.planId " +
            "WHERE  jpr2.jobId IN ((SELECT j.parentId FROM Job j WHERE j.jobId " +
            "IN (SELECT pj.jobId FROM PlanJob pj WHERE pj.status = 1))) " +
            "AND jpr2.status = 1 AND pj2.status = 1 AND jpr2.parentId IS NOT NULL AND pl.status = 1 "+
            "GROUP BY pj2.planId,jpr2.parentId, jpr2.jobId,pj2.planJobId, jpr2.name,jpr2.code, pj2.idx " +
            "ORDER BY  pj2.idx")
    List<AssignCheckListResponseLv2> getAssignCheckListPR2();

    @Query("SELECT new com.spm.viettel.msm.dto.request.PlanJobRequest( pj.planJobId, pj.planId, pj.jobId,j.parentId," +
            " j.name, j.code, pj.required, pj.requiredFile, pj.expiredInDay, pj.idx, pj.status, j.resultDataType) " +
            "FROM PlanJob pj JOIN Job j ON j.jobId = pj.jobId " +
            "JOIN Plan pl ON pl.planId = pj.planId " +
            "WHERE pj.status = 1 AND pl.status = 1 " +
            "AND j.jobId IN (SELECT j3.jobId FROM Job j3 WHERE j3.status = 1 AND j3.parentId IN " +
            "(SELECT j2.jobId FROM Job j2 WHERE j2.status = 1 AND j2.parentId IN " +
            "(SELECT j.jobId FROM Job j WHERE j.status = 1 AND j.parentId IS NULL))) " +
            "GROUP BY pj.planJobId, pj.planId, pj.jobId,j.parentId, j.name,j.code, pj.required, pj.requiredFile, " +
            "pj.expiredInDay, pj.idx, pj.status, j.resultDataType " +
            "ORDER BY pj.planId , pj.idx ASC ")
    List<PlanJobRequest> getAssignCheckListPR3();

    @Query("SELECT pj FROM PlanJob pj WHERE pj.planId = :planId")
    List<PlanJob> checkPlanJobUsingManagementPlan(@RequestParam("planId") Long planId);

}
