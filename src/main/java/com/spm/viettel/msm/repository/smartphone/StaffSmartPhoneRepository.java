package com.spm.viettel.msm.repository.smartphone;


import com.spm.viettel.msm.repository.sm.entity.Staff;
import com.spm.viettel.msm.repository.smartphone.entity.StaffSmartPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffSmartPhoneRepository extends JpaRepository<StaffSmartPhone, Long> {
    StaffSmartPhone getStaffByStaffCode (String staffCode);

    @Query("SELECT s \n" +
            "FROM StaffSmartPhone s \n" +
            "WHERE s.staffId IN (SELECT DISTINCT m.auditorId FROM MapAuditorCheckList m)")
    List<StaffSmartPhone> getStaffAuditor();

}
