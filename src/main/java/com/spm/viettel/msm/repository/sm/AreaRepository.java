package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.AreaDto;
import com.spm.viettel.msm.dto.AreaWithListShop;
import com.spm.viettel.msm.dto.AreaWithShopDto;
import com.spm.viettel.msm.repository.sm.entity.Area;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AreaRepository extends GenericJpaRepository<Area, Long> {
    @TemplateQuery
    List<AreaWithShopDto> getProvinceWithListBranchId(@Param("userShopId") Long userShopId);

    @TemplateQuery
    List<AreaWithListShop> getAllDistricts(@Param("province") String province);

    @TemplateQuery
    List<AreaWithListShop> getListPrecinct(@Param("province") String province,
                                  @Param("district") String district);
}
