package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.PlanChannelSearchRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.dto.response.PlanChannelResponse;
import com.spm.viettel.msm.dto.response.PlanChannelTargetResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelTarget;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.ChannelTypeService;
import com.spm.viettel.msm.service.PlanChannelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import viettel.passport.client.UserToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PlanChannelControllerTest {

    @InjectMocks
    private PlanChannelController planChannelController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private PlanChannelService planChannelService;
    @Mock
    private ChannelTypeService channelTypeService;

    @Mock
    private ResponseFactory responseFactory;

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private HttpSession sessionMock;

    @Mock
    private UserToken userTokenMock;

    @Before
    public void setUp() {
        when(responseFactory.success(any())).thenCallRealMethod();
    }

    @Test
    public void test__getPlanChannel__haveCase__actionIsLIST__shouldReturnSuccess() throws ParseException {
        UserTokenDto userToken = mock(UserTokenDto.class);
        PlanChannelResponse data = new PlanChannelResponse();
        List<PlanChannelTargetResponse> list = new ArrayList<>();
        PlanChannelTargetResponse planChannelDtoMock = mock(PlanChannelTargetResponse.class);
        list.add(planChannelDtoMock);
        data.setTargetList(list);
        when(planChannelService.searchToResponse(any())).thenReturn(list);
        PlanChannelSearchRequest request =  mock(PlanChannelSearchRequest.class);
        when(request.getAction()).thenReturn(ActionChannelType.LIST);
//        when(request.getBrCode()).thenReturn("AMABR");
//        when(request.getBcCode()).thenReturn("");
//        when(request.getPlanType()).thenReturn(null);
//        when(request.getTargetLevel()).thenReturn(1);
//        when(request.getStaffCode()).thenReturn(null);
//        when(request.getChannelId()).thenReturn(null);
//        when(request.getPlanDate()).thenReturn("2023");
        ResponseEntity<GeneralResponse<List>> result = (ResponseEntity<GeneralResponse<List>>) planChannelController.searchPlanChannel(request);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), result.getBody().getStatus().getCode());
        assertEquals(ResponseStatusEnum.SUCCESS.getMessage(), result.getBody().getStatus().getMessage());
        PlanChannelResponse dataResult = (PlanChannelResponse) result.getBody().getData();
        assertNotNull(dataResult);
        assertEquals(1, dataResult.getTargetList().size());

    }

    @Test
    public void test__getPlanChannel__haveCase__actionIsVIEW__shouldReturnSuccess() throws ParseException {
        UserTokenDto userToken = mock(UserTokenDto.class);

        PlanChannelSearchRequest request = mock(PlanChannelSearchRequest.class);
        when(request.getAction()).thenReturn(ActionChannelType.VIEW);
        when(request.getBrCode()).thenReturn("AMABR");
//        when(request.getBcCode()).thenReturn("");
        when(request.getPlanDate()).thenReturn("2023");
        when(request.getTargetLevel()).thenReturn(1);
        List<ChannelWithGroupDTO> someListOfChannelTypes = new ArrayList<>();
        ChannelWithGroupDTO channelTypeDtoMock = mock(ChannelWithGroupDTO.class);
        someListOfChannelTypes.add(channelTypeDtoMock);

        List<MapPlanChannelTarget> someListOfTargets = new ArrayList<>();
        MapPlanChannelTarget planChannelTarget = mock(MapPlanChannelTarget.class);
        someListOfTargets.add(planChannelTarget);

        when(channelTypeService.getListChannelCanPlanToDevelop()).thenReturn(someListOfChannelTypes); // Replace with actual data
        when(planChannelService.findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(any(), any(), any(), any())).thenReturn(someListOfTargets); // Replace with actual data

        ResponseEntity<GeneralResponse<PlanChannelResponse>> result = (ResponseEntity<GeneralResponse<PlanChannelResponse>>) planChannelController.searchPlanChannel(request);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), result.getBody().getStatus().getCode());
        assertEquals(ResponseStatusEnum.SUCCESS.getMessage(), result.getBody().getStatus().getMessage());

        PlanChannelResponse dataResult = result.getBody().getData();
        assertNotNull(dataResult);
        // Add assertions to check the data in dataResult based on your expectations
    }

    @Test
    public void test__importPlanSaleChannel__shouldReturnSuccess() throws Exception {


    }
}
