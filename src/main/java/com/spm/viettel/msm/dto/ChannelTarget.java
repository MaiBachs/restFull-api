package com.spm.viettel.msm.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChannelTarget implements Serializable {
    private Long targetId;
    private String channelCode;
    private String channelTypeName4Sort;
    private String channelKey;
    private Long channelId;
    private Long parentId;
    private Integer targetType;
    private AmountByMonth target;
    private AmountByMonth result;
    private AmountByMonth plan;
}
