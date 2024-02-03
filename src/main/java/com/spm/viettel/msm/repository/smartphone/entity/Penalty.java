package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "PENALTY", schema = "SMARTPHONE")
public class Penalty {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SMARTPHONE_PENALTY_SEQ_GEN")
    @SequenceGenerator(
            name = "SMARTPHONE_PENALTY_SEQ_GEN",
            sequenceName = "SMARTPHONE.PENALTY_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_TYPE_ID")
    private Long userTypeId;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "EVALUATION_ID")
    private Long evaluationId;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "UPDATED_DATE")
    private Date updateDate;

    @Transient
    private int indexp;

    @Transient
    private PenaltyGravedad penaltyGravedad;

    @Transient
    private String evaluationType;

    @Transient
    private List<PenaltyGravedad> detailData;

    public Penalty(Long id, Long userTypeId, String userType, String createdBy, Date createdDate, Long status, Long evaluationId, String updatedBy, Date updateDate, String evaluationType) {
        this.id = id;
        this.userTypeId = userTypeId;
        this.userType = userType;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.status = status;
        this.evaluationId = evaluationId;
        this.updatedBy = updatedBy;
        this.updateDate = updateDate;
        this.evaluationType = evaluationType;
    }
}
