package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.entity.ChannelType;
import com.spm.viettel.msm.service.ChannelTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/channel-type")
public class ChannelTypeController  extends BaseController{

    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final ChannelTypeService channelTypeService;


    private final Logger loggerFactory = LoggerFactory.getLogger(ChannelTypeController.class);

    @Autowired
    public ChannelTypeController(HttpServletRequest request, ResponseFactory responseFactory, ChannelTypeService channelTypeService) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.channelTypeService = channelTypeService;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }


    @GetMapping("/get-channels")
    public ResponseEntity<GeneralResponse<List<ChannelType>>> getChannelType(){
        try {
            return responseFactory.success(channelTypeService.getAllChannelType());
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
       return responseFactory.error(HttpStatus.NOT_FOUND,"Channel type not found",null);
    }
}
