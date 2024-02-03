package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.request.PlanJobRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.service.JobService;
import com.spm.viettel.msm.service.PlanJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/plan-job")
public class PlanJobController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(PlanJobController.class);
    @Autowired
    private PlanJobService planJobService;

    @Autowired
    private JobService jobService;

    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private HttpServletRequest request;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @GetMapping("/fill-plan")
    public ResponseEntity<?> fillPlanUsingAsignCheckList(){
        return new ResponseEntity<>(planJobService.getFillPlanUsingAssignChecklist(), HttpStatus.OK);
    }

    @GetMapping("/fill-checklist-level1-using-assign-checklist")
    public ResponseEntity<?> getFillCheckListLevel1HaveLevel2AndLevel3(){
        return new ResponseEntity<>(jobService.getFillCheckListLevel1HaveLevel2AndLevel3(), HttpStatus.OK);
    }

    @PostMapping("/new-assign-checklist")
    public ResponseEntity<?> newAssignCheckList(@RequestBody List<PlanJobRequest> jobRequests){
        return planJobService.newAssignCheckList(jobRequests);
    }

    @PostMapping("/search-assign-checklist")
    public ResponseEntity<?> searchAssignChecklist(@RequestBody PlanJobRequest request){
        return planJobService.searchAssignCheckList(request);
    }

    @PutMapping("/edit-assign-checklist")
    public ResponseEntity<?> editAssignCheckList(@RequestBody List<PlanJobRequest> jobRequests){
        return planJobService.editAssignCheckList(jobRequests);
    }

    @PutMapping("/delete-assign-checklist")
    public ResponseEntity<?> deleteAssignCheckList(@RequestBody List<PlanJobRequest> planJobRequests){
        return planJobService.deleteAssignCheckList(planJobRequests);
    }
}
