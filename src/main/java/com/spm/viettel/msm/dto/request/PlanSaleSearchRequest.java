package com.spm.viettel.msm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanSaleSearchRequest {
    private Long targetId;
    private String brCode;
    private String bcCode;
    private String staffCode;
    private Long staffId;
    private String channelCode;
    private Long channelTypeId;
    private Long mapSalePolicyId;
    private Long mapPlanSaleId;
    private String planDate;
    private String isdn;
    private String fromDate;
    private String toDate;
    private Integer planType;
    private Integer targetLevel;
    private Integer status;
    private boolean isPaging = true;
    private int pageSize = 10;
    private int currentPage;
}
