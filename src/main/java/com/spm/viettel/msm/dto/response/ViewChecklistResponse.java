package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.request.JobRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewChecklistResponse {
    private List<JobRequest> searchChecklistRespones;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;
}
