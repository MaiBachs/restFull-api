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
@Table(name = "REASON")
public class Reason {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "REASON_SEQ_GEN")
    @SequenceGenerator(
            name = "REASON_SEQ_GEN",
            sequenceName = "SMARTPHONE.REASON_SEQ",
            allocationSize = 1
    )
    @Column(name = "REASON_ID")
    private Long reasonId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Transient
    private String gravedad;

    @Transient
    private String gravedadCode;

    public Reason(Long reasonId, String code, String name, String note, Long status, Date createdDate, Date lastUpdate) {
        this.reasonId = reasonId;
        this.code = code;
        this.name = name;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
    }
}
