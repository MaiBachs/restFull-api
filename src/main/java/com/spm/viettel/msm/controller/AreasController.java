package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.AreaRequest;
import com.spm.viettel.msm.dto.request.SearchVisitPlanRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.service.AreaService;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.VisitPlanService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
@RestController
@RequestMapping("/api/areas")
public class AreasController extends  BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(AreasController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private AreaService areaService;
    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping("/list")
    public ResponseEntity<?> getAreaWithListShop(@RequestBody AreaRequest request ){
        List<AreaWithListShop> areas = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(request.getProvince()) && StringUtils.isEmpty(request.getProvince())) {
                UserTokenDto userToken = authenticateService.getUserInformation(getUserLogined());
                areas = areaService.getProvinceWithListBranchId(userToken.getShopId());
            }
            else if (StringUtils.isNotEmpty(request.getProvince()) && StringUtils.isEmpty(request.getDistrict())) {
                areas = areaService.getAllDistricts(request.getProvince());
            }
            else if (StringUtils.isNotEmpty(request.getProvince()) && StringUtils.isNotEmpty(request.getDistrict())) {
                areas = areaService.getListPrecinct(request.getProvince(), request.getDistrict());
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
            return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.GENERAL_ERROR);
        }
        return responseFactory.success(areas);
    }
}
