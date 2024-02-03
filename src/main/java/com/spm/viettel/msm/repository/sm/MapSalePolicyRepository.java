package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.PlanSalePolicyDTO;
import com.spm.viettel.msm.repository.sm.entity.MapSalePolicy;
import com.slyak.spring.jpa.GenericJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapSalePolicyRepository extends GenericJpaRepository<MapSalePolicy, Long>{
    MapSalePolicy findByName(String name);

    @TemplateQuery
    List<PlanSalePolicyDTO> getListSalePolicyWithSaleTime(
            @Param("name") String name,
            @Param("status") Integer status,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    @Query("SELECT t FROM MapSalePolicy t WHERE  upper(t.name) = upper(:name) AND t.status <> -1")
    MapSalePolicy findByNameAndNotDeleted(@Param("name") String name);

    @Query("SELECT t FROM MapSalePolicy t WHERE t.id <> 5 AND t.status <> -1")
    List<MapSalePolicy> getListPolicy();

    @Query(value = "SELECT m FROM MapSalePolicy m WHERE m.status <> -1 AND m.startDate <= SYSDATE AND (m.endDate >= SYSDATE OR m.endDate IS NULL) ORDER BY m.name")
    List<MapSalePolicy> getListSalePolicy();
}
