package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.SaleTimeConfigDTO;
import com.spm.viettel.msm.repository.sm.entity.MapSaleTimeConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapSaleTimeConfigRepository extends GenericJpaRepository<MapSaleTimeConfig, Long> {

    @TemplateQuery
    List<SaleTimeConfigDTO> getListSaleTimeConfigBySalePolicyId(@Param("mapSalePolicyId") Long mapSalePolicyId);

    List<MapSaleTimeConfig> findByMapSalePolicyId(Long mapSalePolicyId);

    @Query("SELECT t FROM MapSaleTimeConfig t WHERE t.id = :id AND t.status <> -1")
    MapSaleTimeConfig findByIdAndNotDeleted(@Param("id") Long id);
}
