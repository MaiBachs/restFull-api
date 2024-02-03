package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanSaleTargetDto {
    private Long id;
    private Long parentId;
    private String brCode;
    private String bcCode;
    private String staffCode;
    private Long staffId;
    private Integer totalTarget;
    private Integer totalResult;
    private Integer totalPlan;
    private String createDate;
    private String createUser;
    private Integer planType;
    private Integer targetLevel;
    private Integer t1Target;
    private Integer t1Result;
    private Integer t2Target;
    private Integer t2Result;
    private Integer t3Target;
    private Integer t3Result;
    private Integer t4Target;
    private Integer t4Result;
    private Integer t5Target;
    private Integer t5Result;
    private Integer t6Target;
    private Integer t6Result;
    private Integer t7Target;
    private Integer t7Result;
    private Integer t8Target;
    private Integer t8Result;
    private Integer t9Target;
    private Integer t9Result;
    private Integer t10Target;
    private Integer t10Result;
    private Integer t11Target;
    private Integer t11Result;
    private Integer t12Target;
    private Integer t12Result;
    private Integer t1Plan;
    private Integer t2Plan;
    private Integer t3Plan;
    private Integer t4Plan;
    private Integer t5Plan;
    private Integer t6Plan;
    private Integer t7Plan;
    private Integer t8Plan;
    private Integer t9Plan;
    private Integer t10Plan;
    private Integer t11Plan;
    private Integer t12Plan;
    private Integer status;
}
