package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.ShopTreeDTO;
import com.spm.viettel.msm.repository.sm.entity.ShopTree;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopTreeRepository extends GenericJpaRepository<ShopTree, Long> {
    @TemplateQuery
    List<ShopTreeDTO> getListShopTree(@Param("parentShopId") Long parentShopId
                                    , @Param("level") int level
                                    , @Param("userShopId") Long userShopId
                                    , @Param("shopId") Long shopId);

    ShopTree findShopTreeByLevelAndShopId(Integer level, Long shopId);
}
