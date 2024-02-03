package com.spm.viettel.msm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchAuditorRequest {
    private Long branchId;
    private Long ChannelTypeId;
    private Long auditorId;
    private Long shopChannelId;
    private String fromDate;
    private String toDate;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;

    public SearchAuditorRequest(Long br, Long channelTypeId, Long auditor_id,Long shopChannelId, String fromDate, String toDate, Integer currentPage) {
        this.branchId = br;
        ChannelTypeId = channelTypeId;
        this.auditorId = auditor_id;
        this.shopChannelId = shopChannelId;
        this.toDate = toDate;
        this.fromDate = fromDate;
        this.currentPage = currentPage;
    }
}
