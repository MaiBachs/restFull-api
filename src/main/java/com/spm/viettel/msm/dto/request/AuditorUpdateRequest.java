package com.spm.viettel.msm.dto.request;

import lombok.Data;

@Data
public class AuditorUpdateRequest {
    private Long auditorId;
    private Long branchId;
    private Long channelTypeId;
    private Long channelCodeId;
}
