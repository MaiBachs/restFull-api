package com.spm.viettel.msm.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReasonOfVisitPlanDTO {
    private String jobName3;
    private String jobCode3;
    private String optionOfAnswer;
    private String reasonOfNotOK;
    private List<String> filePaths;
}
