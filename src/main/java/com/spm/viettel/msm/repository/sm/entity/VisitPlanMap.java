/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 *
 * @author luan
 */
@Entity
@Table(name = "VISIT_PLAN_MAP")
@Data
public class VisitPlanMap {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_GEN")
    @javax.persistence.SequenceGenerator(
            name = "SEQ_GEN",
            sequenceName = "VISIT_PLAN_MAP_SEQ",
            allocationSize = 1
    )
    @Column(name = "VISIT_PLAN_ID")
    private Long id;

    @Column(name = "BRANCH_ID")
    private Long branchId;
    @Column(name = "BRANCH_CODE")
    private String branchCode;
    @Column(name = "BC_ID")
    private Long bcId;

    @Column(name = "BC_CODE")
    private String bcCode;

    @Column(name = "CHANNEL_FROM_ID")
    private Long zonalId;

    @Column(name = "CHANNEL_FROM_CODE")
    private String zonalCode;

    @Column(name = "CHANNEL_TO_ID")
    private Long pdvId;

    @Column(name = "CHANNEL_TO_CODE")
    private String pdvCode;

    @Column(name = "OBJECT_TYPE")
    private String pdvChannelObjectType;

    @Column(name = "DATE_PLAN")
    @Temporal(TemporalType.DATE)
    private Date datePlan;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "IS_DETAIL")
    private Integer isDetail;

    @Column(name = "CHECKLIST_RESULT_STATUS")
    private Integer checkListResultStatus;

    @Column(name = "CHECKLIST_RESULT_APPROVE_USER")
    private String checkListResultApproveUser;

    @Column(name = "CHECKLIST_RESULT_COMMENT")
    private String checkListResultComment;

    @Column(name = "CHANNEL_TO_CHECKLIST_STATUS")
    private Integer channelToChecklistStatus;

    @Column(name = "FUNCTION_TYPE")
    private Long functionType;

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "VISIT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitTime;

    @Column(name = "SCORE")
    private Long score;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "USER_REVIEW_NAME")
    private String userReviewName;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "IS_ACTION_PLAN")
    private Long isActionPlan;

    @Column(name = "ACTION_PLAN_COMMENT")
    private String actionPlanComment;

    @Transient
    private String comment;
    @Transient
    private Integer index;

    @Transient
    private String datePlanText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getZonalId() {
        return zonalId;
    }

    public void setZonalId(Long zonalId) {
        this.zonalId = zonalId;
    }

    public String getZonalCode() {
        return zonalCode;
    }

    public void setZonalCode(String zonalCode) {
        this.zonalCode = zonalCode;
    }

    public Date getDatePlan() {
        return datePlan;
    }

    public void setDatePlan(Date datePlan) {
        this.datePlan = datePlan;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getPdvId() {
        return pdvId;
    }

    public void setPdvId(Long pdvId) {
        this.pdvId = pdvId;
    }

    public String getPdvCode() {
        return pdvCode;
    }

    public void setPdvCode(String pdvCode) {
        this.pdvCode = pdvCode;
    }

    public String getPdvChannelObjectType() {
        return pdvChannelObjectType;
    }

    public void setPdvChannelObjectType(String pdvChannelObjectType) {
        this.pdvChannelObjectType = pdvChannelObjectType;
    }

    public String getDatePlanText() {
        return datePlanText;
    }

    public void setDatePlanText(String datePlanText) {
        this.datePlanText = datePlanText;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        if (StringUtils.isNotEmpty(branchCode)){
            this.branchCode = branchCode.trim();
        }
    }

    public String getBcCode() {
        return bcCode;
    }

    public void setBcCode(String bcCode) {
        this.bcCode = bcCode;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getBcId() {
        return bcId;
    }

    public void setBcId(Long bcId) {
        this.bcId = bcId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDetail() {
        return isDetail;
    }

    public void setDetail(Integer detail) {
        isDetail = detail;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public String getCheckListResultApproveUser() {
        return checkListResultApproveUser;
    }

    public void setCheckListResultApproveUser(String checkListResultApproveUser) {
        this.checkListResultApproveUser = checkListResultApproveUser;
    }

    public Integer getCheckListResultStatus() {
        return checkListResultStatus;
    }

    public void setCheckListResultStatus(Integer checkListResultStatus) {
        this.checkListResultStatus = checkListResultStatus;
    }

    public String getCheckListResultComment() {
        return checkListResultComment;
    }

    public void setCheckListResultComment(String checkListResultComment) {
        this.checkListResultComment = checkListResultComment;
    }

    public Integer getChannelToChecklistStatus() {
        return channelToChecklistStatus;
    }

    public void setChannelToChecklistStatus(Integer channelToChecklistStatus) {
        this.channelToChecklistStatus = channelToChecklistStatus;
    }

    public Integer getIsDetail() {
        return isDetail;
    }

    public void setIsDetail(Integer isDetail) {
        this.isDetail = isDetail;
    }

    public Long getFunctionType() {
        return functionType;
    }

    public void setFunctionType(Long functionType) {
        this.functionType = functionType;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getUserReviewName() {
        return userReviewName;
    }

    public void setUserReviewName(String userReviewName) {
        this.userReviewName = userReviewName;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getIsActionPlan() {
        return isActionPlan;
    }

    public void setIsActionPlan(Long isActionPlan) {
        this.isActionPlan = isActionPlan;
    }

    @Override
    public boolean equals(Object obj) {
        VisitPlanMap v = (VisitPlanMap) obj;
        return this.zonalCode.equalsIgnoreCase(v.getZonalCode())
                && this.pdvCode.equalsIgnoreCase(v.getPdvCode())
                && this.datePlanText.compareTo(v.datePlanText) == 0;
    }

    @PrePersist
    public void prePersist() {
        createdDate = new Date();
        updatedDate = new Date();
        if (this.status == null){
            this.status = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = new Date();
    }
}
