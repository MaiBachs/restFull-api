package com.spm.viettel.msm.repository.sm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportStaff{
    private Long pvdId;
    private Integer indexNumber;
    private String branchCode;
    private Long branchId;
    private String bcCode;
    private Long bcId;
    private String zonalCode;
    private String pdvCode;
    private String channelObjectType;
    private String planDate;
    protected Double x;
    protected Double y;

}
