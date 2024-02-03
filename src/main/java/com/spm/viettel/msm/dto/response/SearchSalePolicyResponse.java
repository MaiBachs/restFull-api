package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.PlanSalePolicyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchSalePolicyResponse {
    private List<PlanSalePolicyDTO> planSalePolicyDTOS;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;
}
