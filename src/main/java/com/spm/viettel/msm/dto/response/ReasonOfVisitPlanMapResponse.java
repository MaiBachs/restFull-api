package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.ReasonOfVisitPlanDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReasonOfVisitPlanMapResponse {
    private String jobName2;
    private String jobCode2;
    private List<ReasonOfVisitPlanDTO> reasonOfVisitPlanMapResponses;
}
