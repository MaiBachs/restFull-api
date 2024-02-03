package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.request.PlanRequest;
import com.spm.viettel.msm.service.PlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/plan")
public class PlanController extends BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(PlanController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PlanService planService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @GetMapping("/list/fil_channel_type")
    public ResponseEntity<?> getListChannelType(){
        return new ResponseEntity<>(planService.getChannelTypesUsingOfAddCheckList(), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchPlanByChannel(@RequestBody PlanRequest request){
        return planService.searchPlanByChannel(request);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewPlan(@RequestBody PlanRequest request){
        return planService.addNewPlan(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> updatePlan(@RequestBody PlanRequest request){
        return planService.updatePlan(request);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deletePlan(@RequestBody PlanRequest request){
        return planService.deleteChannelPlan(request);
    }
}
