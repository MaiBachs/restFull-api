package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.dto.PlanResultForEvaluationDTO;
import com.spm.viettel.msm.repository.smartphone.entity.PlanResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PlanResultRepository extends JpaRepository<PlanResult, Long> {

    @Query("SELECT pr FROM PlanResult pr WHERE pr.planJobId = :planJobId")
    PlanResult checkPlanResultUsingAssignChecklist(@RequestParam("planJobId") Long planJobId);

    @Query("SELECT pr FROM PlanResult pr " +
            "WHERE pr.visitPlanId = :visitPlanId ")
    List<PlanResult> getPlanResultByVisitPlanMapId(@RequestParam("visitPlanId") Long visitPlanId);

    @Query("SELECT COUNT(pr) " +
            "FROM PlanResult pr " +
            "LEFT JOIN PlanJob pj ON (pr.planJobId  = pj.planJobId) " +
            "WHERE pr.visitPlanId = :visitPlanId " +
            "AND pr.status = 1 AND pj.required = 1 " +
            "AND (pr.result = 'false' OR pr.result IS NULL)")
    Integer VisitPlanChecklistResult_CheckResultIsOk(Long visitPlanId);

    @Query("SELECT new com.spm.viettel.msm.dto.PlanResultForEvaluationDTO(pr, ic) FROM PlanResult pr JOIN ItemConfig ic ON ic.id = pr.itemConfigId " +
            "WHERE pr.visitPlanId = :visitPlanId")
    List<PlanResultForEvaluationDTO> getPlanResult(@RequestParam("visitPlanId") Long visitPlanId);

}
