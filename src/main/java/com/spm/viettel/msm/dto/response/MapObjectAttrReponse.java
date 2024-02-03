package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.MapObjectAttributeDto;
import com.spm.viettel.msm.dto.StaffDto;
import com.spm.viettel.msm.dto.VisitPlanBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MapObjectAttrReponse {
    private List<MapObjectAttributeDto> mapObjectAttributes;
    private List<String> mapObjectInfoImages = new ArrayList<>();
    private List<StaffDto> promoterList;
    private List<StaffDto> afList;
    private List<VisitPlanBean> visitPlans;
    private Integer activeNumber;
    private Double anypay;
}
