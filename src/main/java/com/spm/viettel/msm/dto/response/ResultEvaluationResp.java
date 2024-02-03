package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.ChannelTypeCheckListDTO;
import com.spm.viettel.msm.dto.ChannelTypeDTO;
import com.spm.viettel.msm.repository.smartphone.entity.Job;
import com.spm.viettel.msm.repository.smartphone.entity.MapChannelTypeCheckList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultEvaluationResp {
    private List<MapChannelTypeCheckList> evaluations;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;
}
