package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.MapPlanChannelDevelop;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannel;

import java.util.List;

public interface MapPlanChannelRepository extends GenericJpaRepository<MapPlanChannel,Long> {
    @TemplateQuery
    List<MapPlanChannelDevelop> SearchMapPlanDevelop(String brCode, String bcCode, String staffCode, Long channelTypeId, String fromDate, String toDate, Integer status);
}
