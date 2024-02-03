package com.spm.viettel.msm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class VisitPlanCategoryResultDTO {
    private Long jobId;
    private String name;
    private String code;
    private Long parentId;
    private Long planJobId;
    private Long planId;
    private Integer requireFile;
    private Integer require;
    private String result;
    private String filePath;
    private List<String> listFilePath;
    private String reason;
    private List<VisitPlanCategoryResultDTO> subItems;

    public VisitPlanCategoryResultDTO(Long jobId, String code, String name, Long parentId,Integer requireFile, Long planJobId, Long planId) {
        this.jobId = jobId;
        this.code = code;
        this.name = name;
        this.parentId = parentId;
        this.requireFile = requireFile;
        this.planJobId = planJobId;
        this.planId = planId;
    }

    public VisitPlanCategoryResultDTO(Long jobId, String code, String name, Long parentId, Integer requireFile, Integer require, Long planJobId, String result, String filePath, String reason) {
        this.jobId = jobId;
        this.code = code;
        this.name = name;
        this.parentId = parentId;
        this.requireFile = requireFile;
        this.require = require;
        this.planJobId = planJobId;
        this.result = result;
        this.filePath = filePath;
        this.reason = reason;
    }
}
