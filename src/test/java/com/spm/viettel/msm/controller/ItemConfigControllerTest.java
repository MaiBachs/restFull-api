package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.smartphone.entity.AppParamsSmartPhone;
import com.spm.viettel.msm.service.AppParamService;
import com.spm.viettel.msm.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import viettel.passport.client.UserToken;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ItemConfigControllerTest {
    @InjectMocks
    private ItemConfigController itemConfigController;
    @Mock
    private HttpServletRequest request;
    @Mock
    private AppParamService appParamService;
    @Mock
    private ResponseFactory responseFactory;
    @Mock
    private HttpSession sessionMock;
    @Mock
    private UserToken userTokenMock;

    @Before
    public void setup(){
//        when(responseFactory.success(any())).thenCallRealMethod();
//        when(request.getSession()).thenReturn(sessionMock);
//        when(sessionMock.getAttribute(any())).thenReturn(userTokenMock);
    }

    @Test
    public void test__getGravedad__haveData_shouldReturnSuccess(){
//        List<AppParamsSmartPhone> list =  appParamService.findAppParamsByTypeAndStatus(Constants.CODE_GRAVEDAD_USING);
//        assertNotNull(list);
//        assertEquals(HttpStatus.OK, responseFactory.success(list).getStatusCode());
    }
}
