package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.AppParamsDTO;
import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import com.spm.viettel.msm.repository.sm.ChannelTypeRepository;
import com.spm.viettel.msm.repository.sm.ParamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class ChannelWithGroupService {

    @Autowired
    private ParamsRepository paramsRepository;

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired ChannelTypeService channelTypeService;

    public  List<ChannelWithGroupDTO> getListChannelWithGroup() {
        return channelTypeRepository.getListChannelTypeWithGroup();
    }

    public List<ChannelWithGroupDTO> getListChannelCanPlanToDevelop() {
        List<ChannelWithGroupDTO> lst = channelTypeService.getListChannelCanPlanToDevelop();
        return lst;
    }

    public List<String> getListChannelAllowPlanVisit() {
        List<String> lst = paramsRepository.getListChannelAllowPlanVisit();
        return lst;
    }

    public ChannelWithGroupDTO finChannelTypeById(Long id) {
//        String sql = "SELECT CHANNEL_TYPE_ID AS channelTypeId, NAME AS channelName, CODE AS channelCode  FROM CHANNEL_TYPE WHERE CHANNEL_TYPE_ID =" + id;
        ChannelWithGroupDTO channelWithGroupDTO = channelTypeRepository.findChannelTypeById(id);
        if (channelWithGroupDTO != null){
            return channelWithGroupDTO;
        }
        return null;
    }
}
