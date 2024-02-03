package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.PlanChannelTarget;
import com.spm.viettel.msm.dto.TargetChannelDto;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelTarget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MapPlanChannelTargetRepository extends GenericJpaRepository<MapPlanChannelTarget, Long> {
    @TemplateQuery
    List<PlanChannelTarget> searchPlanChannelTarget(
            @Param("brCode") String  brCode,
            @Param("bcCode") String  bcCode,
            @Param("planType") Integer  planType,
            @Param("targetLevel") Integer  targetLevel,
            @Param("staffCode") String  staffCode,
            @Param("channelId") Long  channelId,
            @Param("year") String  planDate
    );


    @Query(value = "SELECT t FROM MapPlanChannelTarget t WHERE t.brCode = :brCode AND t.channelId = :channelId AND t.targetLevel = :targetLevel AND trunc(t.planDate) = trunc(:planDate)")
    List<MapPlanChannelTarget> findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(String brCode, Long channelId, Integer targetLevel, Date planDate);
    @Query(value = "SELECT t FROM MapPlanChannelTarget t WHERE t.brCode = :brCode AND t.channelId = :channelId AND t.targetLevel = :targetLevel AND t.targetType = :targetType AND trunc(t.planDate) = trunc(:planDate)")
    List<MapPlanChannelTarget> findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(String brCode, Long channelId, Integer targetLevel,Integer targetType, Date planDate);

    @Query(value = "SELECT t FROM MapPlanChannelTarget t WHERE t.bcCode = :bcCode AND t.channelId = :channelId and t.targetLevel = :targetLevel AND trunc(t.planDate) = trunc(:planDate)")
    List<MapPlanChannelTarget> findTargetByBcCodeAndChannelIdAndTargetLevelAndPlanDate(String bcCode, Long channelId, Integer targetLevel, Date planDate);
    @Query(value = "SELECT t FROM MapPlanChannelTarget t WHERE t.bcCode = :bcCode AND t.channelId = :channelId and t.targetLevel = :targetLevel AND t.targetType = :targetType AND trunc(t.planDate) = trunc(:planDate)")
    List<MapPlanChannelTarget> findTargetByBcCodeAndChannelIdAndTargetLevelAndPlanDate(String bcCode, Long channelId, Integer targetLevel,Integer targetType, Date planDate);
    @Query(value = "SELECT t FROM MapPlanChannelTarget t WHERE t.brCode = :brCode AND t.targetLevel = :targetLevel AND trunc(t.planDate) = trunc(:planDateYear)")
    List<MapPlanChannelTarget> findTargetByBrCodeAndTargetLevelAndPlanDate(String brCode, Integer targetLevel, Date planDateYear);

    @Query(value = "SELECT t FROM MapPlanChannelTarget t WHERE t.brCode = :brCode AND t.targetLevel = :targetLevel AND t.targetType = :targetType AND trunc(t.planDate) = trunc(:planDateYear)")
    List<MapPlanChannelTarget> findTargetByBrCodeAndTargetLevelAndPlanDate(String brCode, Integer targetLevel,Integer targetType, Date planDateYear);

    @Query(value = "SELECT t FROM MapPlanChannelTarget t WHERE t.bcCode = :bcCode and t.staffCode= :staffCode AND t.channelId = :channelId and t.targetLevel = :targetLevel AND t.targetType = :targetType AND trunc(t.planDate) = trunc(:planDate)")
    List<MapPlanChannelTarget> findTargetByBcCodeAndStaffCodeAndChannelIdAndTargetLevelAndPlanDate(String bcCode, String staffCode, Long channelId, Integer targetLevel,Integer targetType, Date planDate);

    @TemplateQuery
    List<TargetChannelDto> searchExportTargetChannelLevelSale(
            @Param("brCode") String  brCode,
            @Param("bcCode") String  bcCode,
            @Param("staffCode") String  staffCode,
            @Param("channelTypeId") Long  channelTypeId,
            @Param("targetLevel") Integer  targetLevel,
            @Param("year") String  year
    );

    @TemplateQuery
    List<TargetChannelDto> searchExportTargetChannelLevelBCAccum(
            @Param("brCode") String  brCode,
            @Param("bcCode") String  bcCode,
            @Param("staffCode") String  staffCode,
            @Param("channelTypeId") Long  channelTypeId,
            @Param("targetLevel") Integer  targetLevel,
            @Param("year") String  year
    );
}
