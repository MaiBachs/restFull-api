package com.spm.viettel.msm.dto.request;

import com.spm.viettel.msm.repository.sm.entity.MktSize;
import com.spm.viettel.msm.repository.sm.entity.MktUnitType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MktUnittypeResponse {
    private List<MktUnitType> unitTypes = new ArrayList<>();
    private List<MktSize> sizes = new ArrayList<>();
}
