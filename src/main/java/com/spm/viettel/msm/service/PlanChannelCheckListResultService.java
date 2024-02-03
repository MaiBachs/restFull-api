package com.spm.viettel.msm.service;

import com.spm.viettel.msm.repository.sm.MapPlanChannelCheckListResultRepository;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelCheckListResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanChannelCheckListResultService {
    private final Logger loggerFactory = LoggerFactory.getLogger(PlanChannelCheckListResultService.class);

    @Autowired
    private MapPlanChannelCheckListResultRepository mapPlanChannelCheckListResultRepository;

    public List<MapPlanChannelCheckListResult> findCheckListResultByPlanChannelIdAndCheckListId(Long planChannelId, Long checkListId){
        String hql = "FROM MapPlanChannelCheckListResult a WHERE a.mapPlanChannelId = :planChannelId AND a.mapPlanChannelCheckListId = :checkListId ";
        try {
            List<MapPlanChannelCheckListResult> mapPlanChannelCheckLists = mapPlanChannelCheckListResultRepository.findMapPlanChannelCheckListResultByMapPlanChannelIdAndMapPlanChannelCheckListId(planChannelId,checkListId);
            return mapPlanChannelCheckLists;
        }catch (Exception e){
            loggerFactory.error(e.getMessage(), e);
        }
        return null;
    }
}
