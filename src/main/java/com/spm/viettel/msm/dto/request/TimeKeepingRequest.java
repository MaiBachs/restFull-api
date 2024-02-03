package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class TimeKeepingRequest {
    private String staffCode;
    private Integer objectType;
    private String createdDate;
    private String fromTime;
    private String toTime;
}
