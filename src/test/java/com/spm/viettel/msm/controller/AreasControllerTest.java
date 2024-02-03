package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.AreaDto;
import com.spm.viettel.msm.dto.AreaWithListShop;
import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.AreaRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.service.AreaService;
import com.spm.viettel.msm.service.AuthenticateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import viettel.passport.client.UserToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AreasControllerTest {
    @InjectMocks
    private  AreasController areasController;
    @Mock
    private HttpServletRequest request;
    @Mock
    private AuthenticateService authenticateService;
    @Mock
    private ResponseFactory responseFactory;
    @Mock
    private AreaService areaService;
    @Mock
    private HttpSession sessionMock;
    @Mock
    private UserToken userTokenMock;

    @Before
    public void setUp() {
        when(responseFactory.success(any())).thenCallRealMethod();
        when(request.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute(any())).thenReturn(userTokenMock);
    }

    @Test
    public void test__getAreaWithListShop__haveData__shouldReturnSuccess() {
        UserTokenDto userToken = mock(UserTokenDto.class);
        when(authenticateService.getUserInformation(any())).thenReturn(userToken);
        List<AreaWithListShop> data = new ArrayList<>();
        AreaWithListShop areaDtoMock = mock(AreaWithListShop.class);
        data.add(areaDtoMock);
        when(areaService.getProvinceWithListBranchId(any())).thenReturn(data);
        AreaRequest request =  mock(AreaRequest.class);
        when(request.getProvince()).thenReturn("");
        ResponseEntity<GeneralResponse<List>> result = (ResponseEntity<GeneralResponse<List>>) areasController.getAreaWithListShop(request);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), result.getBody().getStatus().getCode());
        assertEquals(ResponseStatusEnum.SUCCESS.getMessage(), result.getBody().getStatus().getMessage());
        List list = result.getBody().getData();
        assertNotNull(list);
        assertEquals(1, list.size());
        verify(areaService, times(1)).getProvinceWithListBranchId(any());
        verify(areaService, times(0)).getAllDistricts(any());
        verify(areaService, times(0)).getListPrecinct(any(), any());
    }
}