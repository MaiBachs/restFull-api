package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.ConfigureItemDto;
import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemResultResponse {
    private List<ConfigureItemDto> itemConfigDtoList;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;
}
