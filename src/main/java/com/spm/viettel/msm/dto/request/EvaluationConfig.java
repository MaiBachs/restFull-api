package com.spm.viettel.msm.dto.request;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;

@Data
public class EvaluationConfig {
    private Long id;

    private Long branchId;
    @ExcelCellName(value = "BR")
    private String branchCode;

    private Long channelTypeId;
    @ExcelCellName(value = "Tipo de canal")
    private String channelTypeName;

    private Long jobId;
    @ExcelCellName(value = "Tipo de evaluación")
    private String jobName;

    @ExcelCellName(value = "Cantidad por mes")
    private Long quantity_per_month;
    @ExcelCellName(value = "Puntuación de aprobación")
    private Float approval_score;
    @ExcelCellName(value = "Fecha evaluación 1")
    private String date_evaluation_1;
    @ExcelCellName(value = "Fecha evaluación 2")
    private String date_evaluation_2;
    @ExcelCellName(value = "Fecha evaluación 3")
    private String date_evaluation_3;
    @ExcelCellName(value = "Fecha evaluación 4")
    private String date_evaluation_4;
    private String comment;
    private Boolean status;
}