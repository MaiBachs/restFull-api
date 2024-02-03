package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.StaffDto;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelCheckList;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelTarget;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlanChannelResponse {
    private List<PlanChannelTargetResponse> targetList;
    private List<MapPlanChannelTarget> currentTarget = new ArrayList<>();
    private MapPlanChannelSearchListResponse channelPlans;
    private List<StaffDto> pointNears;
    private List<MapPlanChannelCheckList> channelCheckListResults;
}
