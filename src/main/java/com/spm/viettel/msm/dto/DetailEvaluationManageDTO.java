package com.spm.viettel.msm.dto;

import com.spm.viettel.msm.dto.response.ReasonOfVisitPlanMapResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailEvaluationManageDTO {
    private String staffOfChannel;
    private List<ReasonOfVisitPlanMapResponse> reasonOfVisitPlanMapResponses;
    private String reasonOfStaffNotEvaluated;
}
