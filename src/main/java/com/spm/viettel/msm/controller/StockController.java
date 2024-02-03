package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.PrepareDto;
import com.spm.viettel.msm.dto.SaleResultDto;
import com.spm.viettel.msm.dto.request.StockRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.dto.response.StockResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(StockController.class);


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private StockService stockService;
    @Autowired
    private ResponseFactory responseFactory;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping(path = "/search")
    public ResponseEntity<GeneralResponse<StockResponse>> getStock(@RequestBody StockRequest stockRequest){
        StockResponse stockResponse = new StockResponse();
        try{
            Map<Integer, Object> map = stockService.getLastSaleResult2(stockRequest.getObjectId(), stockRequest.getChannelObjectType());
            stockResponse.setSaleResults((List<SaleResultDto>) map.get(1));
            stockResponse.setPrepaidDetailList((List<PrepareDto>) map.get(2));
            if (map.get(3) != null) {
                stockResponse.setLastUpdateDateTime((Date) map.get(3));
            }
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return responseFactory.success(stockResponse);
    }
}
