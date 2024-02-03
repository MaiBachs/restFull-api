package com.spm.viettel.msm.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SaleResultDto implements Serializable {
    private String serviceType;
    private Date lastUpdated;
    private String month;
    private Long count;
    private Long today;
    private Long samplePeriod;
    private Long lastMonth;
}
