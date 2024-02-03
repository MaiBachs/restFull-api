package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.request.ReasonRequest;
import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import com.spm.viettel.msm.service.ReasonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reason")
public class ReasonControoler extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(ReasonControoler.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ReasonService reasonService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchReason(@RequestBody ReasonRequest request){
        List<Reason> reasons = new ArrayList<>();
        try {
            reasons = reasonService.searchReasonByName(request);
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return new ResponseEntity<>(reasons, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewReason(@RequestBody ReasonRequest request){
        return reasonService.addNewReason(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editReason(@RequestBody ReasonRequest request,
                                        @RequestParam("reasonId") Long reasonId){
        return reasonService.editReason(request, reasonId);
    }
}
