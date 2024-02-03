package com.spm.viettel.msm.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BranchBcDTO implements Serializable {
    private Long bcId;
    private String bcCode;
    private String branchCode;
    private Long branchId;
}
