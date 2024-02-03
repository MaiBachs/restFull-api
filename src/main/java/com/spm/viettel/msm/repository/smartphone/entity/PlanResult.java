package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "PLAN_RESULT",schema = "SMARTPHONE")
public class PlanResult implements Serializable {

    @Id
    @SequenceGenerator(
            name = "PLAN_RESULT_SEQ_GEN",
            sequenceName = "SMARTPHONE.PLAN_RESULT_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "PLAN_RESULT_SEQ_GEN")
    @Column(name = "PLAN_RESULT_ID")
    private Long planResultId;

    @Column(name = "VISIT_PLAN_ID")
    private Long visitPlanId;

    @Column(name = "PLAN_JOB_ID")
    private Long planJobId;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "REASON_ID")
    private Long reasonId;

    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "LONGITUDE")
    private Long longitude;

    @Column(name = "LATITUDE")
    private Long latitude;

    @Column(name = "OBJECT_TYPE")
    private Long objectType;

    @Column(name = "OBJECT_ID")
    private Long objectId;

    @Column(name = "ITEM_CONFIG_ID")
    private Long itemConfigId;

}
