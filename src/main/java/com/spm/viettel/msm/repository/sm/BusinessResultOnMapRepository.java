package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.BusinessResultOnMapdto;
import com.spm.viettel.msm.repository.sm.entity.BusinessResultOnMap;
import org.springframework.data.repository.query.Param;

public interface BusinessResultOnMapRepository extends GenericJpaRepository<BusinessResultOnMap, Long> {
    @TemplateQuery
    BusinessResultOnMapdto getActivationDetail(@Param("isStaff") String isStaff, @Param("channelId") Long staffId);
}
