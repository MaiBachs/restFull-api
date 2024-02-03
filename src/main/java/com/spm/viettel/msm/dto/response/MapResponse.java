package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.StaffDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MapResponse {
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
    /**
     * quyết định lấy Staff: 2, Shop: 1, lay tat ca la:0
     */
    private Integer type; //
    private boolean btsSaleCampaign = false;
    private Long btsSalePolicyId;
    private String btsSaleFrom;
    private String btsSaleTo;

    private List<StaffDto> staffs = new ArrayList<>();
    private List<StaffDto> shops = new ArrayList<>();
    private List<String> listChannelCodeAllowPlans = new ArrayList<>();
}
