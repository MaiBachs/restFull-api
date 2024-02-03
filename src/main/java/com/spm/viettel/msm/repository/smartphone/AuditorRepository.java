package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.MapAuditorCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface AuditorRepository extends JpaRepository<MapAuditorCheckList,Long> {

    @Modifying
    @Transactional(value = "smartphoneTransactionManager")
    @Query(value = "INSERT INTO MAP_AUDITOR_CHECK_LIST " +
            "(BR_ID, CHANNEL_TYPE_ID, AUDITOR_ID, SHOP_ID, CREATED_BY, CREATED_DATE, UPDATED_BY, UPDATED_DATE)"+
            "VALUES (:branchId, :channelTypeId, :auditorId, :shopId, :createdBy, :createdDate, :updatedBy, :updatedDate)",
            nativeQuery = true)
    int insertMapAuditorCheckList(@Param("branchId") Long branchId,
                                      @Param("channelTypeId") Long channelTypeId,
                                      @Param("auditorId") Long auditorId,
                                      @Param("shopId") Long shopId,
                                      @Param("createdBy") String createdBy,
                                      @Param("createdDate") String createdDate,
                                      @Param("updatedBy") String updatedBy,
                                      @Param("updatedDate") String updatedDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM MapAuditorCheckList WHERE id = :id")
    void deleteMapAuditorCheckList(@Param("id") Long id);

    @Query("select count(c) from MapAuditorCheckList c where c.auditorId = :auditorId and c.branchId = :branchId and c.channelTypeId = :channelTypeId  and c.shopId = :shopId")
    int getCountAuditor(@Param("auditorId") Long auditorId, @Param("branchId") Long branchId, @Param("channelTypeId") Long channelTypeId, @Param("shopId") Long shopId);
}
