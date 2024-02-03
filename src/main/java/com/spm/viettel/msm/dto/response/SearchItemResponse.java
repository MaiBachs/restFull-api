package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.repository.smartphone.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemResponse {
    private List<Job> evaluationList;
    private List<Job> groupList;
}
