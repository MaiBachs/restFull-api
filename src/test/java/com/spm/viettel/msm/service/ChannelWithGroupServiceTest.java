//package com.spm.viettel.msm.service;
//
//import com.spm.viettel.msm.dto.AreaDto;
//import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
//import com.spm.viettel.msm.repository.sm.ChannelTypeRepository;
//import com.spm.viettel.msm.repository.sm.ParamsRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class ChannelWithGroupServiceTest {
//    @InjectMocks
//    private ChannelWithGroupService channelWithGroupService;
//    @Mock
//    private ParamsRepository paramsRepository;
//    @Mock
//    private ChannelTypeRepository channelTypeRepository;
//
//    @Test
//    public void test__finChannelTypeById__haveData__shouldReturnSuccess() {
//        ChannelWithGroupDTO channelGroupMock = mock(ChannelWithGroupDTO.class);
//        when(channelTypeRepository.findChannelTypeById(any())).thenReturn(channelGroupMock);
//        ChannelWithGroupDTO result = channelWithGroupService.finChannelTypeById(1L);
//        assertNotNull(result);
//        assertEquals(channelGroupMock, result);
//        verify(channelTypeRepository, times(1)).findChannelTypeById(any());
//    }
//
//    @Test
//    public void test__finChannelTypeById__returnNull__shouldReturnSuccess() {
//        when(channelTypeRepository.findChannelTypeById(any())).thenReturn(null);
//        ChannelWithGroupDTO result = channelWithGroupService.finChannelTypeById(1L);
//        assertNull(result);
//        verify(channelTypeRepository, times(1)).findChannelTypeById(any());
//    }
//}