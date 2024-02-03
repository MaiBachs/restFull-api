package com.spm.viettel.msm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDetailsAuditorReq {
    private Long branchId;
    private Long auditorId;
    private Long channelTypeId;
}
