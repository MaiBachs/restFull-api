package com.spm.viettel.msm.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class SearchPenaltyRequest {
    private Long evaluationId;
    private Long userTypeId;
    private String fromDate;
    private String toDate;
    private boolean isPaging = true;
    private int pageSize = 10;
    private int currentPage;
}
