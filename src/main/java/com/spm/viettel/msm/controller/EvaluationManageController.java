package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.request.EvaluationManageRequest;
import com.spm.viettel.msm.repository.smartphone.EvaluationRepository;
import com.spm.viettel.msm.service.AppParamService;
import com.spm.viettel.msm.service.EvaluationManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Locale;

@RestController
@RequestMapping("/api/evaluation-manage")
public class EvaluationManageController {

    public static final String ACTION_PLAN_STATUS = "ACTION_PLAN_STATUS";
    @Autowired
    private EvaluationManageService evaluationManageService;

    @Autowired
    private AppParamService appParamService;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    @GetMapping("/fill-list-status")
    public ResponseEntity<?> fillStatusEvaluationManage(){
        return new ResponseEntity<>(appParamService.findAppParamsByTypeAndStatus("ACTION_PLAN_STATUS"), HttpStatus.OK);
    }

    @PostMapping("/search-evaluation-manage")
    public ResponseEntity<?> searchEvaluationManage(@RequestBody EvaluationManageRequest request){
        return new ResponseEntity<>(evaluationManageService.searchEvaluationManage(request), HttpStatus.OK);
    }

    @PostMapping("/detail")
    public ResponseEntity<?> detailEvaluationManage(@RequestBody EvaluationManageRequest request){
        return new ResponseEntity<>(evaluationManageService.detailEvaluation(request), HttpStatus.OK);
    }

    @PostMapping("/edit-action-plan")
    public ResponseEntity<?> editEvaluationManage(@RequestBody EvaluationManageRequest request) throws ParseException {
        return new ResponseEntity<>(evaluationManageService.editActionPlan(request), HttpStatus.OK);
    }
}
