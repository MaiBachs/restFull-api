package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "PLAN_JOB",schema = "SMARTPHONE")
public class PlanJob {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "PLAN_JOB_SEQ_GEN")
    @SequenceGenerator(
            name = "PLAN_JOB_SEQ_GEN",
            sequenceName = "SMARTPHONE.PLAN_JOB_SEQ",
            allocationSize = 1
    )
    @Column(name = "PLAN_JOB_ID")
    private Long planJobId;

    @Column(name = "PLAN_ID")
    private Long planId;

    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "REQUIRED")
    private Integer required;

    @Column(name = "REQUIRED_FILE")
    private Integer requiredFile;

    @Column(name = "EXPIRED_IN_DAY")
    private Long expiredInDay;

    @Column(name = "IDX")
    private Long idx;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "IS_AF")
    private Long isAf;
}
