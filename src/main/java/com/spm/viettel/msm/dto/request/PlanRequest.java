package com.spm.viettel.msm.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PlanRequest {
    private String planName;
    private Long objectType;
    private Long objectLevel;
    private String objectLevelName;
    private Long frequency;
    private Long frequencyUnit;
    private Long channelTypeId;
    private String channelTypeName;
    private Long planId;
    private Long status;
    private Date lastUpdate;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;


}
