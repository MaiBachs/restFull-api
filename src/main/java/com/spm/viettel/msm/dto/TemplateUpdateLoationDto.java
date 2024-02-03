package com.spm.viettel.msm.dto;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class TemplateUpdateLoationDto {

    private Long channelCodeId;
    @ExcelCellName(value = "Código de canal")
    private String channelCode;
    @ExcelCellName(value = "Longitud")
    private Double longitude;
    @ExcelCellName(value = "Latitud")
    private Double latitude;
    @ExcelCellName(value = "Radio de la circunferencia")
    private Float radius;
    private String comment;
    private String objectType;

    public String getFriendlyChannelCode(){
        if (StringUtils.isEmpty(this.channelCode)){
            return "";
        }else {
             return this.channelCode.trim().toUpperCase();
        }
    }

}
