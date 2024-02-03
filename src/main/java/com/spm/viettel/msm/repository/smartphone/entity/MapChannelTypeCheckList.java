package com.spm.viettel.msm.repository.smartphone.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MAP_CHANNEL_TYPE_CHECK_LIST")
public class MapChannelTypeCheckList {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MAP_CHECK_LIST_SEQ_GEN")
    @SequenceGenerator(
            name = "MAP_CHECK_LIST_SEQ_GEN",
            sequenceName = "SMARTPHONE.MAP_CHECK_LIST_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "BR_ID")
    private Long branchId;

    @Column(name = "BR_CODE")
    private String branchCode;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "QUANTITY_PER_MONTH")
    private Long quantityPerMonth;

    @Column(name = "APPROVAL_SCORE")
    private Float approvalScore;

    @Column(name = "DATE_EVALUATION_1")
    private Date dateEvaluation1;

    @Column(name = "DATE_EVALUATION_2")
    private Date dateEvaluation2;

    @Column(name = "DATE_EVALUATION_3")
    private Date dateEvaluation3;

    @Column(name = "DATE_EVALUATION_4")
    private Date dateEvaluation4;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

}