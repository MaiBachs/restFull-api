package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import com.spm.viettel.msm.dto.request.SearchChannalTypeByActionRequest;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.service.ChannelWithGroupService;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/api/channelwithgroup")
@RestController
public class ChannelwithgroupController extends BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(ChannelwithgroupController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private ChannelWithGroupService channelWithGroupService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Operation(summary = "Lấy danh sách channelWithGroupAction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved information"),
            @ApiResponse(responseCode = "400", description = "invalid request review parameters"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping("/getChannel")
    public ResponseEntity<?> getListChannels(@RequestBody SearchChannalTypeByActionRequest actionChannelType) {
        List<ChannelWithGroupDTO> channels = null;
        try {
            channels = new ArrayList<>();
            switch (actionChannelType.getAction()) {
                case CHANNEL_TYPE_CAN_PLAN_DEVELOP:
                    channels = channelWithGroupService.getListChannelCanPlanToDevelop();
                    break;
                case ALL:
                    channels = channelWithGroupService.getListChannelWithGroup();
                    break;
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
            return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.GENERAL_ERROR);
        }
        return responseFactory.success(channels);
    }

}
