package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.ChannelTypeDTO;
import com.spm.viettel.msm.dto.request.JobRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.smartphone.entity.Job;
import com.spm.viettel.msm.service.ChannelTypeService;
import com.spm.viettel.msm.service.EvaluationService;
import com.spm.viettel.msm.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(JobController.class);
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private JobService jobService;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private ChannelTypeService channelTypeService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping(path = "/fil-channel-type")
    public ResponseEntity<GeneralResponse<List<ChannelTypeDTO>>> filChannelType(){
        List<ChannelTypeDTO> channelTypeList = channelTypeService.getListChannelTypeUsingOfccAudit();
        return responseFactory.success(channelTypeList);
    }

    @GetMapping(path = "/fil-evaluation")
    public ResponseEntity<GeneralResponse<List<Job>>> filEvaluation(){
        List<Job> evaluationList = jobService.findEvaluation();
        return responseFactory.success(evaluationList);
    }

    @GetMapping(path = "/fil-group")
    public ResponseEntity<GeneralResponse<List<Job>>> filGroup(){
        List<Job> groupList = jobService.findGroup();
        return responseFactory.success(groupList);
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }


    //HIeuNd: checkList

    @PostMapping("/check-list-by-jobId")
    public ResponseEntity<?> getCheckListByJobId(@RequestBody JobRequest request){
        return new ResponseEntity<>(jobService.fillCheckListByJobId(request), HttpStatus.OK);
    }

    @PostMapping("/fill-check-list-by-parentId")
    public ResponseEntity<?> fillCheckListByParentId(@RequestBody JobRequest request){
        return new ResponseEntity<>(jobService.fillCheckListByParentId(request), HttpStatus.OK);
    }

    @PostMapping("/fill-check-list-lv3-not-assign-by-parentId")
    public ResponseEntity<?> fillCheckListLv3NotAssignByParentId(@RequestBody JobRequest request){
        return new ResponseEntity<>(jobService.fillCheckListLv3NotAssignByParentId(request), HttpStatus.OK);
    }

    @PostMapping("/fill-check-list-levle-1")
    public ResponseEntity<?> fillCheckListByLevel1(){
        return new ResponseEntity<>(jobService.fillCheckListLevel1(), HttpStatus.OK);
    }

    @PostMapping("/search-checklist")
    public ResponseEntity<?> searchCheckList(@RequestBody JobRequest request){
        return new ResponseEntity<>(jobService.searchJobByParentIdAndNameAndCode(request), HttpStatus.OK);
    }

    @PostMapping("/add-new-checklist")
    public ResponseEntity<?> addNewCheckList(@RequestBody JobRequest request){
        return jobService.addNewCheckList(request);
    }

    @PutMapping("/edit-checklist")
    public ResponseEntity<?> editCheckList(@RequestBody JobRequest request){
        return jobService.editCheckList(request);
    }

    @PostMapping("/delete-checklist")
    public ResponseEntity<?> deleteCheckList(@RequestBody JobRequest request){
        return jobService.deleteCheckList(request);
    }

}
