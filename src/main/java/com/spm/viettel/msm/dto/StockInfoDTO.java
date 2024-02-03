package com.spm.viettel.msm.dto;

import lombok.Data;
import java.util.Date;

@Data
public class StockInfoDTO {
    private Long ownerId;
    private String code;
    private String name;
    private Long stockModelId;
    private Long stockTypeId;
    private Date saleDate;
    private Double price;
    private String fromSerial;
    private String toSerial;
    private Integer quantity;
    private String tableName;
    private String stockTypeName;
}
