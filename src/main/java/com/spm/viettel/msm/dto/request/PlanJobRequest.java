package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class PlanJobRequest {
    private Long planJobId;
    private Long planId;
    private Long jobId;
    private Long parentIdOfJob;
    private String jobName;
    private String jobCode;
    private Integer required;
    private Integer requiredFile;
    private Long expiredInDay;
    private Long idx;
    private Long status;
    private Long fileType;
//    private boolean isPaging = true;
//    private int pageSize = 3;
//    private Integer currentPage;

    public PlanJobRequest(Long planJobId, Long planId, Long jobId,Long parentIdOfJob, String jobName, String jobCode,
                          Integer required, Integer requiredFile, Long expiredInDay, Long idx, Long status, Long fileType) {
        this.planJobId = planJobId;
        this.planId = planId;
        this.jobId = jobId;
        this.parentIdOfJob = parentIdOfJob;
        this.jobName = jobName;
        this.jobCode = jobCode;
        this.required = required;
        this.requiredFile = requiredFile;
        this.expiredInDay = expiredInDay;
        this.idx = idx;
        this.status = status;
        this.fileType = fileType;
    }
}
