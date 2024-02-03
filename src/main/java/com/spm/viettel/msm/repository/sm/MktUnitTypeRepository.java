package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.spm.viettel.msm.repository.sm.entity.MktUnitType;

import java.util.List;

public interface MktUnitTypeRepository extends GenericJpaRepository<MktUnitType,Long> {
    List<MktUnitType> findMktUnitTypeByStatus(Integer status);
}
