package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.PenaltyGravedad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PenaltyGravedadRepository extends JpaRepository<PenaltyGravedad, Long> {
    PenaltyGravedad findByPenaltyIdAndGravedad(Long penaltyId, String gravedad);

    List<PenaltyGravedad> findByPenaltyIdAndStatus(Long penaltyId, Long status);
}
