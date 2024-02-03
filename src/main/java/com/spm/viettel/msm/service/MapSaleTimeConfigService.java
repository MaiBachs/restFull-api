package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.SaleTimeConfigDTO;
import com.spm.viettel.msm.dto.request.SaleTimeConfigRequest;
import com.spm.viettel.msm.repository.sm.MapSaleTimeConfigRepository;
import com.spm.viettel.msm.repository.sm.entity.MapSaleTimeConfig;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapSaleTimeConfigService {
    @Autowired
    private MapSaleTimeConfigRepository mapSaleTimeConfigRepository;

    public Optional<MapSaleTimeConfig> findById(Long id) {
        return mapSaleTimeConfigRepository.findById(id);
    }

    public List<SaleTimeConfigDTO> getListSaleTimeConfigBySalePolicyId(Long mapSalePolicyId) {
        return mapSaleTimeConfigRepository.getListSaleTimeConfigBySalePolicyId(mapSalePolicyId);
    }

    public void deleteSaleTimeConfigByPolicyId(Long mapSalePolicyId) {
        List<MapSaleTimeConfig> mapSaleTimeConfigs = mapSaleTimeConfigRepository.findByMapSalePolicyId(mapSalePolicyId);
        for (MapSaleTimeConfig mapSaleTimeConfig : mapSaleTimeConfigs) {
            mapSaleTimeConfig.setStatus(Constants.STATUS_DELETE);
        }
    }

    public void insertSaleTimeConfig(List<SaleTimeConfigRequest> saleTimeConfigRequests, Long mapSalePolicyId) {
        saleTimeConfigRequests.forEach(t -> {
            MapSaleTimeConfig mapSaleTimeConfig = new MapSaleTimeConfig();
            mapSaleTimeConfig.setMapSalePolicyId(mapSalePolicyId);
            mapSaleTimeConfig.setTimeFrom(t.getTimeFrom());
            mapSaleTimeConfig.setTimeTo(t.getTimeTo());
            mapSaleTimeConfig.setStatus(Constants.STATUS_ACTIVE.intValue());
            mapSaleTimeConfig.setStartTime(DataUtils.timeIn24hFormatToDecimal(t.getTimeFrom()));
            mapSaleTimeConfig.setEndTime(DataUtils.timeIn24hFormatToDecimal(t.getTimeTo()));
            mapSaleTimeConfigRepository.save(mapSaleTimeConfig);
        });

    }

    public void updateSaleTimeConfig(List<SaleTimeConfigRequest> saleTimeConfigRequests) {
        saleTimeConfigRequests.forEach(configRequest -> {
            MapSaleTimeConfig saleTimeConfig = this.findById(configRequest.getId()).get();
            saleTimeConfig.setTimeFrom(configRequest.getTimeFrom());
            saleTimeConfig.setTimeTo(configRequest.getTimeTo());
            saleTimeConfig.setStartTime(DataUtils.timeIn24hFormatToDecimal(configRequest.getTimeFrom()));
            saleTimeConfig.setEndTime(DataUtils.timeIn24hFormatToDecimal(configRequest.getTimeTo()));
            saleTimeConfig.setStatus(configRequest.getStatus());
            mapSaleTimeConfigRepository.save(saleTimeConfig);
        });
    }

    public void deleteSaleTimeConfig(List<SaleTimeConfigRequest> saleTimeConfigRequests) {
        saleTimeConfigRequests.forEach(configRequest -> {
            MapSaleTimeConfig existingConfig = mapSaleTimeConfigRepository.findByIdAndNotDeleted(configRequest.getId());
            existingConfig.setStatus(Constants.STATUS_DELETE);
            mapSaleTimeConfigRepository.save(existingConfig);
        });
    }
}
