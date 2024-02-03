package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class SearchChannelCodeRequest {
    private String code;
    private Long shopId;
    private Long ownerId;
    private Long channelTypeId;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;
    private int objectType;
}
