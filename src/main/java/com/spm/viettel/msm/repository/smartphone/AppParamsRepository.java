package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.AppParamsSmartPhone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppParamsRepository extends JpaRepository<AppParamsSmartPhone, String> {
    AppParamsSmartPhone getAppParamsByTypeAndCode(String type, String code);

    List<AppParamsSmartPhone> findAppParamsByTypeAndStatus(String type, String status);
}
