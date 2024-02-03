package com.spm.viettel.msm.dto.request;

import com.spm.viettel.msm.dto.MapChannelCheckListDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MapCheckListRequest {
    private List<MapChannelCheckListDTO> channelTypeCheckListBeans;
}