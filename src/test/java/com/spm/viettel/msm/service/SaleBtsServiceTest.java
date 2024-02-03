//package com.spm.viettel.msm.service;
//
//import com.spm.viettel.msm.dto.SaleBtsSummaryDto;
//import com.spm.viettel.msm.repository.sm.MapSaleBtsConfigRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SaleBtsServiceTest {
//    @InjectMocks
//    private SaleBtsConfigService btsService;
//
//    @Mock
//    private MapSaleBtsConfigRepository mapSaleBtsConfigRepository;
//
//    @Test
//    public void test__function_listStaff(){
//        Map<String, Object> hashMap = mock(Map.class);
//        when(mapSaleBtsConfigRepository.getListSaleBtsSummary()).thenReturn(new ArrayList<>());
//        List<SaleBtsSummaryDto> result = btsService.listSaleBtsSummary(hashMap);
//        assertNotNull(result);
//        verify(mapSaleBtsConfigRepository, times(1)).getListSaleBtsSummary();
//    }
//}
