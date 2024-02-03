package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.VisitPlanCategoryResultDTO;
import com.spm.viettel.msm.dto.VisitPlanMapDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEvalByIdResponse {
    private VisitPlanMapDTO resultEvlById;
    private List<VisitPlanCategoryResultDTO> cateResult;
    private VisitPlanMapDTO action_plan;
}
