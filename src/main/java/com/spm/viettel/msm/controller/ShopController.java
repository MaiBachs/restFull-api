package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.ShopDto;
import com.spm.viettel.msm.dto.ShopTreeDTO;
import com.spm.viettel.msm.dto.request.ShopTreeRequest;
import com.spm.viettel.msm.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController extends BaseController {

    private final Logger loggerFactory = LoggerFactory.getLogger(ShopController.class);
    private final HttpServletRequest request;
    private final ShopService shopService;

    @Autowired
    public ShopController(
            HttpServletRequest request,
            ShopService shopService) {
        this.request = request;
        this.shopService = shopService;
    }


    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
    @Operation(summary = "shop manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Import file"),
            @ApiResponse(responseCode = "400", description = "invalid request review parameters"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })

    @PostMapping("/shop-tree")
    public ResponseEntity<List<ShopTreeDTO>> listShopTree(@RequestBody ShopTreeRequest req){
        List<ShopTreeDTO> shopTree = new ArrayList<>();
        try {
             shopTree = shopService.listShopTree(req.getParentShopId(),req.getLevel(), req.getUserShopId(), req.getStaffId());
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return new ResponseEntity<>(shopTree, HttpStatus.OK);
    }

    @GetMapping("/shopChannel-auditor-config")
    public ResponseEntity<List<ShopDto>> getShopChannelOfAudit(){
        List<ShopDto> listShopChannel = shopService.getListShopChannelOfCCAudit();
        return new ResponseEntity<List<ShopDto>>(listShopChannel, HttpStatus.OK);
    }

    @PostMapping("/get-shop-parent")
    public List<ShopDto> getParentShop(@Param("shopId") Long shopId){
        List<ShopDto> parentShop = shopService.getParentShop(shopId, null, null);
        return parentShop;
    }
}
