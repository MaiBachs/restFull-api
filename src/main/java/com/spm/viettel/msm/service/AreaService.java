package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.AreaDto;
import com.spm.viettel.msm.dto.AreaWithListShop;
import com.spm.viettel.msm.dto.AreaWithShopDto;
import com.spm.viettel.msm.repository.sm.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AreaService {
    @Autowired
    private AreaRepository areaRepository;

    public List<AreaWithListShop> getProvinceWithListBranchId(Long userShopId){
        List<AreaWithShopDto> listProviceShop = new ArrayList<>();
        listProviceShop = areaRepository.getProvinceWithListBranchId(userShopId);
        Map<String,AreaWithListShop> map = new LinkedHashMap<String, AreaWithListShop>();
        for(AreaWithShopDto as: listProviceShop){
            AreaWithListShop province = map.get(as.getCode());
            if(province == null){
                province = new AreaWithListShop();
                province.setCode(as.getCode());
                province.setName(as.getName());
                province.setProvince(as.getProvince());
                province.setListShopId(new ArrayList<Long>());
            }
            province.getListShopId().add(as.getShopId());
            map.put(as.getCode(), province);
        }
        return new ArrayList<AreaWithListShop>(map.values());
    }
    public List<AreaWithListShop> getAllDistricts(String province){
        List<AreaWithListShop> listAreaByDistricts = areaRepository.getAllDistricts(province);
        return listAreaByDistricts;
    }

    public List<AreaWithListShop> getListPrecinct(String province, String district){
        List<AreaWithListShop> listAreaByDP = areaRepository.getListPrecinct(province,district);
        return listAreaByDP;
    }

}
