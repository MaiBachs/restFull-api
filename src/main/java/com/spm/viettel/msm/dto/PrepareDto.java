package com.spm.viettel.msm.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class PrepareDto implements Serializable {
    String typeActive;
    Long total;
}
