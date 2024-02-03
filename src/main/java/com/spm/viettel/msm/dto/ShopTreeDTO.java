package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShopTreeDTO {
    private Long shopId;
    private String name;
    private String code;
    private Long parentShopId;

    public ShopTreeDTO(Long shopId, String name, String code) {
        this.shopId = shopId;
        this.name = name;
        this.code = code;
    }
}
