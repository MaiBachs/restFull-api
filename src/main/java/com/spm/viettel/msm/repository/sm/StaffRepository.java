package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.BranchBcDTO;
import com.spm.viettel.msm.dto.BranchDto;
import com.spm.viettel.msm.dto.StaffBaseDto;
import com.spm.viettel.msm.dto.StaffDto;
import com.spm.viettel.msm.repository.sm.entity.ReportStaff;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author edward
 */
public interface StaffRepository extends GenericJpaRepository<Staff, Long> {
    Staff findFirstByStaffCodeIgnoreCaseAndStatus(String staffCode, Long status);
        Staff findFirstByStaffCodeIgnoreCase(String staffCode);

    @Modifying
    @Transactional(value = "transactionManager")
    @Query("update Staff set y = :longutide, x = :latitude, radius = :radius, lastUpdateUser = :lastUpdateUser, lastUpdateTime = :lastUpdateTime where staffId = :id")
    int updateLocationsForStaff(@Param("longutide") Double longutide, @Param("latitude") Double latitude, @Param("radius") Float radius, String lastUpdateUser, Date lastUpdateTime, Long id);

    @TemplateQuery
    Page<StaffBaseDto> getStaffs(@Param("shopCode") String shopCode, @Param("inUse") Long inUse, @Param("staffCode") String staffCode, @Param("staffName") String staffName, Pageable pageable);

    @TemplateQuery
    StaffDto getUserImportInformation(@Param("userCode") String userCode);

    @TemplateQuery
    Integer CheckStaffMustValidateWhenImport(@Param("posCode") String posCode);

    @TemplateQuery
    List<ReportStaff> ListPVDWithZonal(List<Long> staffIds, String managerPosCode);

    @TemplateQuery
    BranchBcDTO BranchBcOfStaff(@Param("staffId") Long staffId);

    @Query("from Staff a where a.status = 1 and a.shopId=:shopId " +
            " AND exists  (select b.channelTypeId from ChannelType b where b.objectType = :objectType" +
            " and   b.isVtUnit = :isVtUnit and b.status = :status and b.channelTypeId = a.channelTypeId) order by lower(a.staffCode) asc")
    Page<Staff> getAllStaffsOfShop(@Param("shopId") Long shopId, @Param("objectType") String objectType, @Param("isVtUnit") String isVtUnit, @Param("status") Long status, Pageable pageable);

    @TemplateQuery
    List<StaffBaseDto> getListEmployeeOfShop(@Param("shopId") Long shopId, @Param("status") Integer status, @Param("userType") String userType,@Param("hasChild") Integer hasChild,  @Param("channelCode") String channelCode);

    @TemplateQuery
    Page<BranchDto> getBranches(@Param("branchCode") String branchCode , @Param("name") String name , Pageable pageable);

    Staff getStaffByStaffCode (String staffCode);

    @TemplateQuery
    StaffBaseDto getStaffZonalAgent(@Param("staffId") Long staffId);

    Staff getByStaffCode(String staffCode);

    List<Staff> findStaffByShopIdAndChannelTypeId (@Param("shopId") Long shopId,@Param("channelTypeId") Long channelTypeId);

    @Query("SELECT new com.spm.viettel.msm.dto.StaffDto(" +
            "sf.staffId, sf.shopId, sf.staffCode, sf.name,sf.staffOwnerId,sf.channelTypeId, sf.y, sf.x, sf.radius)" +
            "FROM Staff sf " +
            "WHERE ((:shopId IS NOT NULL AND sf.shopId = :shopId) OR (:shopId IS NULL)) " +
            "AND ((:channelTypeId IS NOT NULL AND sf.channelTypeId = :channelTypeId) OR (:channelTypeId IS NULL)) " +
            "AND ((:staffId IS NOT NULL AND sf.staffOwnerId = :staffId) OR (:staffId IS NULL))")
    List<StaffDto> findStaffByShopIdAndChannelTypeId(@Param("shopId") Long shopId, @Param("channelTypeId") Long channelTypeId, @Param("staffId") Long staffId);

    @TemplateQuery
    Page<StaffDto> getListChannelByShopAndChannelTypeAndStaffCodeAndOwner(@Param("shopId") Long shopId, @Param("channelTypeId") Long channelTypeId, @Param("channelCode") String channelCode, @Param("staffId") Long staffId, PageRequest pageRequest);
    @TemplateQuery
    List<StaffDto> getChannelByDistance(@Param("pointX") Double x,@Param("pointY") Double y, @Param("distance") Double distance, @Param("channelTypeId") Long channelTypeId);
    @TemplateQuery
    List<StaffDto> getListDFsByDistance(@Param("pointX") Double x,@Param("pointY") Double y, @Param("distance") Double distance);
    @TemplateQuery
    List<StaffDto> getListChannelPlanApprovedByDistance(@Param("pointX") Double x,@Param("pointY") Double y, @Param("distance") Double distance, @Param("channelTypeId") Long channelTypeId);

    @Query(value = "select staff_id id, staff_code code, name, id_no idNo from staff  where status=1 AND pos_code IN (select code from sm.app_params where type = 'BM_MAP_OWNER_MANAGE' and status=1) AND staff_id= :staffId ",nativeQuery = true)
    StaffBaseDto isZonalAgent(@Param("staffId") Long staffId);

    Staff findByStaffOwnerId(Long staffOwnerId);

    Staff findByStaffCode(String staffCode);
}
