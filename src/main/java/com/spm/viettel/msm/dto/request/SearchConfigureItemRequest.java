package com.spm.viettel.msm.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(NON_NULL)
public class SearchConfigureItemRequest{
    private Long evaluationId;
    private Long channelTypeId;
    private Long groupId;
    private String fromDate;
    private String toDate;
    private boolean isPaging = true;
    private int pageSize = 10;
    private int currentPage;
}
