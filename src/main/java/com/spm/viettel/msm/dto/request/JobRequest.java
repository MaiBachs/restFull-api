package com.spm.viettel.msm.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobRequest {
    private Long jobId;
    private Long planId;
    private Long parentId;
    private String name;
    private String code;
    private Long resultDataType;
    private Long status;
    private String parentName;
    private String parentCode;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;


    public JobRequest(Long jobId, String code,String name, Long parentId, Long resultDataType) {
        this.jobId = jobId;
        this.code = code;
        this.name = name;
        this.parentId = parentId;
        this.resultDataType = resultDataType;
    }

    public JobRequest(Long jobId, Long parentId, String name, String code, String parentName, String parentCode) {
        this.jobId = jobId;
        this.parentId = parentId;
        this.name = name;
        this.code = code;
        this.parentName = parentName;
        this.parentCode = parentCode;
    }
}
