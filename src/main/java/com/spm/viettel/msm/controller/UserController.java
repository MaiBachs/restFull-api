package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.AppParamsDTO;
import com.spm.viettel.msm.dto.ShopDto;
import com.spm.viettel.msm.dto.ShopTreeDTO;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.repository.sm.AppParamsSMRepository;
import com.spm.viettel.msm.repository.sm.ShopRepository;
import com.spm.viettel.msm.repository.sm.StaffRepository;
import com.spm.viettel.msm.repository.sm.entity.AppParamsSM;
import com.spm.viettel.msm.repository.sm.entity.Shop;
import com.spm.viettel.msm.repository.sm.entity.ShopTree;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.ShopService;
import com.spm.viettel.msm.service.StaffService;
import com.spm.viettel.msm.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author edward
 */
@RequestMapping("/api/users")
@RestController
public class UserController extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private AppParamsSMRepository appParamsSMRepository;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }


    @Operation(summary = "Lấy thông tin của người dùng đã login vào hệ thống")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved information"),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping("/information")
    public ResponseEntity<UserTokenDto> information() {
        UserTokenDto userToken = authenticateService.getUserInformation(getUserLogined());
        Boolean visitImport;
        Staff staff = staffService.getByStaffCode(userToken.getStaffCode());
        Shop shp = shopService.getShopById(staff.getShopId());
        String posCode = staff.getPosCode();
        if (staff.getShopId() == 7282) {
            // Bitel
            userToken.setShopType(Constants.SHOP_TYPE_VTP);
            posCode = Constants.SHOP_TYPE_VTP;
        }else if (staffService.isZonalAgent(staff.getStaffId()) != null) {
            // Zonal agent
            userToken.setShopType(Constants.SHOP_TYPE_AGENT);
            ShopTree shBc = new ShopTree();
            if (shopService.isBussinessCenter(shp.getParentShopId(), shp.getShopId())) {
                ShopTreeDTO bc = new ShopTreeDTO();
                bc.setCode(shp.getShopCode());
                bc.setName(shp.getName());
                bc.setShopId(shp.getShopId());
                shBc.setParentShopId(shp.getParentShopId());
                userToken.setBc(bc);
            } else if (shopService.isBranch(staff.getShopId())) {
                shBc.setParentShopId(shp.getShopId());
            } else {
                shBc = shopService.findShopTreeByLevelAndShopId(4, shp.getShopId());
                ShopTreeDTO bc = new ShopTreeDTO();
                bc.setCode(shBc.getShopCode());
                bc.setName(shBc.getShopName());
                bc.setShopId(shBc.getShopId());
                userToken.setBc(bc);
            }
            ShopTree  shBranch = shopService.findShopTreeByLevelAndShopId(3, shBc.getParentShopId());
            ShopTreeDTO branch = new ShopTreeDTO();
            branch.setCode(shBranch.getShopCode());
            branch.setName(shBranch.getShopName());
            branch.setShopId(shBranch.getShopId());
            userToken.setBranch(branch);
        } else if (shopService.isBussinessCenter(shp.getParentShopId(), shp.getShopId())) {
            // Bussiness Center
            userToken.setShopType(Constants.SHOP_TYPE_BC);
            ShopTree shBranch = shopService.findShopTreeByLevelAndShopId(3, shp.getShopId());
            ShopTreeDTO branch = new ShopTreeDTO();
            branch.setCode(shBranch.getRootCode());
            branch.setName(shBranch.getRootName());
            branch.setShopId(shBranch.getRootId());
            userToken.setBranch(branch);
            ShopTreeDTO bc = new ShopTreeDTO();
            bc.setCode(shp.getShopCode());
            bc.setName(shp.getName());
            bc.setShopId(shp.getShopId());
            userToken.setBc(bc);
        } else if (shopService.isBranch(staff.getShopId())) {
            // Brach
            userToken.setShopType(Constants.SHOP_TYPE_BR);
            ShopTreeDTO branch = new ShopTreeDTO();
            branch.setCode(shp.getShopCode());
            branch.setName(shp.getName());
            branch.setShopId(shp.getShopId());
            userToken.setBranch(branch);
        } else {
            userToken.setShopType("UNKNOWN");
        }
        List<AppParamsDTO> userTypes = appParamsSMRepository.
                getUserTypeAndPosCode("QLKD_MAP_ZONAL_AGENT", posCode);
        List<AppParamsDTO> planImport = appParamsSMRepository.
                getUserTypeAndPosCode("BM_MAP_VISIT_PLAN_IMPORT", posCode);
        if (!planImport.isEmpty()){
            visitImport = true;
        }else {
            visitImport = false;
        }
        userToken.setUserTypes(userTypes);
        userToken.setVisitImport(visitImport);
        if (userToken != null) {
            return new ResponseEntity(userToken, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
