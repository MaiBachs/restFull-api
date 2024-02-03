package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import org.javers.core.metamodel.annotation.DiffIgnore;

@Data
@Entity
@Table(name = "MAP_SALE_BTS_CONFIG")
public class MapSaleBtsConfig implements java.io.Serializable{
    //Fields
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MAP_SALE_BTS_CONFIG_SEQ_GEN")
    @javax.persistence.SequenceGenerator(
            name = "MAP_SALE_BTS_CONFIG_SEQ_GEN",
            sequenceName = "MAP_SALE_BTS_CONFIG_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;
    @Column(name = "BTS_CODE")
    private String btsCode;
    @Column(name = "BR_CODE")
    private String brCode;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "status")
    private Integer status;
    @Column(name = "map_sale_policy_id")
    private Long salePolicyId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    @DiffIgnore
    @Column (name ="UPDATE_DATE")
    private Date updateDate;
    @Column (name = "CREATED_USER")
    private String createdUser;
    @DiffIgnore
    @Column (name = "UPDATE_USER")
    private String updateUser;


    @Transient
    private String sStartDate;
    @Transient
    private String sEndDate;
    @Transient
    private String salePolicyName;
    @Transient
    private String statusString;
    @Transient
    private String sCreatedDate;
    @Transient
    private String sUpdateDate;
}
