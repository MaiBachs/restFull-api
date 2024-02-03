package com.spm.viettel.msm.dto;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;

import javax.persistence.Transient;

@Data
public class PenaltyDto {
    @ExcelCellName(value = "Tipo de evaluación", mandatory = false)
    private String typeEvaluation;
    @ExcelCellName(value = "Usuaria", mandatory = false)
    private String userType;
    @ExcelCellName(value = "Gravedad", mandatory = false)
    private String gravedad;
    @ExcelCellName(value = "Papá tumba", mandatory = false)
    private String penalidad;

    private Integer indexp;
    private boolean pass = false;
    private String comment;
}
