package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChannelWithGroupDTO {
    private Long channelTypeId;
    private String channelName;
    private String channelCode;
    private String channelGroupName;
    private Integer type;
    private String objectType;
}
