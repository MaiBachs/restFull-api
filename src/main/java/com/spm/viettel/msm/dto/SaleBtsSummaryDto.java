package com.spm.viettel.msm.dto;

import lombok.Data;

@Data
public class SaleBtsSummaryDto {
    private String brCode;
    private String btsCode;
    private Long mapSalePolicyId;
    private String mapSalePolicyName;
    private Long planCount;
    private Long saleResult;
}
