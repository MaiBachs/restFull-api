package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.GetChannelCodeDto;
import com.spm.viettel.msm.dto.request.ShopTreeRequest;
import com.spm.viettel.msm.repository.sm.ShopRepository;
import com.spm.viettel.msm.repository.sm.ShopTreeRepository;
import com.spm.viettel.msm.repository.sm.entity.Shop;
import com.spm.viettel.msm.repository.sm.entity.ShopTree;
import com.spm.viettel.msm.repository.smartphone.ShopSmartPhoneRepository;
import com.spm.viettel.msm.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final ShopTreeRepository shopTreeRepository;
    private final ShopSmartPhoneRepository shopSmartPhoneRepository;
    private final Logger loggerFactory = LoggerFactory.getLogger(ShopService.class);

    private final StaffService staffService;

    @Autowired
    public ShopService(ShopRepository shopRepository, ShopTreeRepository shopTreeRepository,ShopSmartPhoneRepository shopSmartPhoneRepository, StaffService staffService) {
        this.shopRepository = shopRepository;
        this.shopTreeRepository = shopTreeRepository;
        this.shopSmartPhoneRepository = shopSmartPhoneRepository;
        this.staffService = staffService;
    }

    public List<Shop> getShopByParrentIdAndChannelTypeId(GetChannelCodeDto req){
        List<Shop> shopList = new ArrayList<Shop>();
        try{
            shopList = shopRepository.findShopByParentIdAndChannelTypeId(req.getShopId(),req.getChannelTypeId(),req.getOwnerId());
        }catch (Exception e){
            loggerFactory.error(e.getMessage(), e);
        }
        return shopList;
    }



    public ShopTreeDTO getShopInListShop(List<ShopTreeDTO> allShop, String branchCode) {
        for (ShopTreeDTO shop : allShop) {
            if (branchCode.equalsIgnoreCase(shop.getCode())) {
                return shop;
            }
        }
        return null;
    }

    public List<TemplateUpdateLoationDto> updateLocations(List<TemplateUpdateLoationDto> dataFiles, UserTokenDto userInfo){
        List<TemplateUpdateLoationDto> dataRes = new ArrayList<>();
        try {
            for (TemplateUpdateLoationDto data :dataFiles) {
                int qttUpdate = shopRepository.updateLocationsForShop(data.getLongitude(),data.getLatitude(),data.getRadius(),userInfo.getStaffCode(),new Date(),data.getChannelCodeId());
                if(qttUpdate > 0){
                    dataRes.add(data);
                }
            }
        }catch (Exception e){
            loggerFactory.error(e.getMessage(), e);
        }
        return dataRes;
    }

    public List<ShopTreeDTO> listShopTree(Long parentShopId, Integer level, Long userShopId, Long staffId){
        List<ShopTreeDTO> list = new ArrayList<>();
        Long bcId = 0l;
        List<ShopTreeDTO> lst = shopRepository.getListBranchIsCenter(parentShopId);
        List<ShopTreeDTO> shopTreeDtoList = new ArrayList<>();
        list.addAll(lst);
        // Chi nhanh
//        if (level == 3) {
//            shopTreeDtoList = shopTreeRepository.getListShopTree(parentShopId, level, null,null);
//        }
//        sb.append(" and shop_level = :level");
//        if (level == 3 && userShopId != Constants.VTP_SHOP_ID) {
//            sb.append(" and shop_id IN (");
//            sb.append(" select tree.root_id from sm.tbl_shop_tree tree");
//            sb.append(" where tree.shop_id = :userShopId )");
//            sb.append(" and tree.shop_level = 4) ");
//        }
        if (level == 4) {
            bcId = 0l;
            if (isBussinessCenter(parentShopId, userShopId)) {
                bcId = userShopId;
            } else if(staffService.getStaffZonalAgent(staffId) != null){
                bcId = getBussinessCenterOfAgent(userShopId);
            }
//            if (bcId != 0) {
//                shopTreeDtoList = shopTreeRepository.getListShopTree(parentShopId, level, userShopId, bcId);
//            }
        }
        shopTreeDtoList = shopTreeRepository.getListShopTree(parentShopId, level, userShopId,bcId);
        list.addAll(shopTreeDtoList);
        return list;
    }

    public Shop getShopByCode(String shopCode) {
        return shopRepository.findByShopCode(shopCode);
    }

    public List<ShopDto> listShopBC(Long parentShopId,Long ChannelTypeId){
       List<ShopDto> shops = shopRepository.getListShopsByParentShopId(1l,parentShopId,ChannelTypeId,null);
       return shops;
    }
    public List<ShopDto> getParentShop( Long parentId,Long channelTypeId,String shopCode){
        return shopRepository.getListShopsByParentShopId(null,parentId,channelTypeId,shopCode);
    }

    public ShopDto getChannelShopOfBranchSM( Long shopId,String shopCode){
        try {
            return shopRepository.getChannelShopOfBranch(shopId,shopCode);
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public ShopDto getChannelShopOfBranchSmartPhone( Long shopId,String shopCode,Long channelTypeId){
        try {
            return shopSmartPhoneRepository.getChannelShopOfBranch(shopId,shopCode,channelTypeId);
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    public List<ShopDto> getChannelcodeInChannelTypeIdByBranch( Long shopId,Long channelTypeId){
        try {
            return shopSmartPhoneRepository.getChannelcodeInChannelTypeIdByBranch(shopId,channelTypeId);
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

//    public List<ShopTreeDTO> getShopTree(ShopTreeRequest req){
//        try {
//            return shopTreeRepository.getListShopTree(req.getParentShopId(),req.getLevel(),req.getUserShopId(),req.getStaffId());
//        }catch (Exception e){
//            loggerFactory.error(e.getMessage());
//        }
//        return null;
//    }

    public Long getBussinessCenterOfAgent(Long shopId) {
        Long parentShopId = shopRepository.getBussinessCenterOfAgent(shopId);
        if (parentShopId != null) {
            return parentShopId;
        } else {
            return 0l;
        }
    }

    public boolean isBussinessCenter(Long parentShopId, Long shopId) {
        boolean parentIsBranch = isBranch(parentShopId);
        if (parentIsBranch) {
            int i = shopRepository.getCountShopTreeCheckCenter(parentShopId, shopId!=null?shopId:0).intValue();
            return i > 0;
        }
        return false;
    }

    public boolean isBranch(Long shopId) {
        return shopRepository.getShopTreeCheckIsBranch(shopId).intValue() > 0;
    }

    public List<ShopDto> getListShopChannelOfCCAudit(){
        try{
            return shopSmartPhoneRepository.findShopChannelOfAudit();
        }catch (Exception e){
            loggerFactory.error(e.getMessage(), e);
        }
        return null;
    }

    public List<StaffDto> getListChannelByChannelCode(List<String> channelCodes){
        return shopRepository.getListChannelByChannelCode(channelCodes);
    }

    public ShopDto getShop(Long shopId){
        return  shopRepository.getShop(shopId);
    }

    public Shop getShopById(Long shopId){
        return  shopRepository.getShopById(shopId);
    }

    public ShopTree findShopTreeByLevelAndShopId(Integer level, Long shopId){
        return shopTreeRepository.findShopTreeByLevelAndShopId(level, shopId);
    }

    public BranchBcDTO getBranchBcOfShop(Long shopId){
        return shopRepository.getBranchBcOfShop(shopId);
    }
}
