package com.spm.viettel.msm.service;

import com.spm.viettel.msm.repository.smartphone.ItemConfigReasonRepository;
import com.spm.viettel.msm.repository.smartphone.entity.ItemConfigReason;
import com.spm.viettel.msm.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemConfigReasonService {
    @Autowired
    private ItemConfigReasonRepository itemConfigReasonRepository;

    public ItemConfigReason save(ItemConfigReason itemConfigReason){
        return itemConfigReasonRepository.save(itemConfigReason);
    }

    public List<ItemConfigReason> saveAll(List<ItemConfigReason> itemConfigReasons){
        return itemConfigReasonRepository.saveAll(itemConfigReasons);
    }

    public ItemConfigReason findItemConfigReasonByItemConfigIdAndReasonId(Long itemConfigId,Long reasonId){
        return itemConfigReasonRepository.findItemConfigReasonByItemConfigIdAndReasonId(itemConfigId, reasonId);
    }

    public List<ItemConfigReason> deleteAllByItemConfigId(Long itemConfigId){
        List<ItemConfigReason> itemConfigReasons = itemConfigReasonRepository.findByItemConfigId(itemConfigId);
        for(ItemConfigReason itemConfigReason: itemConfigReasons){
            itemConfigReason.setStatus(0l);
        }
        return itemConfigReasonRepository.saveAll(itemConfigReasons);
    }

    public List<ItemConfigReason> findByItemConfigIdAndStatus(Long itemConfigId, Long status){
        return itemConfigReasonRepository.findByItemConfigIdAndStatus(itemConfigId, status);
    }

    public List<ItemConfigReason> findByItemConfigId(Long itemConfigId){
        return itemConfigReasonRepository.findByItemConfigId(itemConfigId);
    }
}
