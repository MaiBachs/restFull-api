package com.spm.viettel.msm.dto.request;

import com.spm.viettel.msm.dto.enums.ActionChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanChannelSearchRequest {
    private ActionChannelType action;
    private Long id;
    private Long targetId;
    private String brCode;
    private String bcCode;
    private String staffCode;
    private String planDate;
    private Integer planType;
    private Integer targetLevel;
    private Long channelId;
    private Long channelTypeId;
    private Integer status;
    private List<Long> parentIds;
    private String fromDate;
    private String toDate;
    private String comment;
    private Double x;
    private Double y;
    private boolean isPaging = true;
    private int pageSize = 10;
    private int currentPage;
}
