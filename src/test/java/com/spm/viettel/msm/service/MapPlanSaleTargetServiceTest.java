//package com.spm.viettel.msm.service;
//
//import com.spm.viettel.msm.dto.PlanSaleTargetDto;
//import com.spm.viettel.msm.dto.ShopTreeDTO;
//import com.spm.viettel.msm.dto.StaffBaseDto;
//import com.spm.viettel.msm.dto.UserTokenDto;
//import com.spm.viettel.msm.dto.request.PlanSaleSearchRequest;
//import com.spm.viettel.msm.repository.sm.MapPlanSaleTargetRepository;
//import com.spm.viettel.msm.repository.sm.ShopRepository;
//import com.spm.viettel.msm.repository.sm.ShopTreeRepository;
//import com.spm.viettel.msm.repository.sm.entity.MapPlanSaleTarget;
//import com.spm.viettel.msm.utils.Constants;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.context.MessageSource;
//import viettel.passport.client.UserToken;
//import java.util.Locale;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class MapPlanSaleTargetServiceTest {
//    @InjectMocks
//    private MapPlanSaleTargetService mapPlanSaleTargetService;
//    @Mock
//    private MapPlanSaleTargetRepository mapPlanSaleTargetRepository;
//    @Mock
//    private MessageSource messageSource;
//    @Mock
//    private ShopTreeService shopTreeService;
//    @Mock
//    private StaffService staffService;
//
//    @Test
//    public void test_import_withVtp_shouldReturnSuccess(){
//        // ___test vtp
//
//        //fake data excel
//        List<MapPlanSaleTarget> targets = new ArrayList<>();
//        MapPlanSaleTarget mapPlanSaleTarget1 = new MapPlanSaleTarget(){};
//        mapPlanSaleTarget1.setRowIndex(1);
//        mapPlanSaleTarget1.setBrCode("AMABR");
//        mapPlanSaleTarget1.setT1Target(1);
//        mapPlanSaleTarget1.setT2Target(2);
//        mapPlanSaleTarget1.setT3Target(3);
//        mapPlanSaleTarget1.setT4Target(4);
//        mapPlanSaleTarget1.setT5Target(3);
//        mapPlanSaleTarget1.setT6Target(2);
//        mapPlanSaleTarget1.setT7Target(4);
//        mapPlanSaleTarget1.setT8Target(5);
//        mapPlanSaleTarget1.setT9Target(9);
//        mapPlanSaleTarget1.setT10Target(3);
//        mapPlanSaleTarget1.setT11Target(4);
//        mapPlanSaleTarget1.setT12Target(4);
//        MapPlanSaleTarget mapPlanSaleTarget2 = new MapPlanSaleTarget();
//        mapPlanSaleTarget2.setRowIndex(0);
//        targets.add(mapPlanSaleTarget1);
//        targets.add(mapPlanSaleTarget2);
//        List<MapPlanSaleTarget> listTargetDelete =  new ArrayList<>();
//        List<MapPlanSaleTarget> listTargetOk = new ArrayList<>();
//
//        // fake dependent funtion
//        UserTokenDto userToken = mock(UserTokenDto.class);
//        when(userToken.getStaffCode()).thenReturn("THANHNV3_VTP");
////        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("error comment");
//        List<ShopTreeDTO> branches = new ArrayList<>();
//        ShopTreeDTO shopTreeDTO = new ShopTreeDTO();
//        shopTreeDTO.setShopId(1002159l);
//        shopTreeDTO.setName("AMAZONAS BRANCH");
//        shopTreeDTO.setCode("AMABR");
//        branches.add(shopTreeDTO);
//        when(shopTreeService.listShopTree(Constants.VTP_SHOP_ID, 3, Constants.VTP_SHOP_ID, null)).thenReturn(branches);
//        MapPlanSaleTarget targetInDb = mock(MapPlanSaleTarget.class);
//        when(mapPlanSaleTargetRepository.findSaleTargetByBrCodeAndTargetLevelAndPlanDate(anyString(), anyInt(), any())).thenReturn(targetInDb);
//        when(targetInDb.isNoData()).thenReturn(false);
//        Locale locale = Locale.US;
//
//        mapPlanSaleTargetService.importPlanSale(targets, listTargetDelete, listTargetOk, null, null, 1, "2023", userToken, locale);
//
//        assertEquals(1, listTargetOk.size());
//        verify(mapPlanSaleTargetRepository, times(1)).findSaleTargetByBrCodeAndTargetLevelAndPlanDate(anyString(), anyInt(), any());
//
//        // ___test br
//        branches = new ArrayList<>();
//        ShopTreeDTO shopTreeDTO1 = new ShopTreeDTO();
//        shopTreeDTO1.setShopId(1002159l);
//        shopTreeDTO1.setName("AMAZONAS BRANCH");
//        shopTreeDTO1.setCode("AMABR");
//        branches.add(shopTreeDTO1);
//
//        mapPlanSaleTarget1.setBrCode(null);
//        mapPlanSaleTarget1.setBcCode("AMABC01");
//        listTargetDelete =  new ArrayList<>();
//        listTargetOk = new ArrayList<>();
//        List<ShopTreeDTO> bcList = new ArrayList<>();
//        ShopTreeDTO shopTreeDTO2 = new ShopTreeDTO();
//        shopTreeDTO2.setShopId(101197604l);
//        shopTreeDTO2.setName("BAGUA CENTER");
//        shopTreeDTO2.setCode("AMABC01");
//        bcList.add(shopTreeDTO2);
//
//        MapPlanSaleTarget tBc = new MapPlanSaleTarget();
//        tBc.setId(14133l);
//        tBc.setRowIndex(0);
//        tBc.setBrCode("AMABR");
//        tBc.setBcCode("AMABC01");
//        tBc.setT11Target(4);
//        when(shopTreeService.listShopTree(branches.get(0).getShopId(), 4, null, null)).thenReturn(bcList);
//        when(mapPlanSaleTargetRepository.findSaleTargetByBcCodeAndTargetLevelAndPlanDate(anyString(), anyInt(), any())).thenReturn(tBc);
//        mapPlanSaleTargetService.importPlanSale(targets, listTargetDelete, listTargetOk, "AMABR", null, 2, "2023", userToken, locale);
//        assertEquals(1, listTargetOk.size());
//
//        //__test bc
//        mapPlanSaleTarget1.setBrCode(null);
//        mapPlanSaleTarget1.setBcCode(null);
//        mapPlanSaleTarget1.setStaffCode("AMADD01132");
//        listTargetDelete =  new ArrayList<>();
//        listTargetOk = new ArrayList<>();
//        bcList = new ArrayList<>();
//        ShopTreeDTO shopTreeDTO3 = new ShopTreeDTO();
//        shopTreeDTO3.setShopId(1002159l);
//        shopTreeDTO3.setName("AMAZONAS BRANCH");
//        shopTreeDTO3.setCode("AMABR");
//        bcList.add(shopTreeDTO3);
//        when(shopTreeService.listShopTree(branches.get(0).getShopId(), 4, null, null)).thenReturn(bcList);
//
//        MapPlanSaleTarget targetBc = new MapPlanSaleTarget();
//        targetBc.setId(14135l);
//        targetBc.setRowIndex(0);
//        targetBc.setTotalTarget(9);
//        when(mapPlanSaleTargetRepository.findSaleTargetByBcCodeAndTargetLevelAndPlanDate(anyString(), anyInt(), any())).thenReturn(targetBc);
//
//        List<StaffBaseDto> listStaffs = new ArrayList<>();
//        StaffBaseDto staffBaseDto = new StaffBaseDto();
//        staffBaseDto.setId(108011187l);
//        staffBaseDto.setCode("AMADD01132");
//        staffBaseDto.setShopId(101197604l);
//        listStaffs.add(staffBaseDto);
//        when(staffService.getListEmployeeOfShop(anyLong(), any(), any(), any(), any())).thenReturn(listStaffs);
//
//        MapPlanSaleTarget tStaff = new MapPlanSaleTarget();
//        tStaff.setId(14152l);
//        tStaff.setBrCode("AMABR");
//        tStaff.setBcCode("AMABR");
//        tStaff.setStaffCode("AMADD01132");
//        when(mapPlanSaleTargetRepository.findSaleTargetByBcCodeAndStaffCodeAndTargetLevelAndPlanDate(anyString(), anyString(), anyInt(), any())).thenReturn(tStaff);
//        mapPlanSaleTargetService.importPlanSale(targets, listTargetDelete, listTargetOk, "AMABR", "AMABR", 3, "2023", userToken, locale);
//        assertEquals(1, listTargetOk.size());
//        verify(staffService, times(5)).getListEmployeeOfShop(anyLong(), any(), any(), any(), any());
//        verify(mapPlanSaleTargetRepository, times(1)).findSaleTargetByBcCodeAndStaffCodeAndTargetLevelAndPlanDate(anyString(), anyString(), anyInt(), any());
//    }
//
//    @Test
//    public void test_search_shouldReturnSuccess(){
//        PlanSaleSearchRequest request = mock(PlanSaleSearchRequest.class);
//        List<PlanSaleTargetDto> list = new ArrayList<>();
////        when(mapPlanSaleTargetRepository.search(any(), any(), any(), any(), any(), any())).thenReturn(list);
//        assertEquals(0, list.size());
//        PlanSaleTargetDto planSaleTargetDto = mock(PlanSaleTargetDto.class);
//        list.add(planSaleTargetDto);
////        when(mapPlanSaleTargetRepository.search(any(), any(), any(), any(), any(), any())).thenReturn(list);
//        assertEquals(1, list.size());
//    }
//}
