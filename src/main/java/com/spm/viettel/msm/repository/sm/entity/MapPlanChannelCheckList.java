package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "MAP_PLAN_CHANNEL_CHECK_LIST")
public class MapPlanChannelCheckList implements java.io.Serializable {

    //Fields
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DATA_TYPE")
    private Integer dataType;
    @Column(name = "\"TYPE\"")
    private Integer type;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Transient
    private List<MapPlanChannelCheckListResult> checkListResults;
}
