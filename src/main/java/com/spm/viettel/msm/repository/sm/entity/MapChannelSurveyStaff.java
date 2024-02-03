package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "MAP_CHANNEL_SURVEY_STAFF")
public class MapChannelSurveyStaff {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_GEN")
    @javax.persistence.SequenceGenerator(
            name = "SEQ_GEN",
            sequenceName = "MAP_CHANNEL_SURVEY_STAFF_SEQ",
            allocationSize = 1
    )
    @Column(name = "SURVEY_ID")
    private Long surveyId;

    @Column(name = "BRANCH_ID")
    private Long branchId;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "BC_ID")
    private Long bcId;

    @Column(name = "BC_CODE")
    private String bcCode;

    @Column(name = "USER_ID")
    private Long userId; // this is the person being judged (voter will vote for user)

    @Column(name = "USER_CODE") // this is the person being judged (voter will vote for user)
    private String userCode;

    @Column(name = "CHANNEL_ID")
    private Long channelId; // this is the voter_Id

    @Column(name = "CHANNEL_CODE")
    private String channelCode; // this is the voter_Code

    @Column(name = "SURVEY_COMMENT")
    private String surveyComment;

    @Column(name = "RESULT_SURVEY")
    private String resultSurvey;

    @Column(name = "START_VOTE_DATE")
    private Date start_vote_date;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "SURVEY_DATE")
    private Date surveyDate;

    @Column(name = "STATUS")
    private Long status;
}
