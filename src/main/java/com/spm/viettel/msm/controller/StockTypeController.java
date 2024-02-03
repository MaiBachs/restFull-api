package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.StockTypeDTO;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stock-type")
public class StockTypeController extends  BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(StockTypeController.class);
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final StockService stockService;

    @Autowired
    public StockTypeController(HttpServletRequest request, ResponseFactory responseFactory, StockService stockService) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.stockService = stockService;
    }


    @GetMapping("/")
    public ResponseEntity<?> searchStockType(){
        List<StockTypeDTO> stockTypes = new ArrayList<>();
        try{
            stockTypes = stockService.listStockType();
        }catch (Exception e){
            loggerFactory.error(e.getMessage(), e);
        }
        return responseFactory.success(stockTypes);
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
}
