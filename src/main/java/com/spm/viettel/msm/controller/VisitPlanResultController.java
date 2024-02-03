package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.VisitPlanCategoryResultDTO;
import com.spm.viettel.msm.dto.request.VisitPlanResultRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.entity.VisitPlanMap;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.VisitPlanResultService;
import com.spm.viettel.msm.service.VisitPlanService;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.DateUtil;
import com.spm.viettel.msm.utils.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/visit-plan-result")
public class VisitPlanResultController extends BaseController{
    @Autowired
    private VisitPlanResultService visitPlanResultService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private VisitPlanService visitPlanService;
    @Autowired
    private ResponseFactory responseFactory;
    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    UserTokenDto userToken = null;

    @PutMapping(path = "/set-status")
    public ResponseEntity<?> setStatus(@RequestBody VisitPlanResultRequest visitPlanResultRequest){
        userToken = authenticateService.getUserInformation(getUserLogined());
        VisitPlanMap visitPlanMap = visitPlanService.findById(visitPlanResultRequest.getVisitPlanId());
        if (visitPlanMap == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.VISIT_PLAN_NOT_FOUND, null, locale));
        } else {
            visitPlanMap.setCheckListResultStatus(visitPlanResultRequest.getCheckListResultStatus());
            visitPlanMap.setCheckListResultApproveUser(userToken.getStaffCode());
            visitPlanMap.setUpdatedDate(new Date());
            if (visitPlanMap.getCheckListResultStatus() == Constants.VISIT_PLAN_RESULT_REJECT) {
                visitPlanMap.setCheckListResultComment(visitPlanResultRequest.getCheckListResultComment());
                visitPlanMap.setChannelToChecklistStatus(Constants.STATUS_INACTIVE.intValue());
                String content = messageSource.getMessage(messageSource.getMessage(MessageKey.VISIT_PLAN_RESULT_REJECT, null, locale)
                        , new String[]{visitPlanResultRequest.getChannelCode(), DateUtil.date2ddMMyyyyString(visitPlanMap.getVisitTime())}, locale);
                visitPlanResultService.saveSmsWhenReject(userToken.getStaffCode(), content);
            } else {
                boolean isOk = visitPlanResultService.checkVisitPlanResultIsOk(visitPlanMap.getId());
                if (!isOk) {
                    visitPlanMap.setChannelToChecklistStatus(Constants.STATUS_INACTIVE.intValue());
                } else {
                    visitPlanMap.setChannelToChecklistStatus(Constants.ACTIVE_INT);
                }
            }
            visitPlanService.save(visitPlanMap);
            return responseFactory.success(visitPlanMap);
        }
    }

    @PostMapping("/list")
    public ResponseEntity<?> getList(@RequestBody VisitPlanResultRequest visitPlanResultRequest){
        try {
            List<VisitPlanCategoryResultDTO> cateResult = new ArrayList<>();
            List<VisitPlanCategoryResultDTO> allCates = visitPlanResultService.getListCate(visitPlanResultRequest);
            allCates.forEach(l1 -> {
                // Level 2
                if (!CollectionUtils.isEmpty(l1.getSubItems())) {
                    List<VisitPlanCategoryResultDTO> l2Cates = l1.getSubItems();
                    for (VisitPlanCategoryResultDTO l2 : l2Cates) {
                        if (!CollectionUtils.isEmpty(l2.getSubItems())) {
                            cateResult.add(l1);
                            break;
                        }
                    }
                }
            });
            return responseFactory.success(cateResult);
        } catch (Exception e) {
            return  responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.VISIT_PLAN_RESULT_NOT_FOUND, null, locale));
        }
    }

    @PostMapping("/get-file")
    public ResponseEntity<?> getFile(@RequestBody VisitPlanResultRequest visitPlanResultRequest){
        return new ResponseEntity<>(visitPlanResultService.downloadCheckListResultFile(visitPlanResultRequest.getFilePath()), HttpStatus.OK) ;
    }

}
