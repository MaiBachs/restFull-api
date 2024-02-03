package com.spm.viettel.msm.dto.request;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditorConfig {
    private Long branchId;
    private Long auditorId;
    private Long channelTypeId;
    private Long shopChannelId;
    @ExcelCellName(value = "Rama")
    private String brCode;
    @ExcelCellName(value = "Censurar")
    private String auditor;
    @ExcelCellName(value = "Tipo de canal")
    private String channelType;
    @ExcelCellName(value = "Código de canal")
    private String shopChannel;
    private String comment;
}