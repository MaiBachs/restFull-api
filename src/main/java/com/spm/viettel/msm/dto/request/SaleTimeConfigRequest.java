package com.spm.viettel.msm.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleTimeConfigRequest implements Serializable {
    private Long id;
    private String timeFrom;
    private String timeTo;
    private Integer status;
}
