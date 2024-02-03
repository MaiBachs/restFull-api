package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelCheckListResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapPlanChannelCheckListResultRepository extends GenericJpaRepository<MapPlanChannelCheckListResult,Long>, JpaRepository<MapPlanChannelCheckListResult,Long> {
    List<MapPlanChannelCheckListResult> findMapPlanChannelCheckListResultByMapPlanChannelIdAndMapPlanChannelCheckListId(Long planChannelId,Long checkListId);
}
