package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "PENALTY_GRAVEDAD", schema = "SMARTPHONE")
public class PenaltyGravedad {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "PENALTY_GRAVEDAD_SEQ")
    @SequenceGenerator(
            name = "PENALTY_GRAVEDAD_SEQ",
            sequenceName = "SMARTPHONE.PENALTY_GRAVEDAD_SEQ",
            allocationSize = 1
    )
    @Column(name = "PENALTY_GRAVEDAD_ID")
    private Long penaltyGravedadId;

    @Column(name = "GRAVEDAD")
    private String gravedad;

    @Column(name = "PENALIDAD")
    private String penalidad;

    @Column(name = "PENALTY_ID")
    private Long penaltyId;

    @Column(name = "STATUS")
    private Long status;
}
