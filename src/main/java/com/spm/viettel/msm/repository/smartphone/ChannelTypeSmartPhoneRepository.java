package com.spm.viettel.msm.repository.smartphone;

import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.ChannelTypeDTO;
import com.spm.viettel.msm.repository.smartphone.entity.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelTypeSmartPhoneRepository extends JpaRepository<ChannelType,Long> {

    @Query(value = "SELECT cl FROM ChannelType cl JOIN Plan p ON p.channelTypeId = cl.channelTypeId WHERE p.ccAudit = 1")
    List<ChannelType> getPlansByCcAudit();

    @Query(value = "SELECT new com.spm.viettel.msm.dto.ChannelTypeDTO(cl.channelTypeId,cl.name,cl.objectType) FROM ChannelType cl where cl.channelTypeId in (:channelTypeIds)")
    List<ChannelTypeDTO> findChannelByListId(@Param("channelTypeIds") List<Long> channelTypeIds);
}
