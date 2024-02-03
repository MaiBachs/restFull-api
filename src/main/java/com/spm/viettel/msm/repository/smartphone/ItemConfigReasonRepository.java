package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.ItemConfigReason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemConfigReasonRepository extends JpaRepository<ItemConfigReason, Long> {
    ItemConfigReason findItemConfigReasonByItemConfigIdAndReasonId(Long itemConfigId,Long reasonId);

    List<ItemConfigReason> findByItemConfigId(Long itemConfigId);

    List<ItemConfigReason> findByItemConfigIdAndStatus(Long itemConfigId, Long status);
}
