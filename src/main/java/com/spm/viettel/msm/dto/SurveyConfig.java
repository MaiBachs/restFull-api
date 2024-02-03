package com.spm.viettel.msm.dto;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;

@Data
public class SurveyConfig {
    @ExcelCellName(value = "BR")
    private String brCode;
    @ExcelCellName(value = "BC")
    private String bcCode;
    @ExcelCellName(value = "CÓDIGO DE PERSONAL")
    private String staffCode;
    @ExcelCellName(value = "CÓDIGO DE CANAL")
    private String channelCode;
    @ExcelCellName(value = "FECHA DE INVESTIGACION")
    private String start_vote_date;
    private String comment;
}
