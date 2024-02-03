/**
 * @(#)ShopBO.java 8/8/2012 4:08 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.spm.viettel.msm.repository.sm.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author linhvm
 * @version 1.0
 * @since 8/8/2012 4:08 PM
 */
@Entity
@Table(name = "SHOP")
public class Shop implements java.io.Serializable {
    //<editor-fold defaultstate="collapsed" desc="Fields">

    private Long shopId;
    private String name;
    private Long parentShopId;
    private String account;
    private String bankName;
    private String address;
    private String tel;
    private String fax;
    private String shopCode;
    private String shopType;
    private String contactName;
    private String contactTitle;
    private String telNumber;
    private String email;
    private String description;
    private String province;
    private String parShopCode;
    private String centerCode;
    private String oldShopCode;
    private String company;
    private String tin;
    private String shop;
    private String provinceCode;
    private String payComm;
    private Date createDate;
    private Long channelTypeId;
    private String discountPolicy;
    private String pricePolicy;
    private Long status;
    private String shopPath;
    private String provinceShopCode;
    private String district;
    private String precinct;
    private Double x;
    private Double y;
    private String channelName;
    private Long staffOwnerId;
    private String staffOwnerName;

    private Float radius;
    private String lastUpdateUser;
    private Date lastUpdateTime;

    //</editor-fold>        
    //<editor-fold defaultstate="collapsed" desc="Constructors">    

    public Shop() {
    }

    public Shop(Long shopId) {
        this.shopId = shopId;
    }

    public Shop(Long shopId, String name, Long parentShopId, String account, String bankName, String address, String tel, String fax, String shopCode, String shopType, String contactName, String contactTitle, String telNumber, String email, String description, String province, String parShopCode, String centerCode, String oldShopCode, String company, String tin, String shop, String provinceCode, String payComm, Date createDate, Long channelTypeId, String discountPolicy, String pricePolicy, Long status, String shopPath, String provinceShopCode, String district, String precinct, Double x, Double y, Long staffOwnerId, String staffOwnerName,Float radius) {
        this.shopId = shopId;
        this.name = name;
        this.parentShopId = parentShopId;
        this.account = account;
        this.bankName = bankName;
        this.address = address;
        this.tel = tel;
        this.fax = fax;
        this.shopCode = shopCode;
        this.shopType = shopType;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.telNumber = telNumber;
        this.email = email;
        this.description = description;
        this.province = province;
        this.parShopCode = parShopCode;
        this.centerCode = centerCode;
        this.oldShopCode = oldShopCode;
        this.company = company;
        this.tin = tin;
        this.shop = shop;
        this.provinceCode = provinceCode;
        this.payComm = payComm;
        this.createDate = createDate;
        this.channelTypeId = channelTypeId;
        this.discountPolicy = discountPolicy;
        this.pricePolicy = pricePolicy;
        this.status = status;
        this.shopPath = shopPath;
        this.provinceShopCode = provinceShopCode;
        this.district = district;
        this.precinct = precinct;
        this.x = x;
        this.y = y;
        this.staffOwnerId = staffOwnerId;
        this.staffOwnerName = staffOwnerName;
        this.radius = radius;
    }
    //</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="Properties">    

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence",
    parameters = {
        @Parameter(name = "sequence", value = "shop_seq")
    })
    @Column(name = "SHOP_ID", unique = true, nullable = false)
    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(final Long shopId) {
        this.shopId = shopId;
    }

    @Column(name = "RADIUS")
    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float raduius) {
        this.radius = raduius;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Column(name = "PARENT_SHOP_ID")
    public Long getParentShopId() {
        return this.parentShopId;
    }

    public void setParentShopId(final Long parentShopId) {
        this.parentShopId = parentShopId;
    }

    @Column(name = "ACCOUNT")
    public String getAccount() {
        return this.account;
    }

    public void setAccount(final String account) {
        this.account = account;
    }

    @Column(name = "BANK_NAME")
    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    @Column(name = "TEL")
    public String getTel() {
        return this.tel;
    }

    public void setTel(final String tel) {
        this.tel = tel;
    }

    @Column(name = "FAX")
    public String getFax() {
        return this.fax;
    }

    public void setFax(final String fax) {
        this.fax = fax;
    }

    @Column(name = "SHOP_CODE", nullable = false)
    public String getShopCode() {
        return this.shopCode;
    }

    public void setShopCode(final String shopCode) {
        this.shopCode = shopCode;
    }

    @Column(name = "SHOP_TYPE")
    public String getShopType() {
        return this.shopType;
    }

    public void setShopType(final String shopType) {
        this.shopType = shopType;
    }

    @Column(name = "CONTACT_NAME")
    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(final String contactName) {
        this.contactName = contactName;
    }

    @Column(name = "CONTACT_TITLE")
    public String getContactTitle() {
        return this.contactTitle;
    }

    public void setContactTitle(final String contactTitle) {
        this.contactTitle = contactTitle;
    }

    @Column(name = "TEL_NUMBER")
    public String getTelNumber() {
        return this.telNumber;
    }

    public void setTelNumber(final String telNumber) {
        this.telNumber = telNumber;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Column(name = "PROVINCE")
    public String getProvince() {
        return this.province;
    }

    public void setProvince(final String province) {
        this.province = province;
    }

    @Column(name = "PAR_SHOP_CODE")
    public String getParShopCode() {
        return this.parShopCode;
    }

    public void setParShopCode(final String parShopCode) {
        this.parShopCode = parShopCode;
    }

    @Column(name = "CENTER_CODE")
    public String getCenterCode() {
        return this.centerCode;
    }

    public void setCenterCode(final String centerCode) {
        this.centerCode = centerCode;
    }

    @Column(name = "OLD_SHOP_CODE")
    public String getOldShopCode() {
        return this.oldShopCode;
    }

    public void setOldShopCode(final String oldShopCode) {
        this.oldShopCode = oldShopCode;
    }

    @Column(name = "COMPANY")
    public String getCompany() {
        return this.company;
    }

    public void setCompany(final String company) {
        this.company = company;
    }

    @Column(name = "TIN")
    public String getTin() {
        return this.tin;
    }

    public void setTin(final String tin) {
        this.tin = tin;
    }

    @Column(name = "SHOP")
    public String getShop() {
        return this.shop;
    }

    public void setShop(final String shop) {
        this.shop = shop;
    }

    @Column(name = "PROVINCE_CODE")
    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceCode(final String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Column(name = "PAY_COMM")
    public String getPayComm() {
        return this.payComm;
    }

    public void setPayComm(final String payComm) {
        this.payComm = payComm;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "CHANNEL_TYPE_ID")
    public Long getChannelTypeId() {
        return this.channelTypeId;
    }

    public void setChannelTypeId(final Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    @Column(name = "DISCOUNT_POLICY")
    public String getDiscountPolicy() {
        return this.discountPolicy;
    }

    public void setDiscountPolicy(final String discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    @Column(name = "PRICE_POLICY")
    public String getPricePolicy() {
        return this.pricePolicy;
    }

    public void setPricePolicy(final String pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return this.status;
    }

    public void setStatus(final Long status) {
        this.status = status;
    }

    @Column(name = "SHOP_PATH")
    public String getShopPath() {
        return this.shopPath;
    }

    public void setShopPath(final String shopPath) {
        this.shopPath = shopPath;
    }

    @Column(name = "PROVINCE_SHOP_CODE")
    public String getProvinceShopCode() {
        return this.provinceShopCode;
    }

    public void setProvinceShopCode(final String provinceShopCode) {
        this.provinceShopCode = provinceShopCode;
    }

    @Column(name = "DISTRICT")
    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    @Column(name = "PRECINCT")
    public String getPrecinct() {
        return this.precinct;
    }

    public void setPrecinct(final String precinct) {
        this.precinct = precinct;
    }

    @Column(name = "X")
    public Double getX() {
        return this.x;
    }

    public void setX(final Double x) {
        this.x = x;
    }

    @Column(name = "Y")
    public Double getY() {
        return this.y;
    }

    public void setY(final Double y) {
        this.y = y;
    }

    @Column(name = "STAFF_OWNER_ID", precision = 10, scale = 0)
    public Long getStaffOwnerId() {
        return this.staffOwnerId;
    }

    public void setStaffOwnerId(Long staffOwnerId) {
        this.staffOwnerId = staffOwnerId;
    }

    @Transient
    public String getStaffOwnerName() {
        return staffOwnerName;
    }

    public void setStaffOwnerName(String staffOwnerName) {
        this.staffOwnerName = staffOwnerName;
    }

    @Transient
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    //</editor-fold>

}