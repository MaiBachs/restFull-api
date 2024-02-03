package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.repository.sm.BusinessResultOnMapRepository;
import com.spm.viettel.msm.repository.sm.ChannelTypeRepository;
import com.spm.viettel.msm.repository.sm.StockHandsetRepository;
import com.spm.viettel.msm.repository.sm.StockRepository;
import com.spm.viettel.msm.utils.Constants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StockService {
    @Autowired
    private BusinessResultOnMapRepository businessResultOnMapRepository;
    @Autowired
    private ChannelTypeRepository channelTypeRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockHandsetRepository stockHandsetRepository;

    public Map<Integer, Object> getLastSaleResult2(Long staffId, String channelObjectType){
        Map<Integer, Object> map = new HashMap<>();
        List<SaleResultDto> saleResultBeans = new ArrayList<>();
        List<PrepareDto> prepaidDetailList = new ArrayList<>();
        PrepareDto active_new_today = new PrepareDto();
        active_new_today.setTypeActive("active_new_today");
        prepaidDetailList.add(active_new_today);
        PrepareDto recharge_today = new PrepareDto();
        recharge_today.setTypeActive("recharge_today");
        prepaidDetailList.add(recharge_today);
        PrepareDto active_new_acumulate = new PrepareDto();
        active_new_acumulate.setTypeActive("active_new_acumulate");
        prepaidDetailList.add(active_new_acumulate);
        PrepareDto recharge_acumulate = new PrepareDto();
        recharge_acumulate.setTypeActive("recharge_acumulate");
        prepaidDetailList.add(recharge_acumulate);
        PrepareDto active_new_lastMonth = new PrepareDto();
        active_new_lastMonth.setTypeActive("active_new_lastMonth");
        prepaidDetailList.add(active_new_lastMonth);
        PrepareDto recharge_lastMonth = new PrepareDto();
        recharge_lastMonth.setTypeActive("recharge_lastMonth");
        prepaidDetailList.add(recharge_lastMonth);
        boolean isStaff = false;
        if (Constants.OBJECT_TYPE_STAFF.equalsIgnoreCase(channelObjectType)){
            isStaff = true;
        }
        BusinessResultOnMapdto result = businessResultOnMapRepository.getActivationDetail(Boolean.toString(isStaff), staffId);
        SaleResultDto pre = new SaleResultDto();
        pre.setServiceType("Prepaid");
        SaleResultDto post = new SaleResultDto();
        post.setServiceType("Postpaid");
        Date lastUpdateDateTime = null;
        if (result != null){
            if (result.getPreLastDateActive() != null) {
                lastUpdateDateTime = result.getPreLastDateActive();
            }
            if (result.getPreLastDateActive() != null) {
                pre.setLastUpdated(result.getPreLastDateActive());
            }
            if (result.getPreActiveLastDay() != null) {
                pre.setToday(result.getPreActiveLastDay());
            }
            if (result.getPreAcumN() != null) {
                pre.setCount(result.getPreAcumN());
            }
            if (result.getPreAcumN1() != null) {
                pre.setLastMonth(result.getPreAcumN1());
            }
            if (result.getPostLastDateActive() != null) {
                post.setLastUpdated(result.getPostLastDateActive());
            }
            if (result.getPostActiveLastDay() != null) {
                post.setToday(result.getPostActiveLastDay());
            }
            if (result.getPostAcumN() != null) {
                post.setCount(result.getPostAcumN());
            }
            if (result.getPostAcumN1() != null) {
                post.setLastMonth(result.getPostAcumN1());
            }
            if (result.getPreActiveRcLastDay() != null) {
                active_new_today.setTotal(result.getPreActiveRcLastDay());
            }
            if (result.getPreAcumNNoRc() != null) {
                recharge_today.setTotal(result.getPreAcumNNoRc());
            }
            if (result.getPreAcumNRc() != null) {
                active_new_acumulate.setTotal(result.getPreAcumNRc());
            }
            if (result.getPreAcumNRc() != null) {
                recharge_acumulate.setTotal(result.getPreAcumNRc());
            }
            if (result.getPreAcumN1NoRc() != null) {
                active_new_lastMonth.setTotal(result.getPreAcumN1NoRc());
            }
            if (result.getPreAcumN1Rc() != null) {
                recharge_lastMonth.setTotal(result.getPreAcumN1Rc());
            }
        }
        saleResultBeans.add(pre);
        saleResultBeans.add(post);
        map.put(1, saleResultBeans);
        map.put(2, prepaidDetailList);
        map.put(3, lastUpdateDateTime);
        return map;
    }



    public List<StockInfoDTO> listStockInfo(Long ownerId, String channelObjectType) {
        // Get Channel type Unit of channel
        List<String> unitResults;
        List<StockInfoDTO> dataStockInfo = null;
        try {
            if ("2".equals(channelObjectType)) {
                unitResults = channelTypeRepository.getChannelUnit(2, ownerId);
            } else {
                unitResults = channelTypeRepository.getChannelUnit(1, ownerId);
            }
            int channelIsVTUnit = 0;

            if (CollectionUtils.isNotEmpty(unitResults)) {
                for (String vtUnit : unitResults) {
                    if (vtUnit == Constants.VT_UNIT) {
                        channelIsVTUnit = 2;
                    }
                    break;
                }
            }
            dataStockInfo = new ArrayList<>();
            List<StockTypeDTO> listType = listStockType();
            for (StockTypeDTO type : listType) {
                System.out.println(type.getTableName());
                String tableName = type.getTableName();
                List<StockInfoDTO> data = stockHandsetRepository.searchListStockInfos(tableName,ownerId,type.getName(),type.getId(),channelIsVTUnit);
                dataStockInfo.addAll(data);
            }
            return dataStockInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataStockInfo;
    }

    public List<StockTypeDTO> listStockType() {
        return stockRepository.getListStockType();
    }
}
