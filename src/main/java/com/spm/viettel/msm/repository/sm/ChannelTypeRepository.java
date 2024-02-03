package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.ChannelTypeDTO;
import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import com.spm.viettel.msm.repository.sm.entity.ChannelType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

public interface ChannelTypeRepository extends GenericJpaRepository<ChannelType, Long> {

    @Query(value = "SELECT CHANNEL_TYPE_ID AS channelTypeId, NAME AS channelName, CODE AS channelCode  FROM CHANNEL_TYPE WHERE CHANNEL_TYPE_ID = :id", nativeQuery = true)
    ChannelWithGroupDTO findChannelTypeById (Long id);

    @TemplateQuery
    List<ChannelWithGroupDTO> getListChannelTypeWithGroup();

    @TemplateQuery
    List<String> getChannelUnit(@Param("number") int number,
                                @Param("ownerId") Long ownerId);

    @TemplateQuery(value = "ChannelType")
    ChannelWithGroupDTO findChannelTypeByName(@Param("name") String name);

    @TemplateQuery
    ChannelWithGroupDTO findChannelByID(@Param("channelTypeId") Long channelTypeId);

    @TemplateQuery
    ChannelTypeDTO findChannelByIDAndName(@Param("channelTypeId") Long channelTypeId, @Param("channelTypeName") String channelTypeName);

    @Query(value = "SELECT new com.spm.viettel.msm.dto.ChannelTypeDTO(c.channelTypeId, c.name, c.status, c.objectType,c.perfixObjectCode) FROM ChannelType c WHERE c.channelTypeId IN :channelTypeIds")
    List<ChannelTypeDTO> findByChannelTypeIds(List<Long> channelTypeIds);

    @Query(value = "SELECT c FROM ChannelType c " +
            "WHERE c.name = :name " +
            "AND c.status = :status")
    ChannelType getByNameAndCodeAndStatus(@RequestParam("name") String name,
                                          @RequestParam("status")Long status);

    @Query("SELECT new com.spm.viettel.msm.dto.ChannelTypeDTO(c.channelTypeId, c.name, c.objectType) FROM ChannelType c WHERE c.status = 1 ")
    List<ChannelTypeDTO> getAllChannelTypeUsingOfAddCheckList();

    List<ChannelType> findByNameNotContainingAndPerfixObjectCodeIsNotNullAndPerfixObjectCodeNotInOrderByName(String nameNotContain, List<String> notIns);



}
