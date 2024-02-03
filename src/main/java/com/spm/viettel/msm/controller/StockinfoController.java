package com.spm.viettel.msm.controller;


import com.spm.viettel.msm.dto.StockInfoDTO;
import com.spm.viettel.msm.dto.request.SearchChannalTypeByActionRequest;
import com.spm.viettel.msm.dto.request.SearchSockInfosRequest;
import com.spm.viettel.msm.dto.request.StockInfosRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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



@RequestMapping("/api/stockinfo")
@RestController
public class StockinfoController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(StockinfoController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private StockService stockService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Operation(summary = "Lấy danh sách StockInfo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved information"),
            @ApiResponse(responseCode = "400", description = "invalid request review parameters"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping("/search")
    private ResponseEntity<?> getStockInfo(@RequestBody SearchSockInfosRequest request){
        return responseFactory.success(stockService.listStockInfo(request.getOwnid(),request.getChannelobj()));
    }
}
