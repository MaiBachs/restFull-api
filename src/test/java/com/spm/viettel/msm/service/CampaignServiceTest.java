//package com.spm.viettel.msm.service;
//
//import com.spm.viettel.msm.dto.CampaignExcelDto;
//import com.spm.viettel.msm.dto.UserTokenDto;
//import com.spm.viettel.msm.dto.request.CampaignSearchRequest;
//import com.spm.viettel.msm.repository.sm.MapCheckListCampaignRepository;
//import com.spm.viettel.msm.repository.sm.entity.MapCheckListCampaign;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CampaignServiceTest {
//    @InjectMocks
//    private MapCheckListCampainService campaignService;
//
//    @Mock
//    private MapCheckListCampaignRepository mapCheckListCampaignRepository;
//
//    @Test
//    public void test__campaign__add__new__return__boolean(){
//        // Tạo đối tượng giả
//        CampaignExcelDto mockData = mock(CampaignExcelDto.class);
//        UserTokenDto mockUserTokenDto = mock(UserTokenDto.class);
//
//        // Sử dụng mockData và mockRepository trong test case
//        boolean result = campaignService.insertCampaign(mockData, mockUserTokenDto);
//        assertEquals(false,result);
//        verify(mapCheckListCampaignRepository, times(1)).save(any());
//    }
//    @Test
//    public void test__campaign__search__new__return__list(){
//        // Tạo đối tượng giả
//        CampaignSearchRequest mockData = mock(CampaignSearchRequest.class);
//        when(mockData.getCurrentPage()).thenReturn(0);
//        when(mockData.getPageSize()).thenReturn(10);
//        int pageCurrent = mockData.getCurrentPage();
//        PageRequest pageRequest = PageRequest.of(1, 10);
//        // Sử dụng mockData và mockRepository trong test case
//        Page<MapCheckListCampaign> result = campaignService.searchCampaigns(mockData);
//        assertNotNull(result.get());
//        verify(mapCheckListCampaignRepository, times(1)).searchCampaign(any(),any(),any(),any(),pageRequest);
//        System.out.println(result.getSize());
//    }
//}
