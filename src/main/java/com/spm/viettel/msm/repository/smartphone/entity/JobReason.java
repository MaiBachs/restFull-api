package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "JOB_REASON", schema = "SMARTPHONE")
public class JobReason {
    @Id
    @Column(name = "JOB_REASON_ID")
    private Long jobReasonId;

    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "REASON_ID")
    private Long reasonId;

    @Column(name = "IDX")
    private Long idx;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;
}
