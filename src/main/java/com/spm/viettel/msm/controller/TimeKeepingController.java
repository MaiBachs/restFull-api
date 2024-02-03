package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.CoordinatesDto;
import com.spm.viettel.msm.dto.request.TimeKeepingRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.service.TimeKeepingService;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/time-keeping")
public class TimeKeepingController extends BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(TimeKeepingController.class);
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final TimeKeepingService timeKeepingService;
    private final MessageSource messageSource;
    @Autowired
    @Qualifier("SMSessionFactory")
    private SessionFactory smSessionFactory;

    public TimeKeepingController(HttpServletRequest request, ResponseFactory responseFactory, TimeKeepingService timeKeepingService, MessageSource messageSource) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.timeKeepingService = timeKeepingService;
        this.messageSource = messageSource;
    }

    @Override
    public HttpServletRequest getRequest() {
        return null;
    }


    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody TimeKeepingRequest request) {
        Session sessionSM;
        List<CoordinatesDto> coordinates;
        ResponseEntity response = null;
        try {
            if (StringUtils.isNotEmpty(request.getStaffCode()) && request.getObjectType() != null && StringUtils.isNotEmpty(request.getCreatedDate())) {
                String fromDate = request.getCreatedDate() + " " + request.getFromTime();
                if (StringUtils.isEmpty(request.getFromTime())){
                    fromDate = request.getCreatedDate() + " 00:00";
                }
                String toDate = request.getCreatedDate() + " " + request.getToTime();
                if (StringUtils.isEmpty(request.getToTime())){
                    toDate = request.getCreatedDate() + " 23:59";
                }
                sessionSM = smSessionFactory.openSession();
                coordinates = timeKeepingService.searchCoordinates(sessionSM, request.getStaffCode(), request.getObjectType(), fromDate, toDate);
                response = responseFactory.success(coordinates);
            } else {
                response = responseFactory.error(HttpStatus.OK, ResponseStatusEnum.valueOf(messageSource.getMessage(MessageKey.REQUEST_INVALID, null, locale)));
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        return response;
    }
}
