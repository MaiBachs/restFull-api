package com.spm.viettel.msm.repository.sm;

import com.spm.viettel.msm.dto.AppParamsDTO;
import com.spm.viettel.msm.repository.sm.entity.AppParamsSM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AppParamsSMRepository extends JpaRepository<AppParamsSM, String> {
    @Query("SELECT new com.spm.viettel.msm.dto.AppParamsDTO( ap.name, ap.value ) FROM AppParamsSM ap " +
            "WHERE ap.type = :paramType " +
            "AND ap.baseName LIKE %:posCode% " +
            "AND ap.status = '1' " +
            "ORDER BY ap.name, ap.value")
    List<AppParamsDTO> getUserTypeAndPosCode(@RequestParam("paramType") String paramType,
                                             @RequestParam("posCode") String posCode);

    List<AppParamsSM> findAppParamsSMByTypeAndCode(String paramType, String posCode);

    @Query(value = "select code from app_params where type =:type and status=1",nativeQuery = true)
    List<String> getListChannelAllowPlanVisit (String type);
}
