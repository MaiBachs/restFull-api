//package com.spm.viettel.msm.service;
//
//import com.spm.viettel.msm.dto.AreaDto;
//import com.spm.viettel.msm.dto.AreaWithListShop;
//import com.spm.viettel.msm.dto.AreaWithShopDto;
//import com.spm.viettel.msm.repository.sm.AreaRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
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
//public class AreaServiceTest {
//    @InjectMocks
//    private AreaService areaService;
//
//    @Mock
//    private AreaRepository areaRepository;
//
//    @Test
//    public void test__getListArea__EmptyResult__shouldReturnSuccess() {
//        when(areaRepository.getProvinceWithListBranchId(any())).thenReturn(new ArrayList<>());
//        List<AreaWithListShop> result = areaService.getProvinceWithListBranchId(1L);
//        assertNotNull(result);
//        assertEquals(0, result.size());
//        verify(areaRepository, times(1)).getProvinceWithListBranchId(any());
//    }
//
//    @Test
//    public void test__getListArea__haveResultData__shouldReturnSuccess() {
//        List<AreaWithShopDto> data = new ArrayList<>();
//        AreaWithShopDto areaDtoMock = mock(AreaWithShopDto.class);
//        data.add(areaDtoMock);
//        when(areaRepository.getProvinceWithListBranchId(any())).thenReturn(data);
//        List<AreaWithListShop> result = areaService.getProvinceWithListBranchId(1L);
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        verify(areaRepository, times(1)).getProvinceWithListBranchId(any());
//    }
//
//    @Test
//    public void test__getAllDistricts__EmptyResult__shouldReturnSuccess() {
//        when(areaRepository.getAllDistricts(any())).thenReturn(new ArrayList<>());
//        List<AreaWithListShop> result = areaService.getAllDistricts("abc");
//        assertNotNull(result);
//        assertEquals(0, result.size());
//        verify(areaRepository, times(1)).getAllDistricts(any());
//    }
//
//    @Test
//    public void test__getAllDistricts__haveResultData__shouldReturnSuccess() {
//        List<AreaWithListShop> data = new ArrayList<>();
//        AreaWithListShop areaDtoMock = mock(AreaWithListShop.class);
//        data.add(areaDtoMock);
//        when(areaRepository.getAllDistricts(any())).thenReturn(data);
//        List<AreaWithListShop> result = areaService.getAllDistricts("abc");
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        verify(areaRepository, times(1)).getAllDistricts(any());
//    }
//
//    @Test
//    public void test__getListPrecinct__EmptyResult__shouldReturnSuccess() {
//        when(areaRepository.getListPrecinct(any(), any())).thenReturn(new ArrayList<>());
//        List<AreaWithListShop> result = areaService.getListPrecinct("abc", "acsc");
//        assertNotNull(result);
//        assertEquals(0, result.size());
//        verify(areaRepository, times(1)).getListPrecinct(any(), any());
//    }
//
//    @Test
//    public void test__getListPrecinct__haveResultData__shouldReturnSuccess() {
//        List<AreaWithListShop> data = new ArrayList<>();
//        AreaWithListShop areaDtoMock = mock(AreaWithListShop.class);
//        data.add(areaDtoMock);
//        when(areaRepository.getListPrecinct(any(), any())).thenReturn(data);
//        List<AreaWithListShop> result = areaService.getListPrecinct("abc", "casc");
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        verify(areaRepository, times(1)).getListPrecinct(any(), any());
//    }
//}