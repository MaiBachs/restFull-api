package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "map_sale_time_config")
public class MapSaleTimeConfig implements java.io.Serializable {

    @Id
    @javax.persistence.SequenceGenerator(name = "map_sale_time_config_seq"
            , sequenceName = "map_sale_time_config_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "map_sale_time_config_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "MAP_SALE_POLICY_ID")
    private Long mapSalePolicyId;

    @Column(name = "TIME_FROM")
    private String timeFrom;

    @Column(name = "TIME_TO")
    private String timeTo;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "START_TIME")
    private BigDecimal startTime;

    @Column(name = "END_TIME")
    private BigDecimal endTime;

}
