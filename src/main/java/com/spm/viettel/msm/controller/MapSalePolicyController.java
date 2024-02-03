package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.PlanSalePolicyDTO;
import com.spm.viettel.msm.dto.RecordList;
import com.spm.viettel.msm.dto.request.SalePolicyRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.entity.MapSalePolicy;
import com.spm.viettel.msm.service.SalePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/map-sale-policy")
public class MapSalePolicyController extends BaseController{
    @Autowired
    private SalePolicyService salePolicyService;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private HttpServletRequest request;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @GetMapping("/fill-list-policy")
    public ResponseEntity<?> fillListPolicy(){
        return responseFactory.success(salePolicyService.findListPolicy());
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchSalePolicy(@RequestBody SalePolicyRequest request){
        return responseFactory.success(salePolicyService.getListSalePolicyWithSaleTime(request));
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteSalePolicy(@RequestBody SalePolicyRequest request){
        return salePolicyService.deleteSalePolicy(request);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSalePolicy(@RequestBody SalePolicyRequest request){
         return salePolicyService.addNewSalePolicy(request);
    }

    @PutMapping("/set-status")
    public ResponseEntity<?> setStatusSalePolicy(@RequestBody SalePolicyRequest request){
        return salePolicyService.setStatusSalePolicy(request);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editSalePolicy(@RequestBody SalePolicyRequest request){
        return salePolicyService.editSalePolicy(request);
    }

    @PostMapping("/detail")
    public ResponseEntity<?> detailSalePolicy(@RequestBody SalePolicyRequest request) throws ParseException {
        return salePolicyService.detailSalePolicy(request);
    }

    @GetMapping("/list-sale-policy")
    public ResponseEntity<GeneralResponse<List<MapSalePolicy>>> getListSalePolicy(){
        return responseFactory.success(salePolicyService.getListSalePolicy());
    }
}
