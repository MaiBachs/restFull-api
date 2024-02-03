package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class VisitPlanWithImageRequest {
    private Long mapObjectId;
    private String pvdCode;
    private Integer channelObjectType;
}
