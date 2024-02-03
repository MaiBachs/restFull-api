/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spm.viettel.msm.dto;

import lombok.Data;

/**
 *
 * @author Boong
 */

@Data
public class ReportStaffDTO {

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
