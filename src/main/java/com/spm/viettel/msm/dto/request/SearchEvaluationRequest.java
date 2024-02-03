package com.spm.viettel.msm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchEvaluationRequest {
    private Long branchId;
    private Long channelTypeId;
    private Long jobId; // is JOB_ID have parent = null
//    private String createdBy;
    private String toDate;
    private String fromDate;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;

    public SearchEvaluationRequest(Long br, Long channelTypeId, Long evaluationType, String fromDate, String toDate, Integer currentPage) {
        this.branchId = br;
        this.channelTypeId = channelTypeId;
        this.jobId = evaluationType;
        this.toDate = toDate;
        this.fromDate = fromDate;
        this.currentPage = currentPage;
    }

    public SearchEvaluationRequest(Long br) {
        this.branchId = br;
    }
}
