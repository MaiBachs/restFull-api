package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelTypeDTO {
    private Long channelTypeId;
    private String name;
    private Long status;
    private String objectType;
    private String perfixObjectCode;

    public ChannelTypeDTO(Long channelTypeId, String name, Long status, String objectType,String perfixObjectCode) {
        this.channelTypeId = channelTypeId;
        this.name = name;
        this.status = status;
        this.objectType = objectType;
        this.perfixObjectCode = perfixObjectCode;
    }

    public ChannelTypeDTO(Long channelTypeId, String name, String objectType) {
        this.channelTypeId = channelTypeId;
        this.name = name;
        this.objectType = objectType;
    }
}
