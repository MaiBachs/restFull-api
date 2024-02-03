package com.spm.viettel.msm.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class SearchVisitPlanRequest{
    private Long branchId;
    private Long bcId;
    private Long staffId;
    private String posCode;
    private Long isVisited;
    private String fromDate;
    private String toDate;
    private String exportMonth;
    private Long shopId;
    private List<Long> visitPlanId;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;
}