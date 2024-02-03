package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.repository.sm.entity.MapSaleBtsConfig;
import com.spm.viettel.msm.repository.sm.entity.MapSalePolicy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MapSaleBtsConfigRepository extends GenericJpaRepository<MapSaleBtsConfig, Long> {
    @TemplateQuery
    List<MapSaleBtsConfigDTO> searchListBtsConfig(
            @Param("brCode") String brCode,
            @Param("salePolicyId") Long salePolicyId,
            @Param("btsCode") String btsCode,
            @Param("status") Integer status,
            @Param("createdDate") String createdDate,
            @Param("updateDate") String updateDate,
            @Param("createdUser") String createdUser,
            @Param("updateUser") String updateUser,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    @TemplateQuery
    List<BTS> findListBtsByCode(@Param("btsCode") String btsCode, @Param("brCode") String brCode);
    @TemplateQuery
    MapSaleBtsConfig findByBrCodeAndBtsCodeAndSalePolicyId(@Param("brCode") String brCode, @Param("btsCode") String btsCode, @Param("salePolicyId") Long salePolicyId);
    @TemplateQuery
    BigDecimal hasSaleConfigAvailable(@Param("brCode") String brCode, @Param("btsCode") String btsCode);

    @Query("SELECT msp FROM MapSalePolicy msp WHERE lower(msp.name) = lower('Bts normal') AND msp.status = 1")
    MapSalePolicy getBtsNormal();

    @TemplateQuery
    List<StaffDto> getListBts(@Param("shopId") Object shopId,@Param("staffId") Object staffId,@Param("province") Object province,@Param("district") Object district,@Param("precinct") Object precinct,@Param("channelCode") Object channelCode,@Param("btsRegisterFrom") Object btsRegisterFrom,@Param("btsRegisterTo") Object btsRegisterTo,@Param("btsCreateFromDate") Object btsCreateFromDate,@Param("btsCreateToDate") Object btsCreateToDate);

    @TemplateQuery
    List<SaleBtsSummaryDto> getListSaleBtsSummary();

}
