package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class ReasonRequest extends BaseRequest{
    private String name;
    private String code;
    private String note;
    private Long status;
}
