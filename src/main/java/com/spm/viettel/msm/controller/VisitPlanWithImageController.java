package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.VisitPlanBean;
import com.spm.viettel.msm.dto.request.VisitPlanWithImageRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.service.VisitPlanService;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visit-plan-with-image")
public class VisitPlanWithImageController extends BaseController {

    private final Logger loggerFactory = LoggerFactory.getLogger(VisitPlanWithImageController.class);
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final VisitPlanService visitPlanService;
    private final MessageSource messageSource;
    private Boolean hasImage = Boolean.FALSE;


    public VisitPlanWithImageController(HttpServletRequest request, ResponseFactory responseFactory, VisitPlanService visitPlanService, MessageSource messageSource) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.visitPlanService = visitPlanService;
        this.messageSource = messageSource;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping("/")
    public ResponseEntity<?> doExecute(@RequestBody VisitPlanWithImageRequest request) {
        List<VisitPlanBean> visitPlans;
        ResponseEntity response = null;
        try {
            if (request.getChannelObjectType() != null && StringUtils.isNotEmpty(request.getPvdCode()) && request.getMapObjectId() != null) {
                Map<String, Object> result = visitPlanService.getLast10VisitPlan(request.getMapObjectId(), request.getPvdCode(), request.getChannelObjectType());
                visitPlans = (List<VisitPlanBean>) result.get("listResult");
                if (CollectionUtils.isNotEmpty(visitPlans)) {
                    hasImage = true;
                }
                response = responseFactory.success(visitPlans);
            } else {
                response = responseFactory.error(HttpStatus.OK, ResponseStatusEnum.valueOf(messageSource.getMessage(MessageKey.REQUEST_INVALID, null, locale)));
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        return response;
    }
}
