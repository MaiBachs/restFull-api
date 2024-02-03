package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.MapPlanSaleDto;
import lombok.Data;
import java.util.List;

@Data
public class MapPlanSaleSearchListResponse {
    private List<MapPlanSaleDto> mapPlanSaleDtos;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;
}
