package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.ShopTreeDTO;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.ShopTreeRequest;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.ShopTreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/shop-tree")
public class ShopTreeController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(ShopController.class);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private ShopTreeService shopTreeService;

    @PostMapping("/list-shop-tree")
    public ResponseEntity<List<ShopTreeDTO>> listShopTree(@RequestBody ShopTreeRequest request){
        List<ShopTreeDTO> response = null;
        try{
            UserTokenDto userTokenDto = authenticateService.getUserInformation(getUserLogined());
            response = shopTreeService.listShopTree(request.getParentShopId(), request.getLevel(), userTokenDto.getShopId(), userTokenDto.getUserID());
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
}
