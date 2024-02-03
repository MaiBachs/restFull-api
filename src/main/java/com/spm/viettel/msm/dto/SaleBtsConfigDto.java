package com.spm.viettel.msm.dto;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;

@Data
public class SaleBtsConfigDto {
    @ExcelCellName(value = "Código de sucursal", mandatory = false)
    private String brCode;
    @ExcelCellName(value = "BTS sitio", mandatory = false)
    private String btsSite;
    @ExcelCellName(value = "POLÍTICA", mandatory = false)
    private String policyName;
    @ExcelCellName(value = "Fecha de inicio", mandatory = false)
    private String startDate;
    @ExcelCellName(value = "Fecha final", mandatory = false)
    private String endDate;
    @ExcelCellName(value = "Estado", mandatory = false)
    private String status;

    private String comment;
}
