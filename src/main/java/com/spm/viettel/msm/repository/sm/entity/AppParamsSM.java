package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "APP_PARAMS")
public class AppParamsSM {
    @Id
    @Column(name = "TYPE")
    private String type;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;

    @Column(name = "BASE_NAME")
    private String baseName;
}
