package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.dto.ShopDto;
import com.spm.viettel.msm.repository.smartphone.entity.ShopSmartPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopSmartPhoneRepository extends JpaRepository<ShopSmartPhone,Long> {
    @Query("SELECT new com.spm.viettel.msm.dto.ShopDto(a.shopId, a.shopCode, a.name, a.parentShopId) FROM ShopSmartPhone a WHERE a.shopCode = :shopCode and a.channelTypeId = :channelTypeId  AND a.parentShopId IN (SELECT b.shopId FROM ShopSmartPhone b WHERE b.parentShopId = :parentId AND b.channelTypeId = 2 AND b.status = 1)" )
    ShopDto getChannelShopOfBranch(@Param("parentId") Long shopId,
                                   @Param("shopCode") String shopCode,
                                   @Param("channelTypeId") Long channelTypeId);

    @Query("SELECT new com.spm.viettel.msm.dto.ShopDto(a.shopId, a.shopCode, a.name, a.parentShopId) FROM ShopSmartPhone a WHERE a.channelTypeId = :channelTypeId  AND a.parentShopId IN (SELECT b.shopId FROM ShopSmartPhone b WHERE b.parentShopId = :parentId AND b.channelTypeId = 2 AND b.status = 1)" )
    List<ShopDto> getChannelcodeInChannelTypeIdByBranch(@Param("parentId") Long shopId,
                                   @Param("channelTypeId") Long channelTypeId);

    @Query("SELECT new com.spm.viettel.msm.dto.ShopDto(a.shopId, a.shopCode, a.name, a.parentShopId)  FROM ShopSmartPhone a JOIN MapAuditorCheckList  m ON a.shopId = m.shopId")
    List<ShopDto> findShopChannelOfAudit();

}
