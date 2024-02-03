package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditorCheckListDTO {
    private Long id;
    private String brCode;
    private Long brId;
    private Long auditorId;
    private String auditor;
    private Long channelTypeId;
    private String channelTypeName;
    private Long shopChannelId;
    private String shopChannel;
    private String createdBy;
    private Date createdDate;
}