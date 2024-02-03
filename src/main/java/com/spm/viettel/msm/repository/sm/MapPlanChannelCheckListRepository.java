package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelCheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapPlanChannelCheckListRepository extends GenericJpaRepository<MapPlanChannelCheckList,Long>, JpaRepository<MapPlanChannelCheckList,Long> {

//    List<MapPlanChannelCheckList> findMapPlanChannelCheckListByChannelTypeIdAndType(Long channelTypeId);
    List<MapPlanChannelCheckList> findMapPlanChannelCheckListByChannelTypeId(Long channelTypeId);
}
