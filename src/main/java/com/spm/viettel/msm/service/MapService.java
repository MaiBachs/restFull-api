package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.StaffDto;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.MapSearchRequest;
import com.spm.viettel.msm.dto.response.MapResponse;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.DateUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapService {

    private final StaffService staffService;
    private final AuthenticateService authenticateService;
    private final SaleBtsConfigService btsService;
    private final AppParamService appParamService;


    public MapService(StaffService staffService, AuthenticateService authenticateService, SaleBtsConfigService btsService, AppParamService appParamService) {
        this.staffService = staffService;
        this.authenticateService = authenticateService;
        this.btsService = btsService;
        this.appParamService = appParamService;
    }


    public MapResponse searchMap(Session sessionSM,Session sessionANY,Map<String, Object> params,MapSearchRequest request) throws Exception {
        // build param:
//        this.type = ObjectUtils.defaultIfNull(request.getType(), 0);
        List<StaffDto> staffs = new ArrayList<>();
        MapResponse mapResponse = new MapResponse();
        mapResponse.setListChannelCodeAllowPlans(appParamService.getListChannelAllowPlanVisit());
        switch (request.getType()) {
            case 0:
                staffs.addAll(staffService.listStaff(sessionSM, sessionANY,params, Constants.TYPE_STAFF, BooleanUtils.isTrue(request.getAnypay()), BooleanUtils.isTrue(request.getActiveNumber())));
//                this.shops = StaffBiz.listStaff(getSession(), hashMap, Constants.TYPE_SHOP);
                break;
            case 1:
//                this.shops = StaffBiz.listStaff(getSession(), hashMap, Constants.TYPE_SHOP);
                break;
            case 2:
                staffs.addAll(staffService.listStaff(sessionSM, sessionANY,params, Constants.TYPE_STAFF, BooleanUtils.isTrue(request.getAnypay()), BooleanUtils.isTrue(request.getActiveNumber())));
                break;
        }
        staffs.addAll(staffService.listMapOtherObject(sessionSM,params));
        mapResponse.setStaffs(staffs);
        return mapResponse;
    }
}
