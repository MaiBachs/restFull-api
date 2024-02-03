package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@Entity
@Table(name = "MAP_PLAN_SALE")
public class MapPlanSale implements Serializable{

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MAP_PLAN_SALE_SEQ")
    @javax.persistence.SequenceGenerator(
            name = "MAP_PLAN_SALE_SEQ",
            sequenceName = "MAP_PLAN_SALE_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "MAP_PLAN_SALE_TARGET_ID")
    private Long mapPlanSaleTargetID;

    @Column(name = "BR_ID")
    private Long brId;

    @Column(name = "BR_CODE")
    private String brCode;

    @Column(name = "BC_ID")
    private Long bcId;

    @Column(name = "BC_CODE")
    private String bcCode;

    @Column(name = "STAFF_ID")
    private Long staffId;

    @Column(name = "STAFF_CODE")
    private String staffCode;

    @Column(name = "CHANNEL_CODE")
    private String channelCode;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "LATS")
    private Long lats;

    @Column(name = "LONGS")
    private Long longs;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "STAR_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "CREATED_USER")
    private String createdUser;

    @Column(name = "CREATED_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDatetime;

    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    @Column(name = "MAP_SALE_POLICY_ID")
    private Long mapSalePolicyID;

    @Column(name = "SALE_COMMENT")
    private String saleComment;
}
