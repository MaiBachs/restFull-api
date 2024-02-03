package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.request.MktUnittypeResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.entity.MktSize;
import com.spm.viettel.msm.repository.sm.entity.MktUnitType;
import com.spm.viettel.msm.service.MktService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/mktunittype")
public class MktUnittypeController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(this.getClass());
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final MktService mktService;



    public MktUnittypeController(HttpServletRequest request, ResponseFactory responseFactory, MktService mktService) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.mktService = mktService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getMktUnittype(){
        MktUnittypeResponse result = new MktUnittypeResponse();
        try {
            List<MktUnitType> unitTypes = mktService.getListMktUnitTypes();
            List<MktSize> sizes = mktService.getListMktSize();
            result.setUnitTypes(unitTypes);
            result.setSizes(sizes);
        } catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }
        return responseFactory.success(result);
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
}
