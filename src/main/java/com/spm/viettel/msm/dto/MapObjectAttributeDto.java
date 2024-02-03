package com.spm.viettel.msm.dto;

import lombok.Data;

@Data
public class MapObjectAttributeDto {
    private String nameVN;
    private String nameES;
    private String nameEN;
    private String value;
    private Integer type; //1. visited/plan
    private String commentVN;
    private String commentES;
    private String commentEN;
}
