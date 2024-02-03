package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class GetChannelCodeDto {
    private Long shopId;
    private Long ChannelTypeId;
    private Long ownerId;
    private Integer objectType;
}
