package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class MapObjectAttributeRequest {
    private Integer channelObjectType;
    private Long mapObjectId;
    private Integer objectType;
    private Integer channelTypeId;
    private String pvdCode;
    private String fromDate;
    private String toDate;
}
