package com.spm.viettel.msm.repository.sm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "PREFIX_OBJECT_CODE")
    private String perfixObjectCode;

    @Column(name = "TOTAL_DEBIT")
    private Long totalDebit;

//    @Column(name = "MAP_STATUS")
//    private String mapStatus;

//    @Column(name = "GROUP_CHANNEL")
//    private String groupChannel;

    @Column(name = "CODE")
    private String code;
}
