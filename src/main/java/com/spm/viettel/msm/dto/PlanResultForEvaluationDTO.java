package com.spm.viettel.msm.dto;

import com.spm.viettel.msm.repository.smartphone.entity.ItemConfig;
import com.spm.viettel.msm.repository.smartphone.entity.PlanResult;
import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanResultForEvaluationDTO {
    private PlanResult planResult;
    private ItemConfig itemConfig;

}
