package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class MapSearchRequest {
    private Long shopId;
    private Long staffId;
    private String province;
    private String district;
    private String precinct;
    private String fromDate;
    private String toDate;
    /**
     * //Null is all, 1: Checked In, 2: Planed, 3: No plan
     */
    private Integer status;
    private String channelCode;
    private String ranksValue;
    private String userType;
    private Boolean anypay;
    private Boolean activeNumber;
    private Long btsRegisterFrom;
    private Long btsRegisterTo;
    private String btsCreateFrom;
    private String btsCreateTo;
    private Boolean btsSaleCampaign = false;

    /**
     * quyết định lấy Staff: 2, Shop: 1, lay tat ca la:0
     */
    private Integer type; //
    private Long btsSalePolicyId;
    private String btsSaleTo;
    private String btsSaleFrom;
}
