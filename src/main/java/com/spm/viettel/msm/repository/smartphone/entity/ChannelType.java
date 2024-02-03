package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CHANNEL_TYPE")
public class ChannelType {
    @Id
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @Column(name = "IS_VT_UNIT")
    private String isVtUnit;

    @Column(name = "CHECK_COMM")
    private String checkComm;

    @Column(name = "STOCK_TYPE")
    private Long stockType;

    @Column(name = "STOCK_REPORT_TEMPLATE")
    private String stockReportTemplate;

    @Column(name = "TOTAL_DEBIT")
    private Long totalDebit;

    @Column(name = "MAP_STATUS")
    private String mapStatus;

    @Column(name = "GROUP_CHANNEL")
    private String groupChannel;

}
