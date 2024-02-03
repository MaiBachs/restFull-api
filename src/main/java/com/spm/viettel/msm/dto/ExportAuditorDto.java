package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportAuditorDto {
    private String brCode;
    private Long brId;
    private Long auditorId;
    private String auditor;
    private Long channelTypeId;
    private String channelTypeName;
    private Long shopId;
    private String shopCode;
    private String createdBy;
    private Date createdDate;
}
