package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDetailsDTO {

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

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}
