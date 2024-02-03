package com.spm.viettel.msm.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VisitPlanResultDTO implements Serializable {
    private Long id;
    private Long planJobId;
    private String result;
    private String filePath;
    private String file;
}
