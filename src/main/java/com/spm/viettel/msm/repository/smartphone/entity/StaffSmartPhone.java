package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "STAFF")
public class StaffSmartPhone {

    @Id
    @Column(name = "STAFF_ID")
    private Long staffId;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "STAFF_CODE")
    private String staffCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "BIRTHDAY")
    private Date birthday;

    @Column(name = "ID_NO")
    private String idNo;

    @Column(name = "ID_ISSUE_PLACE")
    private String idIssuePlace;

    @Column(name = "ID_ISSUE_DATE")
    private Date idIssueDate;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "TYPE")
    private Long type;

    @Column(name = "SERIAL")
    private String serial;

    @Column(name = "ISDN")
    private String isdn;

    @Column(name = "PIN")
    private String pin;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "STAFF_OWN_TYPE")
    private String staffOwnType;

    @Column(name = "STAFF_OWNER_ID")
    private Long staffOwnerId;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "PRICE_POLICY")
    private String pricePolicy;

    @Column(name = "DISCOUNT_POLICY")
    private String discountPolicy;

    @Column(name = "LOCK_STATUS")
    private Long lockStatus;

    @Column(name = "LAST_LOCK_TIME")
    private Date lastLockTime;

    @Column(name = "MAP_STATUS")
    private String mapStatus;

    @Column(name = "STAFF_LEVEL")
    private Long staffLevel;

    @Column(name = "STAFF_TYPE")
    private Long staffType;

    @Column(name = "ISDN_AGENT")
    private String isdnAgent;

    @Column(name = "X")
    private Long x;

    @Column(name = "Y")
    private Long y;

    @Column(name = "BOARD_TYPE")
    private String boardType;

}
