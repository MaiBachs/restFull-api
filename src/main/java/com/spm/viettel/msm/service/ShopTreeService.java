package com.spm.viettel.msm.service;

import com.spm.viettel.msm.controller.ShopController;
import com.spm.viettel.msm.dto.ShopTreeDTO;
import com.spm.viettel.msm.repository.sm.ShopRepository;
import com.spm.viettel.msm.repository.sm.ShopTreeRepository;
import com.spm.viettel.msm.repository.sm.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopTreeService {
    private final Logger loggerFactory = LoggerFactory.getLogger(ShopController.class);
    @Autowired
    private ShopTreeRepository shopTreeRepository;
    @Autowired
    private ShopRepository  shopRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;

    public List<ShopTreeDTO> listShopTree(Long parentShopId, Integer level, Long userShopId, Long staffId){
        List<ShopTreeDTO> list = new ArrayList<>();
        Long bcId = 0l;
        List<ShopTreeDTO> lst = shopRepository.getListBranchIsCenter(parentShopId);
        List<ShopTreeDTO> shopTreeDtoList = new ArrayList<>();
        list.addAll(lst);
        if (level == 4) {
            bcId = 0l;
            if (shopService.isBussinessCenter(parentShopId, userShopId)) {
                bcId = userShopId;
            } else if(staffService.getStaffZonalAgent(staffId) != null){
                bcId = shopService.getBussinessCenterOfAgent(userShopId);
            }
        }
        shopTreeDtoList = shopTreeRepository.getListShopTree(parentShopId, level, userShopId,bcId);
        list.addAll(shopTreeDtoList);
        return list;
    }

}
