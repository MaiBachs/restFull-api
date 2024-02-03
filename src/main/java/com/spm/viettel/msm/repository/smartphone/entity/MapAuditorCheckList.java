package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MAP_AUDITOR_CHECK_LIST")
public class MapAuditorCheckList {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MAP_AUDITOR_SEQ_GEN")
    @SequenceGenerator(
            name = "MAP_AUDITOR_SEQ_GEN",
            sequenceName = "SMARTPHONE.MAP_AUDITOR_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "BR_ID")
    private Long branchId;

    @Column(name = "BR_CODE")
    private String branchCode;

    @Column(name = "AUDITOR_ID") // STAFF
    private Long auditorId;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

}