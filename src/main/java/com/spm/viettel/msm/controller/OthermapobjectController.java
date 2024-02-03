package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.repository.sm.entity.OtherObjectType;
import com.spm.viettel.msm.service.OtherObjectTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("api/othermapobjects")
@RestController
public class OthermapobjectController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(OthermapobjectController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private OtherObjectTypeService otherObjectTypeService;

    @Operation(summary = "Lấy danh sách OtherObjectType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved information"),
            @ApiResponse(responseCode = "400", description = "invalid request review parameters"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping("/other-object-type")
    private ResponseEntity<?> getListOtherObjectType(){
        List<OtherObjectType> types = null;
        try {
            types = otherObjectTypeService.getListOtherObjectType();
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
            return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.GENERAL_ERROR);
        }
        return responseFactory.success(types);
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
}
