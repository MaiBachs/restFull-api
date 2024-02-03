package com.spm.viettel.msm.dto;

import lombok.Data;

@Data
public class MapPlanSaleDto {
    private Long id;
    private String brCode;
    private String bcCode;
    private String staffCode;
    private Long staffId;
    private String channelCode;
    private String bts;
    private String channelTypeName;
    private Long channelTypeId;
    private Double lats;
    private Double longs;
    private Integer status;
    private String startDate;
    private String endDate;
    private Long mapSalePolicyId;
    private Integer amount;
    private String saleComment;
    private String policyName;
    private String timeSale;
    private String placeSale;
    private String pr;
    private Integer saleResult;
}
