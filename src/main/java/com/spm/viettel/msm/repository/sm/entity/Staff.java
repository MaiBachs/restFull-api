/**
 * @author kdvt_dinhvv
 * @since: 15/05/2011
 */

/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.spm.viettel.msm.repository.sm.entity;

import com.spm.viettel.msm.dto.StaffDto;

import javax.persistence.*;
import java.util.Date;

/**
 * Staff generated by hbm2java
 */
@Entity
@Table(name = "STAFF")
@SqlResultSetMapping(
        name = "StaffDtoMapping",
        classes = @ConstructorResult(
                targetClass = StaffDto.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "code"),
                        @ColumnResult(name = "name"),
                        @ColumnResult(name = "channelTypeId"),
                        @ColumnResult(name = "address"),
                        @ColumnResult(name = "x"),
                        @ColumnResult(name = "y"),
                        @ColumnResult(name = "tel"),
                        @ColumnResult(name = "channelObjectType"),
                        @ColumnResult(name = "rank"),
                        @ColumnResult(name = "staffOwnerId"),
                        @ColumnResult(name = "staffOwnerName"),
                        @ColumnResult(name = "staffOwnerCode"),
                        @ColumnResult(name = "staffOwnerTel"),
                        @ColumnResult(name = "isdnAgent"),
                        @ColumnResult(name = "type"),
                        @ColumnResult(name = "objectType"),
                        @ColumnResult(name = "shopCode"),
                        @ColumnResult(name = "allowVisitPlan"),
                        @ColumnResult(name = "totalAcumulate"),
                        // Định nghĩa các cột khác tương tự
                }
        )
)
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Staff implements java.io.Serializable {

    private Long staffId;
    private Long shopId;
    private String staffCode;
    private String name;
    private Long status;
    private Date birthday;
    private String idNo;
    private String idIssuePlace;
    private Date idIssueDate;
    private String tel;
    private Long type;
    private String serial;
    private String isdn;
    private String pin;
    private String address;
    private String province;
    private String staffOwnType;
    private Long staffOwnerId;
    private Long channelTypeId;
    private String pricePolicy;
    private String discountPolicy;
    private String pointOfSale;
    private Long lockStatus;
    private Date lastLockTime;
    private Double x;
    private Double y;
    //tuena add
    private String district;
    private String precinct;
    private String posCode;

    private Float radius;
    private String lastUpdateUser;
    private Date lastUpdateTime;

    @Column(name = "RADIUS")
    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    @Column(name = "DISTRICT")
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Column(name = "PRECINCT")
    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    @Column(name = "LAST_UPDATE_USER")
    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    @Column(name = "LAST_UPDATE_TIME")
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Staff() {
    }

    public Staff(Long staffId, Long shopId, String staffCode, String name, Float radius) {
        this.staffId = staffId;
        this.shopId = shopId;
        this.staffCode = staffCode;
        this.name = name;
        this.radius = radius;
    }

    public Staff(Long staffId, Long shopId, String staffCode, String name, Long status, Date birthday, String idNo, String idIssuePlace, Date idIssueDate, String tel, Long type, String serial, String isdn, String pin, String address, String province, Long channelTypeId, Float radius/*, Long staffLevel, Long staffType, String isdnAgent, Long trackingStatus, Long businessMethod, Long contractMethod*/) {
        this.staffId = staffId;
        this.shopId = shopId;
        this.staffCode = staffCode;
        this.name = name;
        this.status = status;
        this.birthday = birthday;
        this.idNo = idNo;
        this.idIssuePlace = idIssuePlace;
        this.idIssueDate = idIssueDate;
        this.tel = tel;
        this.type = type;
        this.serial = serial;
        this.isdn = isdn;
        this.pin = pin;
        this.address = address;
        this.province = province;
        this.channelTypeId = channelTypeId;
        this.radius = radius;
//        this.staffLevel = staffLevel;
//        this.staffType = staffType;
//        this.isdnAgent = isdnAgent;
//        this.trackingStatus = trackingStatus;
//        this.businessMethod = businessMethod;
//        this.contractMethod = contractMethod;
    }

    public Staff(Long staffId) {
        this.staffId = staffId;
    }

    public Staff(Long staffId, Long shopId, String staffCode, String name, Long status, Date birthday, String idNo, String idIssuePlace, Date idIssueDate, String tel, Long type, String serial, String isdn, String pin, String address, String province, String staffOwnType, Long staffOwnerId, Long channelTypeId, String pricePolicy, String discountPolicy, String pointOfSale, Long lockStatus, Date lastLockTime/*, String mapStatus, Long staffLevel, Long staffType, String isdnAgent, Long trackingStatus, Long businessMethod, Long contractMethod*/) {
        this.staffId = staffId;
        this.shopId = shopId;
        this.staffCode = staffCode;
        this.name = name;
        this.status = status;
        this.birthday = birthday;
        this.idNo = idNo;
        this.idIssuePlace = idIssuePlace;
        this.idIssueDate = idIssueDate;
        this.tel = tel;
        this.type = type;
        this.serial = serial;
        this.isdn = isdn;
        this.pin = pin;
        this.address = address;
        this.province = province;
        this.staffOwnType = staffOwnType;
        this.staffOwnerId = staffOwnerId;
        this.channelTypeId = channelTypeId;
        this.pricePolicy = pricePolicy;
        this.discountPolicy = discountPolicy;
        this.pointOfSale = pointOfSale;
        this.lockStatus = lockStatus;
        this.lastLockTime = lastLockTime;
//        this.mapStatus = mapStatus;
        //this.geometry = geometry;
//        this.staffLevel = staffLevel;
//        this.staffType = staffType;
//        this.isdnAgent = isdnAgent;
//        this.trackingStatus = trackingStatus;
//        this.businessMethod = businessMethod;
//        this.contractMethod = contractMethod;
    }

    @Id
    @Column(name = "STAFF_ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    @Column(name = "SHOP_ID", precision = 10, scale = 0)
    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Column(name = "STAFF_CODE", length = 40)
    public String getStaffCode() {
        return this.staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    @Column(name = "NAME", length = 500)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "STATUS", precision = 1, scale = 0)
    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BIRTHDAY", length = 7)
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "ID_NO", length = 20)
    public String getIdNo() {
        return this.idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    @Column(name = "ID_ISSUE_PLACE", length = 100)
    public String getIdIssuePlace() {
        return this.idIssuePlace;
    }

    public void setIdIssuePlace(String idIssuePlace) {
        this.idIssuePlace = idIssuePlace;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ID_ISSUE_DATE", length = 7)
    public Date getIdIssueDate() {
        return this.idIssueDate;
    }

    public void setIdIssueDate(Date idIssueDate) {
        this.idIssueDate = idIssueDate;
    }

    @Column(name = "TEL")
    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name = "TYPE", precision = 1, scale = 0)
    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Column(name = "SERIAL", length = 20)
    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Column(name = "ISDN", length = 15)
    public String getIsdn() {
        return this.isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    @Column(name = "PIN", length = 10)
    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Column(name = "ADDRESS", length = 500)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "PROVINCE", length = 5)
    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Column(name = "STAFF_OWN_TYPE")
    public String getStaffOwnType() {
        return this.staffOwnType;
    }

    public void setStaffOwnType(String staffOwnType) {
        this.staffOwnType = staffOwnType;
    }

    @Column(name = "STAFF_OWNER_ID", precision = 10, scale = 0)
    public Long getStaffOwnerId() {
        return this.staffOwnerId;
    }

    public void setStaffOwnerId(Long staffOwnerId) {
        this.staffOwnerId = staffOwnerId;
    }

    @Column(name = "CHANNEL_TYPE_ID", precision = 22, scale = 0)
    public Long getChannelTypeId() {
        return this.channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    @Column(name = "PRICE_POLICY", length = 20)
    public String getPricePolicy() {
        return this.pricePolicy;
    }

    public void setPricePolicy(String pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    @Column(name = "DISCOUNT_POLICY", length = 20)
    public String getDiscountPolicy() {
        return this.discountPolicy;
    }

    public void setDiscountPolicy(String discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    @Column(name = "POINT_OF_SALE", length = 30)
    public String getPointOfSale() {
        return this.pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    @Column(name = "LOCK_STATUS", precision = 10, scale = 0)
    public Long getLockStatus() {
        return this.lockStatus;
    }

    public void setLockStatus(Long lockStatus) {
        this.lockStatus = lockStatus;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOCK_TIME", length = 7)
    public Date getLastLockTime() {
        return this.lastLockTime;
    }

    public void setLastLockTime(Date lastLockTime) {
        this.lastLockTime = lastLockTime;
    }

//    @Column(name = "MAP_STATUS", length = 2)
//    public String getMapStatus() {
//        return this.mapStatus;
//    }
//
//    public void setMapStatus(String mapStatus) {
//        this.mapStatus = mapStatus;
//    }
//    @Basic
//    @Type(type = "com.viettel.nims.fcn.common.util.JGeometryType")
//    @Column(name = "GEOMETRY")
//    public JGeometryType getGeometry() {
//        return this.geometry;
//    }
//
//    public void setGeometry(JGeometryType geometry) {
//        this.geometry = geometry;
//    }
//    @Column(name = "STAFF_LEVEL", precision = 5, scale = 0)
//    public Long getStaffLevel() {
//        return this.staffLevel;
//    }
//
//    public void setStaffLevel(Long staffLevel) {
//        this.staffLevel = staffLevel;
//    }
//
//    @Column(name = "STAFF_TYPE", precision = 5, scale = 0)
//    public Long getStaffType() {
//        return this.staffType;
//    }
//
//    public void setStaffType(Long staffType) {
//        this.staffType = staffType;
//    }
//    @Column(name = "ISDN_AGENT", length = 15)
//    public String getIsdnAgent() {
//        return this.isdnAgent;
//    }
//
//    public void setIsdnAgent(String isdnAgent) {
//        this.isdnAgent = isdnAgent;
//    }
//
//    @Column(name = "TRACKING_STATUS", precision = 1, scale = 0)
//    public Long getTrackingStatus() {
//        return this.trackingStatus;
//    }
//
//    public void setTrackingStatus(Long trackingStatus) {
//        this.trackingStatus = trackingStatus;
//    }
    @Column(name = "X")
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Column(name = "Y")
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    
    
//    @Column(name = "BUSINESS_METHOD", precision = 2, scale = 0)
//    public Long getBusinessMethod() {
//        return businessMethod;
//    }
//
//    public void setBusinessMethod(Long businessMethod) {
//        this.businessMethod = businessMethod;
//    }
//
//    @Column(name = "CONTRACT_METHOD", precision = 2, scale = 0)
//    public Long getContracMethod() {
//        return contractMethod;
//    }
//
//    public void setContracMethod(Long contractMethod) {
//        this.contractMethod = contractMethod;
//    }
//
//    @Column(name = "NUM_OF_VISIT")
//    public Long getNumOfVisit() {
//        return numOfVisit;
//    }
//
//    public void setNumOfVisit(Long numOfVisit) {
//        this.numOfVisit = numOfVisit;
//    }
//
//    @Column(name = "IMG_URL", length = 1000)
//    public String getImgUrl() {
//        return imgUrl;
//    }
//
//    public void setImgUrl(String imgUrl) {
//        this.imgUrl = imgUrl;
//    }

    @Column(name = "pos_code", length = 50)
    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }
}