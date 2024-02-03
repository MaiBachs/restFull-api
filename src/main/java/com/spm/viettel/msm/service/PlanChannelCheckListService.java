package com.spm.viettel.msm.service;

import com.spm.viettel.msm.exceptions.NotFoundException;
import com.spm.viettel.msm.repository.sm.MapPlanChannelCheckListRepository;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannel;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelCheckList;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelCheckListResult;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanChannelCheckListService {
    private final Logger loggerFactory = LoggerFactory.getLogger(PlanChannelCheckListService.class);
    @Autowired
    private PlanChannelService planChannelService;
    @Autowired
    private MapPlanChannelCheckListRepository mapPlanChannelCheckListRepository;
    @Autowired
    private PlanChannelCheckListResultService planChannelCheckListResultService;

    public List<MapPlanChannelCheckList> getListCheckListOfChannel(Long channelId){
        MapPlanChannel plan = planChannelService.findPlanById(channelId);
        if (plan == null){
            throw new NotFoundException();
        }
        List<MapPlanChannelCheckList> listCheckLists = findCheckListByChannelTypeIdAndType(plan.getChannelTypeId());
        if (CollectionUtils.isNotEmpty(listCheckLists)){
            listCheckLists.forEach(c -> {
                List<MapPlanChannelCheckListResult> list = planChannelCheckListResultService.findCheckListResultByPlanChannelIdAndCheckListId(channelId, c.getId());
                list.forEach(r -> r.extractFileName());
                c.setCheckListResults(planChannelCheckListResultService.findCheckListResultByPlanChannelIdAndCheckListId(channelId, c.getId()));
            });
        }
        return listCheckLists;
    }

    public List<MapPlanChannelCheckList> findCheckListByChannelTypeIdAndType(Long channelTypeId){
        try {
            List<MapPlanChannelCheckList> mapPlanChannelCheckLists = mapPlanChannelCheckListRepository.findMapPlanChannelCheckListByChannelTypeId(channelTypeId);
            return mapPlanChannelCheckLists;
        }catch (Exception e){
            loggerFactory.error(e.getMessage(), e);
        }
        return null;
    }


}
