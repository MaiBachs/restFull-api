package com.spm.viettel.msm.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class BTS implements Serializable {
    private String cellId;
    private String name;
}
