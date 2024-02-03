package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopDto implements Serializable {
    private Long shopId;
    private String shopCode;
    private String shopName;
    private Long parentShopId;
    private Integer childrenNumber;
    private String staffOwnerName;
    private Long staffOwnerId;
    private String longitude;
    private String latitude;
    private Float radius;
    private String provinceCode;
    private String district;
    private String precinct;
    private String shopType;
    private Long channelTypeId;

    public ShopDto(Long shopId, String shopCode, String shopName, Long parentShopId) {
        this.shopId = shopId;
        this.shopCode = shopCode;
        this.shopName = shopName;
        this.parentShopId = parentShopId;
    }

    public ShopDto(Long shopId, String shopName, String shopCode, Long parentShopId, Long staffOwnerId, String staffOwnerName,String longitude, String latitude, Float radius) {
        this.shopId = shopId;
        this.shopCode = shopCode;
        this.shopName = shopName;
        this.parentShopId = parentShopId;
        this.staffOwnerName = staffOwnerName;
        this.staffOwnerId = staffOwnerId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public ShopDto(String provinceCode, String district, String precinct, String shopType, Long channelTypeId) {
        this.provinceCode = provinceCode;
        this.district = district;
        this.precinct = precinct;
        this.shopType = shopType;
        this.channelTypeId = channelTypeId;
    }
}
