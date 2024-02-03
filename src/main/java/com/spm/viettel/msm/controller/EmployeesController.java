package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.StaffBaseDto;
import com.spm.viettel.msm.dto.StaffDto;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.SearchChannelCodeRequest;
import com.spm.viettel.msm.dto.request.StaffBaseRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.StaffService;
import com.spm.viettel.msm.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(EmployeesController.class);

    @Autowired
    private StaffService staffService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private AuthenticateService authenticateService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping(path = "/search")
    public ResponseEntity<GeneralResponse<List<StaffBaseDto>>> getListEmployeeOfShop(@RequestBody StaffBaseRequest staffBaseRequest){
        UserTokenDto userToken = authenticateService.getUserInformation(getUserLogined());
        List<StaffBaseDto> staffs = new ArrayList<>();
        try{
            if(Constants.SHOP_TYPE_AGENT.equals(userToken.getShopType()) && !staffService.isManager(userToken.getPosCode())){
                StaffBaseDto staffBaseDto = new StaffBaseDto();
                staffBaseDto.setShopId(userToken.getShopId());
                staffBaseDto.setStaffOwnerId(userToken.getUserID());
                staffBaseDto.setCode(userToken.getStaffCode());
                staffs.add(staffBaseDto);
            }else{
                StaffBaseDto all = new StaffBaseDto();
                all.setId(-1l);
                all.setSeparator("");
                staffs.add(all);
                Long shopId = staffBaseRequest.getShopId() != null? staffBaseRequest.getShopId() : userToken.getShopId();
                staffs = staffService.getListEmployeeOfShop(shopId, 1, null, null, 1);
                return responseFactory.success(staffs);
            }
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return responseFactory.success();
    }

    @PostMapping("/list-channels-with-lat-long")
    public ResponseEntity<Page<StaffDto>> getStaffLike(@RequestBody SearchChannelCodeRequest req){
        Page<StaffDto> staffLike = staffService.getChannelsForUpdateLocation(req);
        return new ResponseEntity<>(staffLike, HttpStatus.OK);
    }
}
