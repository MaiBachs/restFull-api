package com.spm.viettel.msm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailEvaluationManageResponse {
    private List<ReasonOfVisitPlanMapViewResponse> DetailEvaluationManageAndActionPlan;
}
