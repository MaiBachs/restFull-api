package com.spm.viettel.msm.repository.sm.entity;

import com.spm.viettel.msm.utils.NumberUtils;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "MAP_PLAN_CHANNEL_TARGET")
public class MapPlanChannelTarget implements Serializable {
    private static final long serialVersionUID = 1109488548583734821L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "map_plan_channel_target_seq_GEN")
    @javax.persistence.SequenceGenerator(name = "map_plan_channel_target_seq_GEN", sequenceName = "map_plan_channel_target_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "BR_CODE")
    private String brCode;

    @Column(name = "BC_CODE")
    private String bcCode;

    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "STAFF_ID")
    private Long staffId;

    @Column(name = "CHANNEL_TYPE")
    private Integer channelType;

    @Column(name = "CHANNEL_CODE")
    private String channelCode;

    @Column(name = "CHANNEL_ID")
    private Long channelId;

    @Column(name = "TARGET_TYPE")
    private Integer targetType;

    @Column(name = "T1_TARGET")
    private Integer t1Target;

    @Column(name = "T1_PLAN")
    private Integer t1Plan;

    @Column(name = "T1_RESULT")
    private Integer t1Result;

    @Column(name = "T2_TARGET")
    private Integer t2Target;

    @Column(name = "T2_PLAN")
    private Integer t2Plan;

    @Column(name = "T2_RESULT")
    private Integer t2Result;

    @Column(name = "T3_TARGET")
    private Integer t3Target;

    @Column(name = "T3_PLAN")
    private Integer t3Plan;

    @Column(name = "T3_RESULT")
    private Integer t3Result;

    @Column(name = "T4_TARGET")
    private Integer t4Target;

    @Column(name = "T4_PLAN")
    private Integer t4Plan;

    @Column(name = "T4_RESULT")
    private Integer t4Result;

    @Column(name = "T5_TARGET")
    private Integer t5Target;

    @Column(name = "T5_PLAN")
    private Integer t5Plan;

    @Column(name = "T5_RESULT")
    private Integer t5Result;

    @Column(name = "T6_TARGET")
    private Integer t6Target;

    @Column(name = "T6_PLAN")
    private Integer t6Plan;

    @Column(name = "T6_RESULT")
    private Integer t6Result;

    @Column(name = "T7_TARGET")
    private Integer t7Target;

    @Column(name = "T7_PLAN")
    private Integer t7Plan;

    @Column(name = "T7_RESULT")
    private Integer t7Result;

    @Column(name = "T8_TARGET")
    private Integer t8Target;

    @Column(name = "T8_PLAN")
    private Integer t8Plan;

    @Column(name = "T8_RESULT")
    private Integer t8Result;

    @Column(name = "T9_TARGET")
    private Integer t9Target;

    @Column(name = "T9_PLAN")
    private Integer t9Plan;

    @Column(name = "T9_RESULT")
    private Integer t9Result;

    @Column(name = "T10_TARGET")
    private Integer t10Target;

    @Column(name = "T10_PLAN")
    private Integer t10Plan;

    @Column(name = "T10_RESULT")
    private Integer t10Result;

    @Column(name = "T11_TARGET")
    private Integer t11Target;

    @Column(name = "T11_PLAN")
    private Integer t11Plan;

    @Column(name = "T11_RESULT")
    private Integer t11Result;

    @Column(name = "T12_TARGET")
    private Integer t12Target;

    @Column(name = "T12_RESULT")
    private Integer t12Result;

    @Column(name = "T12_PLAN")
    private Integer t12Plan;

    @Column(name = "TOTAL_TARGET")
    private Integer totalTarget;

    @Column(name = "TOTAL_PLAN")
    private Integer totalPlan;

    @Column(name = "TOTAL_RESULT")
    private Integer totalResult;
    @Column(name = "TARGET_LEVEL")
    private Integer targetLevel;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "PLAN_TYPE")
    private Integer planType;

    @Column(name = "PLAN_DATE")
    @Temporal(TemporalType.DATE)
    private Date planDate;

    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "create_user")
    private String createUser;

    @UpdateTimestamp
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "child_status")
    private Integer childStatus;

    @Transient
    private String channelKey;

    @PrePersist
    public void prePersist() {
        createdDate = new Date();
        updatedDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = new Date();
    }

    public void summaryTotalTarget() {
        this.totalTarget = NumberUtils.zeroIfNull(this.t1Target) + NumberUtils.zeroIfNull(this.t2Target) + NumberUtils.zeroIfNull(this.t3Target) +
                NumberUtils.zeroIfNull(this.t4Target) + NumberUtils.zeroIfNull(this.t5Target) + NumberUtils.zeroIfNull(this.t6Target) +
                NumberUtils.zeroIfNull(this.t7Target)  + NumberUtils.zeroIfNull(this.t8Target) +
                NumberUtils.zeroIfNull(this.t9Target) + NumberUtils.zeroIfNull(this.t10Target) + NumberUtils.zeroIfNull(this.t11Target) +
                NumberUtils.zeroIfNull(this.t12Target);
    }
    public void summaryTotalResult() {
        this.totalResult = NumberUtils.zeroIfNull(this.t1Result) + NumberUtils.zeroIfNull(this.t2Result) + NumberUtils.zeroIfNull(this.t3Result) +
                NumberUtils.zeroIfNull(this.t4Result) + NumberUtils.zeroIfNull(this.t5Result) + NumberUtils.zeroIfNull(this.t6Result) +
                NumberUtils.zeroIfNull(this.t7Result) + NumberUtils.zeroIfNull(this.t8Result) +
                NumberUtils.zeroIfNull(this.t9Result) + NumberUtils.zeroIfNull(this.t10Result) + NumberUtils.zeroIfNull(this.t11Result) +
                NumberUtils.zeroIfNull(this.t12Result);
    }

    public void summaryTotalPlan() {
        this.totalPlan = NumberUtils.zeroIfNull(this.t1Plan) + NumberUtils.zeroIfNull(this.t2Plan) + NumberUtils.zeroIfNull(this.t3Plan) +
                NumberUtils.zeroIfNull(this.t4Plan) + NumberUtils.zeroIfNull(this.t5Plan) + NumberUtils.zeroIfNull(this.t6Plan) +
                NumberUtils.zeroIfNull(this.t7Plan) + NumberUtils.zeroIfNull(this.t8Plan) +
                NumberUtils.zeroIfNull(this.t9Plan) + NumberUtils.zeroIfNull(this.t10Plan) + NumberUtils.zeroIfNull(this.t11Plan) +
                NumberUtils.zeroIfNull(this.t12Plan);
    }


}
