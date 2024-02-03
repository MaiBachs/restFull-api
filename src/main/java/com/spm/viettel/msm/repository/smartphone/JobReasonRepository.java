package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.JobReason;
import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobReasonRepository extends JpaRepository<JobReason, Long> {
//    @Query(value = "SELECT new Reason (r.reasonId, r.code,r.name, r.note, r.status, r.createdDate, r.lastUpdate) FROM Job j INNER JOIN JobReason jr ON j.jobId = jr.jobId INNER JOIN Reason r ON jr.reasonId = r.reasonId WHERE j.jobId = :jobId")
}
