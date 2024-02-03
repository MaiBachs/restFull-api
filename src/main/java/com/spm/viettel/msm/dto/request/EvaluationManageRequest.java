package com.spm.viettel.msm.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EvaluationManageRequest {
    private Long branchId;
    private Long typeChannel;
    private String channelCode;
    private Long channelId;
    private String auditor;
    private Long auditorId;
    private Long evaluation;
    private String toDate;
    private String fromDate;
    private Long statusEvaluation;
    private Long jobId;
    private Long visitPlanId;
    private String comment;
    private String scheduledDate;
    private Integer statusActionPlan;
    private Integer checkRejectOrApprove;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;
}
