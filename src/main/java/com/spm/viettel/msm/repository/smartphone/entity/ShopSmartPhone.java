package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "SHOP")
public class ShopSmartPhone {
    @Id
    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PARENT_SHOP_ID")
    private Long parentShopId;

    @Column(name = "ACCOUNT")
    private String account;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "SHOP_CODE")
    private String shopCode;

    @Column(name = "SHOP_TYPE")
    private String shopType;

    @Column(name = "CONTACT_NAME")
    private String contactName;


    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "PAR_SHOP_CODE")
    private String parShopCode;

    @Column(name = "CENTER_CODE")
    private String centerCode;

    @Column(name = "OLD_SHOP_CODE")
    private String oldShopCode;

    @Column(name = "COMPANY")
    private String company;

    @Column(name = "TIN")
    private String tin;

    @Column(name = "PROVINCE_CODE")
    private String provinceCode;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "STATUS")
    private Long status;

}
