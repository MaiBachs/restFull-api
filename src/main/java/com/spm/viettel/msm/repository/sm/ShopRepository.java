package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.repository.sm.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author edward
 */
public interface ShopRepository extends GenericJpaRepository<Shop, Long> {

    Shop findFirstByShopIdAndStatusNot(Long shopId, Long status);

    @TemplateQuery
    public List<Shop> findShopByParentIdAndChannelTypeId(@Param("shopId") Long shopId, @Param("channelTypeId") Long channel, @Param("staffId") Long staffId);

    @TemplateQuery
    List<ShopDto> getListShopsByParentShopId(@Param("status") Long status,
                                             @Param("parentShopId") Long parentShopId,
                                             @Param("channelTypeId") Long channelTypeId,
                                             @Param("shopCode") String shopCode);

    @TemplateQuery
    Page<StaffBaseDto> getListShopByLoginUser(@Param("userShopId") Long userShopId,
                                              @Param("channelTypeId") Long channelTypeId,
                                              @Param("inUse") Long inUse,
                                              @Param("shopCode") String shopCode,
                                              @Param("shopName") String shopName,
                                              Pageable pageable);

    @TemplateQuery
    List<ShopTreeDTO> getListBranchIsCenter(@Param("branchId") Long branchId);
    @TemplateQuery
    List<ShopTreeDTO> getShopTreeByLevel(@Param("parentShopId") Long parentShopId, @Param("level") Integer level, @Param("userShopId") Long userShopId, @Param("bcId") Long bcId);
    @TemplateQuery
    BigDecimal getCountShopTreeCheckCenter(@Param("parentShopId") Long parentShopId, @Param("shopId") Long shopId);
    @TemplateQuery
    BigDecimal getShopTreeCheckIsBranch(@Param("shopId") long shopId);
    @TemplateQuery
    Long getBussinessCenterOfAgent(@Param("shopId") Long shopId);
    Shop getShopByShopCode(String shopCode);

    Shop findByShopCode(String shopCode);

    @TemplateQuery
    ShopDto getChannelShopOfBranch(@Param("parentId") Long shopId,
                                          @Param("shopCode") String shopCode);

    @Modifying
    @Transactional(value = "transactionManager")
    @Query("update Shop set y = :longutide, x = :latitude, radius = :radius, lastUpdateUser = :lastUpdateUser, lastUpdateTime = :lastUpdateTime WHERE shopId = :id")
    int updateLocationsForShop(@Param("longutide") Double longutide, @Param("latitude") Double latitude, @Param("radius") Float radius, String lastUpdateUser, Date lastUpdateTime, Long id);

    @TemplateQuery
    Page<StaffDto> getListChannelByShopAndChannelTypeAndCodeAndOwner(@Param("shopId") Long shopId, @Param("channelTypeId") Long channelTypeId, @Param("channelCode") String channelCode, @Param("staffId") Long staffId, PageRequest pageRequest);

    @TemplateQuery
    List<StaffDto> getListChannelByChannelCode(@Param("channelCodes") List<String> channelCodes);

    @Query("SELECT new com.spm.viettel.msm.dto.ShopDto(s.provinceCode, s.district, s.precinct, s.shopType, s.channelTypeId)" +
            " FROM Shop s WHERE s.shopId = :shopId")
    ShopDto getShop(@Param("shopId") Long shopId);

    @Query("SELECT s FROM Shop s WHERE s.shopId = :shopId")
    Shop getShopById(Long shopId);

    @TemplateQuery
    BranchBcDTO getBranchBcOfShop(@Param("shopId") Long shopId);
}
