package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JOB")
public class Job {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_GEN")
    @SequenceGenerator(
            name = "SEQ_GEN",
            sequenceName = "JOB_SEQ",
            allocationSize = 1
    )
    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "RESULT_DATA_TYPE")
    private Long resultDataType;

    @Column(name = "CATEGORY")
    private Long category;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "CC_AUDIT")
    private Integer ccAudit;

    public Job(Long jobId, String code, String name) {
        this.jobId = jobId;
        this.code = code;
        this.name = name;
    }
}
