package com.spm.viettel.msm.repository.sm;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.spm.viettel.msm.repository.sm.entity.Position;

import java.util.List;

public interface PositionRepository extends GenericJpaRepository<Position,Long> {
    List<Position> findByStatus(Long status);
}
