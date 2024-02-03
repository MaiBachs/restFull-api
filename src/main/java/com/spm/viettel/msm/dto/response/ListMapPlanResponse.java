package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.MapPlanSaleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListMapPlanResponse {
    private List<MapPlanSaleDto> mapPlanSaleDtos;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;
}
