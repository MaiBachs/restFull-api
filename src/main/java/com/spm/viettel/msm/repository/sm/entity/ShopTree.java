package com.spm.viettel.msm.repository.sm.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author luan
 */
@Data
@Entity
@Table(name = "TBL_SHOP_TREE")
public class ShopTree implements Serializable{
    private static final long serialVersionUID = 9037336532369476225L;

    @Column(name = "ROOT_ID")
    private Long rootId;

    @Column(name = "ROOT_CODE")
    private String rootCode;

    @Column(name = "ROOT_NAME")
    private String rootName;

    @Id
    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "SHOP_CODE")
    private String shopCode;

    @Column(name = "PARENT_SHOP_ID")
    private Long parentShopId;

    @Column(name = "SHOP_NAME")
    private String shopName;

    @Column(name = "SHOP_NAME_TREE")
    private String shopNameTree;

    @Column(name = "SHOP_PATH_TREE")
    private String shopPathTree;

    @Column(name = "SHOP_TYPE")
    private String shopType;

    @Column(name = "SHOP_LEVEL")
    private Integer level;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "SHOP_STATUS")
    private Integer status;

    @Column(name = "SHOP_ORDER")
    private Integer shopOrder;
}
