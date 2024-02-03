package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.spm.viettel.msm.repository.sm.entity.MktSize;
import com.spm.viettel.msm.repository.sm.entity.MktUnitType;

import java.util.List;

public interface MktSizeRepository extends GenericJpaRepository<MktSize,Long> {
    List<MktSize> getMktSizesByStatus(Integer status);
}
