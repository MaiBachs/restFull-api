package com.spm.viettel.msm.repository.sm.entity;

import com.poiji.annotation.ExcelCellName;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.NumberUtils;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "MAP_PLAN_SALE_TARGET")
public class MapPlanSaleTarget implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MAP_PLAN_SALE_TARGET_SEQ_GEN")
    @javax.persistence.SequenceGenerator(
            name = "MAP_PLAN_SALE_TARGET_SEQ_GEN",
            sequenceName = "MAP_PLAN_SALE_TARGET_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Transient
    @ExcelCellName("#")
    private int rowIndex;

    @ExcelCellName(value = Constants.BRANCH_CODE, mandatory = false)
    @Column(name = "br_code")
    private String brCode;

    @ExcelCellName(value = Constants.BC_CODE, mandatory = false)
    @Column(name = "bc_code")
    private String bcCode;

    @ExcelCellName(value = Constants.STAFF_CODE, mandatory = false)
    @Column(name = "staff_code")
    private String staffCode;

    @Column(name = "staff_id")
    private Long staffId;

    @ExcelCellName(value = Constants.TARGET_JAN, mandatory = false)
    @Column(name = "t1_target")
    private Integer t1Target;

    @Column(name = "t1_plan")
    private Integer t1Plan;

    @Column(name = "t1_result ")
    private Integer t1Result;

    @ExcelCellName(value = Constants.TARGET_FEB, mandatory = false)
    @Column(name = "t2_target")
    private Integer t2Target;

    @Column(name = "t2_plan")
    private Integer t2Plan;

    @Column(name = "t2_result")
    private Integer t2Result;

    @ExcelCellName(value = Constants.TARGET_MAR, mandatory = false)
    @Column(name = "t3_target")
    private Integer t3Target;

    @Column(name = "t3_plan")
    private Integer t3Plan;

    @Column(name = "t3_result")
    private Integer t3Result;

    @ExcelCellName(value = Constants.TARGET_APR, mandatory = false)
    @Column(name = "t4_target")
    private Integer t4Target;

    @Column(name = "t4_plan")
    private Integer t4Plan;

    @Column(name = "t4_result")
    private Integer t4Result;

    @ExcelCellName(value = Constants.TARGET_MAY, mandatory = false)
    @Column(name = "t5_target")
    private Integer t5Target;

    @Column(name = "t5_plan")
    private Integer t5Plan;

    @Column(name = "t5_result")
    private Integer t5Result;

    @ExcelCellName(value = Constants.TARGET_JUN, mandatory = false)
    @Column(name = "t6_target")
    private Integer t6Target;

    @Column(name = "t6_plan")
    private Integer t6Plan;

    @Column(name = "t6_result")
    private Integer t6Result;

    @ExcelCellName(value = Constants.TARGET_JUL, mandatory = false)
    @Column(name = "t7_target")
    private Integer t7Target;

    @Column(name = "t7_plan")
    private Integer t7Plan;

    @Column(name = "t7_result")
    private Integer t7Result;

    @ExcelCellName(value = Constants.TARGET_AUG, mandatory = false)
    @Column(name = "t8_target")
    private Integer t8Target;

    @Column(name = "t8_plan")
    private Integer t8Plan;

    @Column(name = "t8_result")
    private Integer t8Result;

    @ExcelCellName(value = Constants.TARGET_SEP, mandatory = false)
    @Column(name = "t9_target")
    private Integer t9Target;

    @Column(name = "t9_plan")
    private Integer t9Plan;

    @Column(name = "t9_result")
    private Integer t9Result;

    @ExcelCellName(value = Constants.TARGET_OCT, mandatory = false)
    @Column(name = "t10_target")
    private Integer t10Target;

    @Column(name = "t10_plan")
    private Integer t10Plan;

    @Column(name = "t10_result")
    private Integer t10Result;

    @ExcelCellName(value = Constants.TARGET_NOV, mandatory = false)
    @Column(name = "t11_target")
    private Integer t11Target;

    @Column(name = "t11_plan")
    private Integer t11Plan;

    @Column(name = "t11_result")
    private Integer t11Result;

    @ExcelCellName(value = Constants.TARGET_DEC, mandatory = false)
    @Column(name = "t12_target")
    private Integer t12Target;

    @Column(name = "t12_plan")
    private Integer t12Plan;

    @Column(name = "t12_result")
    private Integer t12Result;

    @ExcelCellName(value = Constants.TARGET, mandatory = false)

    @Column(name = "total_target")
    private Integer totalTarget;

    @Column(name = "total_plan")
    private Integer totalPlan;

    @Column(name = "total_result")
    private Integer totalResult;

    @Column(name = "target_level")
    private Integer targetLevel;

    @Column(name = "status")
    private Integer status;

    @Column(name = "plan_type")
    private Integer planType;

    @Column(name = "PLAN_DATE")
    @Temporal(TemporalType.DATE)
    private Date planDate;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "parent_id")
    private Long parentId;

    @Transient
    private String comment;

    @PrePersist
    public void prePersist() {
        createDate = new Date();
        updateDate = new Date();
        if (this.status == null) {
            this.status = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = new Date();
    }

    public void summaryTotalTarget() {
        this.totalTarget = NumberUtils.zeroIfNull(this.t1Target) + NumberUtils.zeroIfNull(this.t2Target) + NumberUtils.zeroIfNull(this.t3Target) +
                NumberUtils.zeroIfNull(this.t4Target) + NumberUtils.zeroIfNull(this.t5Target) + NumberUtils.zeroIfNull(this.t6Target) +
                NumberUtils.zeroIfNull(this.t7Target)+ NumberUtils.zeroIfNull(this.t8Target) +
                NumberUtils.zeroIfNull(this.t9Target) + NumberUtils.zeroIfNull(this.t10Target) + NumberUtils.zeroIfNull(this.t11Target) +
                NumberUtils.zeroIfNull(this.t12Target);
    }

    public boolean isNoData() {
        try {
            for (int i = 1; i <= 12; i++) {
                String value = BeanUtils.getProperty(this, String.format("t%sTarget", i));
                if (StringUtils.isNotEmpty(value) && Integer.valueOf(value) > 0) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
