/**
 * @(#)AreaBO.java 8/8/2012 4:56 PM,
 * Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author linhvm
 * @version 1.0
 * @since 8/8/2012 4:56 PM
 */
@Data
@Entity
@Table(name = "map_sale_policy")
public class MapSalePolicy implements java.io.Serializable {

    //Fields
    @Id
    @javax.persistence.SequenceGenerator(
            name = "map_sale_policy_seq",
            sequenceName = "map_sale_policy_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "map_sale_policy_seq")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "content")
    private String content;
    @Column(name = "status")
    private Integer status;
}
