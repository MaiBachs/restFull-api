package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.PlanSaleTargetDto;
import com.spm.viettel.msm.repository.sm.entity.MapPlanSaleTarget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MapPlanSaleTargetRepository extends GenericJpaRepository<MapPlanSaleTarget, Long> {

    @TemplateQuery
    List<PlanSaleTargetDto> search(@Param("brCode") String brCode,
                                   @Param("bcCode") String bcCode,
                                   @Param("planType") Integer planType,
                                   @Param("targetLevel") Integer targetLevel,
                                   @Param("staffCode") String staffCode,
                                   @Param("year") String year);

    @Query("SELECT t FROM MapPlanSaleTarget t WHERE t.status <> -3 AND t.brCode = :brCode and t.targetLevel = :targetLevel AND trunc(t.planDate) = trunc(:planDate)")
    MapPlanSaleTarget findSaleTargetByBrCodeAndTargetLevelAndPlanDate(@Param("brCode") String brCode
                                                                    , @Param("targetLevel") Integer targetLevel
                                                                    , @Param("planDate") Date planDate);

    @Query("SELECT t FROM MapPlanSaleTarget t WHERE t.status <> -3 AND t.bcCode = :bcCode and t.targetLevel = :targetLevel AND trunc(t.planDate) = trunc(:planDate)")
    MapPlanSaleTarget findSaleTargetByBcCodeAndTargetLevelAndPlanDate(@Param("bcCode") String bcCode
                                                                    , @Param("targetLevel") Integer targetLevel
                                                                    , @Param("planDate") Date planDate);

    @Query("SELECT t FROM MapPlanSaleTarget t WHERE t.status <> -3 AND t.bcCode = :bcCode and t.staffCode= :staffCode and t.targetLevel = :targetLevel AND trunc(t.planDate) = trunc(:planDate)")
    MapPlanSaleTarget findSaleTargetByBcCodeAndStaffCodeAndTargetLevelAndPlanDate(@Param("bcCode") String bcCode
                                                                                , @Param("staffCode") String staffCode
                                                                                , @Param("targetLevel") Integer targetLevel
                                                                                , @Param("planDate") Date planDate);
}
