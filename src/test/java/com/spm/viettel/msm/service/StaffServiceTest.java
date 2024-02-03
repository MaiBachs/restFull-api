//package com.spm.viettel.msm.service;
//
//import org.hibernate.Session;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import java.util.ArrayList;
//import java.util.Map;
//
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class StaffServiceTest {
//    @InjectMocks
//    private StaffService staffService;
//
//    @Mock
//    private ChannelTypeService channelTypeService;
//
//    @Test
//    public void test__function_listStaff() throws Exception {
//        Session sessionSM = mock(Session.class);
//        Session sessionANY = mock(Session.class);
//        Map<String, Object> hashMap = mock(Map.class);
//        when(channelTypeService.getListChannelWithGroup()).thenReturn(new ArrayList<>());
//        when(staffService.listStaffByManager(sessionSM,sessionANY,null,1,false,false)).thenReturn(new ArrayList<>());
//        when(staffService.listStaffByBranchOrBU(sessionSM,sessionANY,null,1,false,false)).thenReturn(new ArrayList<>());
//        when(staffService.listStaff(sessionSM,sessionANY,hashMap,1,false,false)).thenReturn(new ArrayList<>());
//    }
//}
