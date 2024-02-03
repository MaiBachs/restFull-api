package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.EvaluationManageDTO;
import com.spm.viettel.msm.dto.VisitPlanBean;
import com.spm.viettel.msm.dto.VisitPlanMapDTO;
import com.spm.viettel.msm.repository.sm.entity.VisitPlanMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * @author edward
 */
public interface VisitPlanMapRepository extends GenericJpaRepository<VisitPlanMap, Long> {
    @TemplateQuery
    List<VisitPlanBean> searchVisitPlan(@RequestParam("branchId") Long branchId,
                                        @RequestParam("bcId") Long bcId,
                                        @RequestParam("staffId") Long staffId,
                                        @RequestParam("posCode") String posCode,
                                        @RequestParam("fromDate") String fromDate,
                                        @RequestParam("toDate") String toDate,
                                        @RequestParam("no_sql_param_isVisited") Long isVisited);


    @TemplateQuery
    List<VisitPlanMap> getVisitPlanByPlanSource(
            @Param("zonalStaffCode") String zonalStaffCode,
            @Param("pdvStaffCode") String pdvStaffCode,
            @Param("datePlan") String datePlan
    );

    @TemplateQuery
    List<VisitPlanBean> getVisitPlanByUser(
            @Param("branchId") Long branchId,
            @Param("bcId") Long bcId,
            @Param("staffId") Long staffId,
            @Param("posCode") String posCode,
            @Param("fromDate") Date from,
            @Param("toDate") Date to,
            @Param("no_sql_param_isVisited") Long isVisited);

    @TemplateQuery
    Long getVisitCount(
            @Param("branchId") Long branchId,
            @Param("bcId") Long bcId,
            @Param("staffId") Long staffId,
            @Param("posCode") String posCode,
            @Param("fromDate") String from,
            @Param("toDate") String to,
            @Param("no_sql_param_isVisited") Long isVisited
    );

    @TemplateQuery
    List<VisitPlanBean> searchVisitPlanVisited(@Param("exportMonth") String exportMonth,
                                        @Param("branchId") Long branchId,
                                        @Param("bcId") Long bcId,
                                        @Param("staffId") Long staffId,
                                        @Param("posCode") String posCode,
                                        @Param("no_sql_param_isVisited") Long isVisited);

    @TemplateQuery
    List<VisitPlanBean> searchPDVNotHaveVisitPlan(@Param("shopId") Long shopId,
                                               @Param("channelHavePlan") List<String> channelHavePlan);

    @TemplateQuery
    List<VisitPlanBean> SearchVisitChannelSummary(@Param("exportMonth") String exportMonth,
                                                  @Param("branchId") Long branchId,
                                                  @Param("bcId") Long bcId,
                                                  @Param("shopId") Long shopId);

    @TemplateQuery
    List<EvaluationManageDTO> searchEvaluationManage(@Param("branchId") Long branchId,
                                                     @Param("typeChannel") Long typeChannel,
                                                     @Param("channelCode") String channelCode,
                                                     @Param("auditor") String auditor,
                                                     @Param("evaluation") Long evaluation,
                                                     @Param("statusEvaluation") Long statusEvaluation,
                                                     @Param("toDate") String toDate,
                                                     @Param("fromDate") String fromDate);


    @TemplateQuery
    List<EvaluationManageDTO> getVisitPlanByIdAndCheckActionPlan(@RequestParam("id") Long id,
                                              @RequestParam("actionPlan") Long actionPlan);

    @TemplateQuery
    List<EvaluationManageDTO> getVisitPlanById(@RequestParam("id") Long id);

    @TemplateQuery
    List<VisitPlanBean> searchLast10VisitPlan(@Param("serverMode") String serverMode, @Param("channelId") Long channelId,@Param("channelCode") String channelCode,@Param("objectType") Integer objectType);
}
