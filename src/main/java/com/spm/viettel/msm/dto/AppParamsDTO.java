package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AppParamsDTO {
    private String type;
    private String code;
    private String name;
    private String value;

    public AppParamsDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
