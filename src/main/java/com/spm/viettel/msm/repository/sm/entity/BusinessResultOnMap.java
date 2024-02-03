package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "BUSINESS_RESULT_ON_MAP")
public class BusinessResultOnMap {
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "CHANNEL_ID")
    private Long channelId;

    @Column(name = "CHANNEL_CODE")
    private String channelCode;

    @Column(name = "CHANNEL_TYPE")
    private String channelType;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "BRANCH_ID")
    private Long branchId;

    @Column(name = "CENTER_CODE")
    private String centerCode;

    @Column(name = "CENTER_ID")
    private Long centerId;

    @Column(name = "SHOP_CODE")
    private String shopCode;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "OWNER_ID")
    private Long ownerId;

    @Column(name = "OWNER_CODE")
    private String ownerCode;

    @Column(name = "CALCULATE_MONTH")
    @Temporal(TemporalType.TIMESTAMP)
    private Date calculateMonth;

    @Column(name = "PRE_ACTIVE_LAST_DAY")
    private Long preActiveLastDay;

    @Column(name = "PRE_ACTIVE_NO_RC_LAST_DAY")
    private Long preActiveNoRcLastDay;

    @Column(name = "PRE_ACTIVE_RC_LAST_DAY")
    private Long preActiveRcLastDay;

    @Column(name = "PRE_LAST_DATE_ACTIVE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date preLastDateActive;

    @Column(name = "PRE_ACUM_N")
    private Long preAcumN;

    @Column(name = "PRE_ACUM_N_NO_RC")
    private Long preAcumNNoRc;

    @Column(name = "PRE_ACUM_N_RC")
    private Long preAcumNRc;

    @Column(name = "PRE_ACUM_N_1")
    private Long preAcumN1;

    @Column(name = "PRE_ACUM_N_1_NO_RC")
    private Long preAcumN1NoRc;

    @Column(name = "PRE_ACUM_N_1_RC")
    private Long preAcumN1Rc;

    @Column(name = "POST_ACTIVE_LAST_DAY")
    private Long postActiveLastDay;

    @Column(name = "POST_LAST_DATE_ACTIVE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postLastDateActive;

    @Column(name = "POST_ACUM_N")
    private Long postAcumN;

    @Column(name = "POST_ACUM_N_1")
    private Long postAcumN1;

    @Column(name = "TOTAL_ACTIVE")
    private Long totalActive;

    @Column(name = "TOTAL_ACUM_N")
    private Long totalAcumN;

    @Column(name = "TOTAL_ACUM_N_1")
    private Long totalAcumN1;

    @Column(name = "CALCULATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date calculateDate;

    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
}
