package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "map_plan_channel")
@Data
public class MapPlanChannel implements java.io.Serializable {

    //Fields
    @Id
    @javax.persistence.SequenceGenerator(
            name = "map_plan_channel_seq",
            sequenceName = "map_plan_channel_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "map_plan_channel_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "BR_ID")
    private Long brId;

    @Column(name = "BR_CODE")
    private String brCode;

    @Column(name = "BC_ID")
    private Long bcId;

    @Column(name = "BC_CODE")
    private String bcCode;

    @Column(name = "STAFF_ID")
    private Long staffId;

    @Column(name = "STAFF_CODE")
    private String staffCode;

    @Column(name = "channel_code")
    private String channelCode;

    @Column(name = "channel_code_deployed")
    private String channelCodeDeployed;

    @Column(name = "channel_type_id")
    private Long channelTypeId;

    @Column(name = "lats")
    private Double lats;

    @Column(name = "longs")
    private Double longs;

    @Column(name = "address")
    private String address;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "status")
    private Integer status; // =1 được duyệt, =0 tạo mới, = -1 huỷ, =2 là từ chối, =3 đã sửa

    @Column(name = "type_time")
    private Integer typeTime; // 1 =  Năm,2  = Quý, 3= Tháng, 4 = Tuần - Khi chọn theo tuần thì start date - end date chỉ trong 1 tuần.....

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "map_plan_channel_approve_id")
    private Long mapPlanChannelApproveId;

    @Column(name = "created_user")
    private String createdUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_datetime")
    private Date updatedDatetime;
    @Column(name = "channel_comment")
    private String channelComment;
    @Column(name = "approved_user")
    private String approvedUser;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approved_datetime")
    private Date approvedDatetime;

    @Transient
    private String comment;
    @Transient
    private String fromDateString;
    @Transient
    private String toDateString;
    @Transient
    private String channelTypeCode;
    @Transient
    private Integer index;

    public String getFromDateString() {
        return fromDateString;
    }

    public String getToDateString() {
        return toDateString;
    }

    @PrePersist
    public void prePersist() {
        createdDatetime = new Date();
        updatedDatetime = new Date();
        if (this.status == null){
            this.status = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedDatetime = new Date();
    }


}