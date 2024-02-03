package com.spm.viettel.msm.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SalePolicyRequest extends BaseRequest {
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private Integer status;
    private String content;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;
    private List<SaleTimeConfigRequest> addSaleTimeConfigs;
    private List<SaleTimeConfigRequest> editSaleTimeConfigs;
    private List<SaleTimeConfigRequest> deleteSaleTimeConfigs;
}
