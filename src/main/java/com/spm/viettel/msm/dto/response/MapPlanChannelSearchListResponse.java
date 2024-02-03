package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.MapPlanChannelDevelop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapPlanChannelSearchListResponse {
    private List<MapPlanChannelDevelop> channelDevelops;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;

}