package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "MAP_MKT_SIZE")
public class MktSize implements java.io.Serializable {

    //Fields
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "\"COMMENT\"")
    private String comment;

}
