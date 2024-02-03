package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class AreaRequest {
    private String province;
    // Neu truyen district thi tuc la muon tim Xa/Phuong
    private String district;
}
