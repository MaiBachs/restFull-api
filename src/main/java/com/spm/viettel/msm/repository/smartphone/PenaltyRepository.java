package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    Penalty findByEvaluationIdAndUserTypeId(Long evaluationId, Long userTypeId);

    @Query("SELECT new com.spm.viettel.msm.repository.smartphone.entity.Penalty(p.id, p.userTypeId, p.userType, p.createdBy, p.createdDate, p.status, p.evaluationId, p.createdBy, p.createdDate, j.name) FROM Penalty p LEFT JOIN Job j ON j.jobId = p.evaluationId WHERE p.status = 1 AND p.id = :penaltyId")
    Penalty findByPenaltyId(@Param("penaltyId") Long penaltyId);
}
