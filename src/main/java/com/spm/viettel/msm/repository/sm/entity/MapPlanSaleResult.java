package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "MAP_PLAN_SALE_RESULT")
public class MapPlanSaleResult {
    @Id
    private Long id;

    @Column(name = "BR_ID")
    private Long brId;

    @Column(name = "BR_CODE")
    private String brCode;

    @Column(name = "BC_ID")
    private Long bcId;

    @Column(name = "BC_CODE")
    private String bcCode;

    @Column(name = "OWNER_ID")
    private Long ownerId;

    @Column(name = "OWNER_CODE")
    private String ownerCode;

    @Column(name = "MAP_PLAN_SALE_ID")
    private Long mapPlanSaleId;

    @Column(name = "CHANNEL_CODE")
    private String channelCode;

    @Column(name = "BTS_ACTIVE_CODE")
    private String btsActiveCode;

    @Column(name = "CONNECT_TYPE")
    private String connectType;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "ACTIVE_DATE")
    private Date activeDate;

    @Column(name = "CACULATE_DATE")
    private Date caculateDate;

    @Column(name = "RECHARGE")
    private Long recharge;

    @Column(name = "PRODUCT_CODE")
    private String productCode;

    @Column(name = "ISDN")
    private String isdn;

    @Column(name = "CHANNEL_ID")
    private Long channelId;

    @Column(name = "PLAN_DATE")
    private Date planDate;

    @Column(name = "STAFF_ACTIVE_CODE")
    private String staffActiveCode;

    @Column(name = "STAFF_ACTIVE_ID")
    private Long staffActiveId;
}
