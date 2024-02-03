package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.DetailEvaluationManageDTO;
import com.spm.viettel.msm.dto.EvaluationManageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReasonOfVisitPlanMapViewResponse {
    private List<DetailEvaluationManageDTO> detailEvaluations;
    private EvaluationManageDTO actionPlan;
}
