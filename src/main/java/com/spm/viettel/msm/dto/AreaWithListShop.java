package com.spm.viettel.msm.dto;

import lombok.Data;

import java.util.List;

@Data
public class AreaWithListShop {

    private String name;
    private String code;
    private String province;
    private String district;
    private String precinct;
    private String streetBlock;
    private List<Long> listShopId;

}
