package com.spm.viettel.msm.repository.smartphone;

import com.spm.viettel.msm.repository.smartphone.entity.ItemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ItemConfigRepository extends JpaRepository<ItemConfig, Long> {
    ItemConfig findByJobIdAndChannelTypeId(Long jobId, Long channelTypeId);
    @Query("SELECT i FROM ItemConfig i WHERE i.status = 1")
    List<ItemConfig> findAllItem();

    @Query("SELECT ic FROM ItemConfig ic WHERE ic.id = :itemConfigId AND ic.status = 1")
    ItemConfig getItemConfigByitemConfigId(Long itemConfigId);
}
