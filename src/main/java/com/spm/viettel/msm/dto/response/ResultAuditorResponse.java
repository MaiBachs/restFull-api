package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.AuditorCheckListDTO;
import com.spm.viettel.msm.dto.ChannelTypeCheckListDTO;
import com.spm.viettel.msm.repository.smartphone.entity.MapAuditorCheckList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultAuditorResponse {
    private List<AuditorCheckListDTO> auditorCheckLists;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;
}
