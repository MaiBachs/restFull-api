/**
 * @(#)AreaBO.java 8/8/2012 4:56 PM,
 * Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.spm.viettel.msm.repository.sm.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author linhvm
 * @version 1.0
 * @since 8/8/2012 4:56 PM
 */
@Data
@Entity
@Table(name = "AREA")
public class Area {

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence",
            parameters = {
                    @Parameter(name = "sequence", value = "")
            })

    //Fields
    @Column(name = "TYPE")
    private String type;

    @Column(name = "AREA_CODE", unique = true, nullable = false)
    private String areaCode;

    @Column(name = "PARENT_CODE")
    private String parentCode;

    @Column(name = "CEN_CODE")
    private String cenCode;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "PRECINCT")
    private String precinct;

    @Column(name = "STREET_BLOCK")
    private String streetBlock;

    @Column(name = "STREET")
    private String street;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;

    @Column(name = "ORDER_NO", nullable = false)
    private Long orderNo;

    @Column(name = "PSTN_CODE")
    private String pstnCode;

    @Column(name = "PROVINCE_REFEFENCE")
    private String provinceRefefence;


}
