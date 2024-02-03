package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.MapPlanSaleDto;
import com.spm.viettel.msm.repository.sm.entity.MapPlanSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapPlanSaleRepository extends GenericJpaRepository<MapPlanSale, Long> {
    @TemplateQuery
    Page<MapPlanSaleDto> mapSearchPlan(@Param("brCode") String brCode
            , @Param("bcCode") String bcCode
            , @Param("staffCode") String staffCode
            , @Param("channelCode") String channelCode
            , @Param("status") Integer status
            , @Param("fromDate") String fromDate
            , @Param("toDate") String toDate
            , PageRequest pageRequest);
}
