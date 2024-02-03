package com.spm.viettel.msm.dto;

import lombok.Data;

@Data
public class MapChannelCheckListDTO {
    private Long id;
    private Long branchId;
    private String branchCode;
    private Long channelTypeId;
    private String channelTypeName;
    private Long jobId;
    private String jobCode;
    private Long quantityPerMonth;
    private Float approvalScore;
    private String dateEvaluation1;
    private String dateEvaluation2;
    private String dateEvaluation3;
    private String dateEvaluation4;
    private String createdBy;
    private String createdDate;
    private String updatedBy;
    private String updatedDate;
}
