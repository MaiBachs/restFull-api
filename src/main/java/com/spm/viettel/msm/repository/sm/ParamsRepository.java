package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.spm.viettel.msm.dto.AppParamsDTO;
import com.spm.viettel.msm.repository.sm.entity.AppParamsSM;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParamsRepository extends GenericJpaRepository<AppParamsSM, String> {
    @TemplateQuery
    List<AppParamsDTO> getParams (@Param("TYPE") String type, @Param("code") String code);

    @Query(value = "SELECT code FROM app_params where type ='CHANNEL_TYPE_QLKD_MAP' and status=1", nativeQuery = true)
    List<String> getListChannelAllowPlanVisit();
}
