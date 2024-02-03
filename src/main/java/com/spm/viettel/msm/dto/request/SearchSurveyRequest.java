package com.spm.viettel.msm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchSurveyRequest {
    private Long branchId;
    private Long bcId;
    private Long userId;
    private String channelCode;
    private Long status;
    private String toDate;
    private String fromDate;
}
