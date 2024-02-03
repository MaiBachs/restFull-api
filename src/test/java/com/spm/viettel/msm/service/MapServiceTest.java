//package com.spm.viettel.msm.service;
//
//import com.spm.viettel.msm.dto.request.MapSearchRequest;
//import com.spm.viettel.msm.dto.response.MapResponse;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import java.util.ArrayList;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class MapServiceTest {
//    @InjectMocks
//    private MapService mapService;
//    @Mock
//    private AppParamService appParamService;
//    @Mock
//    private StaffService staffService;
//
//    @Test
//    public void test__search__map() throws Exception {
//        Session sessionSM = mock(Session.class);
//        Session sessionANY = mock(Session.class);
//        MapSearchRequest request = mock(MapSearchRequest.class);
//        when(appParamService.getListChannelAllowPlanVisit()).thenReturn(new ArrayList<>());
////        when(staffService.listStaff(any(),any(),anyMap(),anyInt(),anyBoolean(),anyBoolean())).thenReturn(new ArrayList<>());
//        when(staffService.listMapOtherObject(any(),any())).thenReturn(new ArrayList<>());
//        MapResponse result = mapService.searchMap(sessionSM,sessionANY,null,request);
//        assertNotNull(result);
//        verify(appParamService, times(1)).getListChannelAllowPlanVisit();
//        verify(staffService, times(1)).listMapOtherObject(any(),any());
//    }
//
//
//}