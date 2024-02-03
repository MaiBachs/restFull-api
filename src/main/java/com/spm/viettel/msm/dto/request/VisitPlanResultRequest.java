package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class VisitPlanResultRequest {
    private Long visitPlanId;
    private int checkListResultStatus;
    private String checkListResultComment;
    private String channelCode;
    private Long planId;
    private String filePath;
    private Long jobId;
    private String checkListResultApproveUser;
}
