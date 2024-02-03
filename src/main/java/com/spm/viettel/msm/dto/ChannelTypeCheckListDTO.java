package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelTypeCheckListDTO {
    private String branchCode;
    private Long branchId;
    private String channelTypeName;
    private Long channelTypeId;
    private String createdBy;
    private Date createdDate;

}