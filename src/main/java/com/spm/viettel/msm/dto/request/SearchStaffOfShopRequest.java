package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class SearchStaffOfShopRequest {
    private Long shopId;
    private String userType;
}
