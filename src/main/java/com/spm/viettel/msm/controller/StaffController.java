package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.StaffBaseDto;
import com.spm.viettel.msm.dto.StaffDto;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.SearchStaffOfShopRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.StaffService;
import com.spm.viettel.msm.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController extends BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(EmployeesController.class);
    private final StaffService staffService;
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final AuthenticateService authenticateService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Autowired
    public StaffController(StaffService staffService, HttpServletRequest request, ResponseFactory responseFactory, AuthenticateService authenticateService) {
        this.staffService = staffService;
        this.request = request;
        this.responseFactory = responseFactory;
        this.authenticateService = authenticateService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchStaffOfShopRequest request){
        List<StaffBaseDto> staffs = new ArrayList<>();
        try {
            if (StringUtils.isNotEmpty(request.getUserType()) && "USER_AF_AC".equalsIgnoreCase(request.getUserType())) {
                List<StaffBaseDto> listStaffs = new ArrayList<>();
                List<StaffBaseDto> listAfStaffs = staffService.getListEmployeeOfShop(request.getShopId(), 1, Constants.POS_CODE_AF, null, 1);
                staffs.addAll(listAfStaffs);
                List<StaffBaseDto> listACStaffs = staffService.getListEmployeeOfShop(request.getShopId(), 1, Constants.POS_CODE_AC, null, 1);
                staffs.addAll(listACStaffs);
                staffs.addAll(staffService.getListEmployeeOfShop( request.getShopId(), 0, Constants.USER.CE_DIRECTOR, null, null));
                staffs.addAll(staffService.getListEmployeeOfShop( request.getShopId(), 0, Constants.USER.BR_DIRECTOR, null, null));
                staffs.addAll(staffService.getListEmployeeOfShop( request.getShopId(), 0, Constants.USER.BR_SUB_DIRECTOR, null, null));
            } else {
                UserTokenDto uf = authenticateService.getUserInformation(getUserLogined());
                if (Constants.SHOP_TYPE_AGENT.equals(uf.getShopType()) && !staffService.isManager(uf.getPosCode())) {
                    staffs.add(staffService.isZonalAgent(uf.getUserID()));
                } else {
                    if (request.getShopId() != null) {
                        staffs.addAll(staffService.getListEmployeeOfShop( request.getShopId(), 1, request.getUserType(), null, 1));
                    }
                }
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        ResponseEntity response = responseFactory.success(staffs);
        return response;
    }
}
