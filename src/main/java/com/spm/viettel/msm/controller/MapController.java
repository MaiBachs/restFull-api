package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.SaleBtsSummaryDto;
import com.spm.viettel.msm.dto.StaffDto;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.MapSearchRequest;
import com.spm.viettel.msm.dto.response.MapResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.MapService;
import com.spm.viettel.msm.service.SaleBtsConfigService;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.DateUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/map")
public class MapController extends BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(MapController.class);
    private final MapService mapService;
    private final HttpServletRequest request;
    private final AuthenticateService authenticateService;
    private final SaleBtsConfigService btsService;
    private final ResponseFactory responseFactory;

    @Autowired
    @Qualifier("SMSessionFactory")
    private SessionFactory smSessionFactory;

    @Autowired
    @Qualifier("anypaySessionFactory")
    private SessionFactory anypaySessionFactory;

    @Autowired
    public MapController(MapService mapService, HttpServletRequest request, AuthenticateService authenticateService, SaleBtsConfigService btsService, ResponseFactory responseFactory) {
        this.mapService = mapService;
        this.request = request;
        this.authenticateService = authenticateService;
        this.btsService = btsService;
        this.responseFactory = responseFactory;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchMap(@RequestBody MapSearchRequest request) throws Exception {
        Session sessionSM = null;
        Session sessionANY = null;
        Map<String, Object> hashMap = new HashMap<>();
        MapResponse mapResponse = new MapResponse();
        List<StaffDto> staffs = new ArrayList<>();
        UserTokenDto userInformation = authenticateService.getUserInformation(getUserLogined());
        if (request.getShopId() != null && request.getShopId() > 0) {
            hashMap.put("shopId", request.getShopId());
        }
        if (request.getStaffId() != null && request.getStaffId() > 0) {
            hashMap.put("staffId", request.getStaffId());
        }
        if (StringUtils.isNotEmpty(request.getProvince())) {
            hashMap.put("province", request.getProvince());
        }
        if (StringUtils.isNotEmpty(request.getDistrict())) {
            hashMap.put("district", request.getDistrict());
        }
        if (StringUtils.isNotEmpty(request.getPrecinct())) {
            hashMap.put("precinct", request.getPrecinct());
        }
        if (StringUtils.isNotEmpty(request.getUserType())) {
            hashMap.put("userType", request.getUserType());
        }
        if (StringUtils.isNotEmpty(request.getFromDate())) {
            hashMap.put("fromDate", request.getFromDate());
        } else {
            Date from = DateUtil.addDay(new Date(), -31);
            hashMap.put("fromDate", DateUtil.convertDateTimeToString(from, Constants.DD_MM_YYYY));
        }
        Date toDateTime = new Date();
        if (StringUtils.isNotEmpty(request.getToDate())) {
            toDateTime = DateUtil.convertStringToTime(request.getToDate(), Constants.DD_MM_YYYY);
        }
        toDateTime = DateUtil.addDay(toDateTime, 1);
        hashMap.put("toDate", DateUtil.convertDateTimeToString(toDateTime, Constants.DD_MM_YYYY));

        if (request.getStatus() != null) {
            hashMap.put("status", request.getStatus());
        }
        String channelCode = request.getChannelCode();
        if (StringUtils.isNotEmpty(request.getChannelCode())) {
            hashMap.put("channelCode", "%" + channelCode.trim().toUpperCase() + "%");
            if (request.getShopId() == null || request.getShopId() <= 0) {
                hashMap.put("shopId", userInformation.getShopId());
            }
        }
        System.out.println("=======++Start search MAP +++++=======" + DateFormatUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss.SS"));
        request.setType(ObjectUtils.defaultIfNull(request.getType(),0));
        try {
            sessionSM = smSessionFactory.openSession();
            sessionANY = anypaySessionFactory.openSession();
            mapResponse = mapService.searchMap(sessionSM,sessionANY,hashMap,request);
            if (checkAuthority(Constants.MSM_BTS_RIGHT)) {
                if (request.getBtsRegisterFrom() != null){
                    hashMap.put("btsRegisterFrom", request.getBtsRegisterFrom());
                }
                if (request.getBtsRegisterTo() != null){
                    hashMap.put("btsRegisterTo", request.getBtsRegisterTo());
                }
                if (StringUtils.isNotEmpty(request.getBtsCreateFrom())){
                    hashMap.put("btsCreateFromDate", request.getBtsCreateTo());
                }
                if (StringUtils.isNotEmpty(request.getBtsCreateTo())){
                    hashMap.put("btsCreateToDate", request.getBtsCreateTo());
                }
                List<StaffDto> btsList = btsService.listBTSByShop(sessionSM,hashMap);
                if (request.getBtsSaleCampaign() && !btsList.isEmpty()) {
                    List<String> btsCodeList = btsList.stream().map(StaffDto::getCode).collect(Collectors.toList());
                    hashMap.put("btsCodeList", btsCodeList);
                    if (request.getBtsSalePolicyId() != null){
                        hashMap.put("btsSalePolicyId", request.getBtsSalePolicyId());
                    }
                    if (StringUtils.isNotEmpty(request.getBtsSaleFrom())){
                        hashMap.put("btsSaleFrom", request.getBtsSaleFrom());
                    }
                    if (StringUtils.isNotEmpty(request.getBtsSaleTo())){
                        hashMap.put("btsSaleTo", request.getBtsSaleTo());
                    }
                    List<SaleBtsSummaryDto> saleBtsSummaryList = btsService.listSaleBtsSummary(hashMap);
                    btsList.forEach(bts -> {
                        List<SaleBtsSummaryDto> filteredBtsSummaryList = saleBtsSummaryList.stream().filter(s -> s.getBtsCode().equalsIgnoreCase(bts.getCode())).collect(Collectors.toList());
                        bts.setSaleBtsSummaries(filteredBtsSummaryList);
                    });
                }
                staffs.addAll(mapResponse.getStaffs());
                staffs.addAll(btsList);
            }
            mapResponse.setStaffs(staffs);
            System.out.println("=======++End search MAP +++++======="+ DateFormatUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss.SS"));
        }catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }finally {
            if (sessionSM != null && sessionSM.isOpen()) {
                sessionSM.close();
            }
            if(sessionANY != null && sessionANY.isOpen()){
                sessionANY.close();
            }
        }
        return responseFactory.success(mapResponse);
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
}
