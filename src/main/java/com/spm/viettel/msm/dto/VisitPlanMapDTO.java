package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitPlanMapDTO {
    private Long id;

    private Long branchId;

    private String branchCode;

    private Long bcId;

    private String bcCode;

    private Long zonalId;

    private String zonalCode;

    private Long pdvId;

    private String pdvCode;

    private String pdvChannelObjectType;

    private Date datePlan;

    private Integer status;

    private Integer isDetail;

    private Integer checkListResultStatus;

    private String checkListResultApproveUser;

    private String checkListResultCommnet;

    private Date createdDate;

    private Date updatedDate;

    private Date visitTime;

    private int functionType;

    private String comment;

    private Integer index;

    private String datePlanText;

    private Long userReviewId;

    private String userReviewName;

    private Float score;

    private Date reviewDate;

    private Long parentId;

    private Long channelTypeId;

    private String channelTypeName;

    private Long jobId;

    private String jobCode;

}