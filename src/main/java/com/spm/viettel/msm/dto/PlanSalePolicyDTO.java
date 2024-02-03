package com.spm.viettel.msm.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PlanSalePolicyDTO {
    private Long id;
    private String name;
    private String content;
    private Integer status;
    private String saleTime;
    private Date startDate;
    private Date endDate;
}
