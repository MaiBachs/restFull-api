package com.spm.viettel.msm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopTreeRequest {
    private Long parentShopId;
    private Integer level;
    private  Long userShopId;
    private  Long staffId;
}
