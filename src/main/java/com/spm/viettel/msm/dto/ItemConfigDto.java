package com.spm.viettel.msm.dto;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;

@Data
public class ItemConfigDto {
    @ExcelCellName(value = "Tipo de canal", mandatory = false)
    private String channelType;
    @ExcelCellName(value = "Evaluación", mandatory = false)
    private String evaluation;
    @ExcelCellName(value = "Grupo", mandatory = false)
    private String group;
    @ExcelCellName(value = "Artículo", mandatory = false)
    private String item;
    @ExcelCellName(value = "Peso(%)", mandatory = false)
    private String percent;
    @ExcelCellName(value = "OK", mandatory = false)
    private String ok;
    @ExcelCellName(value = "NOK", mandatory = false)
    private String nok;
    @ExcelCellName(value = "NA", mandatory = false)
    private String na;
    @ExcelCellName(value = "Validación", mandatory = false)
    private String validation;
    @ExcelCellName(value = "Motivo No", mandatory = false)
    private String reasonNok;
    @ExcelCellName(value = "Papá tumba", mandatory = false)
    private String gravedad;

    private String comment;
    private boolean pass = false;
    private Long idc;
}
