package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class PlanSaleBtsConfigSearchRequest {
    private Long id;
    private Long salePolicyId;
    private String brCode;
    private String btsCode;
    private String startDate;
    private String endDate;
    private Integer status;
    private String createdDate;
    private String updateDate;
    private String createdUser;
    private String updateUser;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;
}
