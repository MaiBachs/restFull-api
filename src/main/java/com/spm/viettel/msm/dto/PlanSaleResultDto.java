package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanSaleResultDto {
    private String brCode;
    private String bcCode;
    private String ownerCode;
    private String isdn;
    private String channelCode;
    private String btsActiveCode;
    private String connectType;
    private String serviceType;
    private String activeDate;
    private String caculateDate;
    private Long recharge;
    private String productCode;
}
