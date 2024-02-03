package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "APP_PARAMS")
public class AppParamsSmartPhone {
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
}
