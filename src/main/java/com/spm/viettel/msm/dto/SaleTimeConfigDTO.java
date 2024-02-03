package com.spm.viettel.msm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleTimeConfigDTO {
    private Long id;
    private Long mapSalePolicyId;
    private String timeFrom;
    private String timeTo;
    private Integer status;
    private BigDecimal startTime;
    private BigDecimal endTime;
}
