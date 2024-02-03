package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.PlanSaleResultDto;
import com.spm.viettel.msm.repository.sm.entity.MapPlanSale;
import com.spm.viettel.msm.repository.sm.entity.MapPlanSaleResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapPlanSaleResultRepository extends GenericJpaRepository<MapPlanSaleResult, Long> {

    @TemplateQuery
    Page<PlanSaleResultDto> searchResult(@Param("brCode") String brCode
    , @Param("bcCode") String bcCode
    , @Param("ownerCode") String ownerCode
    , @Param("isdn") String isdn
    , @Param("fromDate") String fromDate
    , @Param("toDate") String toDate
    , @Param("map_plan_sale_id") Long map_plan_sale_id
    , PageRequest pageRequest);

    @TemplateQuery
    List<PlanSaleResultDto> searchResult(@Param("brCode") String brCode
    , @Param("bcCode") String bcCode
    , @Param("ownerCode") String ownerCode
    , @Param("isdn") String isdn
    , @Param("fromDate") String fromDate
    , @Param("toDate") String toDate
    , @Param("map_plan_sale_id") Long map_plan_sale_id);

    @TemplateQuery
    List<MapPlanSale> downloadSearchResultDetail(@Param("brCode") String brCode
            , @Param("bcCode") String bcCode
            , @Param("staffCode") String staffCode
            , @Param("status") Integer status
            , @Param("fromDate") String fromDate
            , @Param("toDate") String toDate);
}
