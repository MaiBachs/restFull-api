package com.spm.viettel.msm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data

public class MapPlanChannelDevelop implements Serializable {
    private Long id;
    private Long brId;
    private String brCode;
    private Long bcId;
    private String bcCode;
    private Long staffId;
    private String staffCode;
    private String channelCodeDeployed;
    private Long mapPlanChannelApproveId;
    private Long channelTypeId;
    private String channelTypeName;
    private Integer amount;
    private Double lats;
    private Double longs;
    private Integer status;
    private Integer typeTime;
    private String fromDate;
    private String toDate;
    private String createdDate;
    private String createdUser;
    private String statusConverted;
    private String channelComment;
    private Integer result;

    public void convertStatus() {
        switch (status) {
            case 0:
                statusConverted = "Crear nuevo";
                break;
            case 1:
                statusConverted = "Aprobado por BC";
                break;
            case 2:
                statusConverted = "BR esta aprobado";
                break;
            case 3:
                statusConverted = "Aprobado por ventas (aceptado)";
                break;
            case 4:
                statusConverted = "Proponer";
                break;
            case 5:
                statusConverted = "Listo";
                break;
            case 6:
                statusConverted = "Se acepta la auditoria";
                break;
            case 7:
                statusConverted = "Aceptar exito";
                break;
            case -1:
                statusConverted = "Cancelar";
                break;
            case -2:
                statusConverted = "Reparar";
                break;
            case -3:
                statusConverted = "Auditoria rechazada";
                break;
            case -4:
                statusConverted = "No aceptable";
                break;
        }
    }
}

