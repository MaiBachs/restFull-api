package com.spm.viettel.msm.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MapChannelSurveyStaffDTO {
    private Long surveyId;
    private Long branchId;
    private String branchCode;
    private Long bcId;
    private String bcCode;
    private Long userId;
    private String userCode;
    private Long channelId;
    private String channelCode;
    private String surveyContent;
    private String resultSurvey;
    private Date createdDate;
    private Date surveyDate;
    private Long status;
}
