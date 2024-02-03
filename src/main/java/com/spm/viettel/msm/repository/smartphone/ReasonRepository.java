package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ReasonRepository  extends JpaRepository<Reason, Long> {

    @Query(value = "SELECT r FROM ItemConfigReason icr INNER JOIN Reason r ON icr.reasonId = r.reasonId WHERE icr.itemConfigId = :itemConfigId AND icr.status = 1")
    List<Reason> getReasonDtoByItemConfig(@Param("itemConfigId") Long itemConfigId);

    @Query(value = "SELECT r FROM Reason r WHERE r.status = 1 " +
            "AND (:reasonName IS NULL OR r.name LIKE %:reasonName%) " +
            "ORDER BY r.reasonId")
    List<Reason> searchReasonByName(@RequestParam("reasonName") String reasonName);

    @Query("SELECT DISTINCT r.name FROM Reason r WHERE r.status = 1 ")
    String[] getReasonNameUsingOfCheclist();

    Reason findByNameAndStatus(String name, Long status);

    List<Reason> findReasonByStatus(Long status);

    Reason findReasonByStatusAndReasonId(Long status, Long reasonId);
}
