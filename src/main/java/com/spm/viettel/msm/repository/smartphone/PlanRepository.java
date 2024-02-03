package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

//    @Query("SELECT DISTINCT p.channelTypeId FROM Plan p")
    List<Plan> getPlansByCcAudit(Long ccAudit);

    @Query("SELECT DISTINCT p.channelTypeId FROM Plan p")
    String[] getChannelTypeIdsUsingOfCcAudit();

    @Query("SELECT DISTINCT p.channelTypeId FROM Plan p")
    List<Long> getChannelTypeIdsOfCcAudit();

    @Query("SELECT p FROM Plan p " +
            "WHERE (:channelTypeId IS NULL OR p.channelTypeId = :channelTypeId) " +
            "AND (:planName IS NULL OR (lower(p.name) LIKE %:planName% AND p.name IS NOT NULL))" +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:objectType IS NULL OR p.objectType = :objectType) " +
            "AND (:objectLevel IS NULL OR p.objectLevel = :objectLevel) " +
            "ORDER BY CASE WHEN p.lastUpdate IS NULL THEN 1 ELSE 0 END, p.lastUpdate DESC ")
    List<Plan> searchPlanByChannel(@RequestParam("channelTypeId") Long channelTypeId,
                                   @RequestParam("planName") String planName,
                                   @RequestParam("status") Long status,
                                   @RequestParam("objectLevel") Long objectLevel,
                                   @RequestParam("objectType") Long objectType);

    @Query("SELECT p FROM Plan p " +
            "WHERE p.status = 1 ")
    List<Plan> getFillPlanUsingAssignChecklist();

    @Query("SELECT p FROM Plan p WHERE lower(p.name) = lower(:planName) AND p.status = 1")
    Plan getQuantityPlanByPlanName(@RequestParam("planName") String planName);

    @Query("SELECT p FROM Plan p WHERE p.status = 1 AND p.channelTypeId = :channelTypeId AND p.objectLevel = :objectLevel")
    Plan getDataPlanByChannelTypeAndObjectLevel(@RequestParam("channelTypeId") Long channelTypeId,
                                                @RequestParam("objectLevel") Long objectLevel);
}
