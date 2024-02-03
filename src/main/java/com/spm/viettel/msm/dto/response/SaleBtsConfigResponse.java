package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.repository.sm.entity.MapSaleBtsConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleBtsConfigResponse {
    private MapSaleBtsConfig saleBtsConfigs;
    private String errorMessage;
}
