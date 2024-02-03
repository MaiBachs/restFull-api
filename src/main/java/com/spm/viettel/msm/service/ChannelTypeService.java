package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.ChannelTypeDTO;
import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import com.spm.viettel.msm.repository.sm.ChannelTypeRepository;
import com.spm.viettel.msm.repository.sm.entity.AppParamsSM;
import com.spm.viettel.msm.repository.sm.entity.ChannelType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@Transactional
public class ChannelTypeService {
    private final Logger loggerFactory = LoggerFactory.getLogger(ChannelTypeService.class);

    private final ChannelTypeRepository channelTypeRepository;
    private final AppParamService appParamService;
    private final PlanService planService;

    @Autowired
    public ChannelTypeService(ChannelTypeRepository channelTypeRepository, AppParamService appParamService, PlanService planService) {
        this.channelTypeRepository = channelTypeRepository;
        this.appParamService = appParamService;
        this.planService = planService;
    }

    public ChannelWithGroupDTO findChannelByName(String channelName){
        return channelTypeRepository.findChannelTypeByName(channelName);
    }

    public List<ChannelTypeDTO> getListChannelTypeUsingOfccAudit(){
        String[] channelTypeIdUsing = planService.getChannelTypeIdsUsingOfCcAudit();
        List<Long> channelIds = new ArrayList<>();
        channelIds = Arrays.stream(channelTypeIdUsing).map(channelTypeIdStr->{
            return Long.parseLong(channelTypeIdStr);
        }).collect(Collectors.toList());
        List<ChannelTypeDTO> channelTypeDTOS = channelTypeRepository.findByChannelTypeIds(channelIds);
        return channelTypeDTOS;
    }

    public List<ChannelType> getAllChannelType(){
        return channelTypeRepository.findByNameNotContainingAndPerfixObjectCodeIsNotNullAndPerfixObjectCodeNotInOrderByName("Staff", Arrays.asList("BC","VT","BR"));
    }

    public List<ChannelWithGroupDTO> getListChannelCanPlanToDevelop() {
        List<AppParamsSM> params = appParamService.getParamsSMByTypeAndCode("QLKD_MAP_PLAN_CHANNEL_TYPE", "CHANNEL_TYPE_CAN_PLAN_DEVELOP");
        List<ChannelWithGroupDTO> lst = new LinkedList<>();
        for (AppParamsSM p : params) {
            String[] channelTypes = p.getValue().split(";");
            for (String type : channelTypes) {
                String[] channel = type.split(":");
                ChannelWithGroupDTO group = finChannelTypeById(Long.valueOf(StringUtils.trim(channel[1])));
                group.setObjectType(StringUtils.trim(channel[0]));
                lst.add(group);
            }
        }
        return lst;
    }

    public ChannelWithGroupDTO finChannelTypeById(Long id) {
        try {
            ChannelWithGroupDTO lst = channelTypeRepository.findChannelByID(id);
            return lst;
        }catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }
        return null;
    }

    public List<ChannelWithGroupDTO> getListChannelWithGroup() {
        List<ChannelWithGroupDTO> list = channelTypeRepository.getListChannelTypeWithGroup();
        return list;
    }
}
