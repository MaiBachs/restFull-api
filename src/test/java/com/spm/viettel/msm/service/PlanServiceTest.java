//package com.spm.viettel.msm.service;
//
//import com.spm.viettel.msm.dto.PlanChannelSale;
//import com.spm.viettel.msm.dto.ShopTreeDTO;
//import com.spm.viettel.msm.dto.UserTokenDto;
//import com.spm.viettel.msm.dto.request.ImportPlanChannelTargetRequest;
//import com.spm.viettel.msm.dto.response.PlanChannelTargetResponse;
//import com.spm.viettel.msm.factory.ResponseFactory;
//import com.spm.viettel.msm.repository.sm.MapPlanChannelTargetRepository;
//import com.spm.viettel.msm.repository.sm.StaffRepository;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.MessageSource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.TestPropertySource;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
//@RunWith(MockitoJUnitRunner.class)
//@TestPropertySource(properties = {
//        "TEMPLATE_PATH=C:/Users/ADMIN/Desktop/excel_check_list",
//        "FILE_TEMP_UPLOAD_PATH=C:/Users/ADMIN/Desktop/excel_result"
//})
//public class PlanServiceTest {
//
//    @InjectMocks
//    private PlanChannelService planChannelService;
//
//    @Mock
//    private ShopService shopService;
//
//    @Mock
//    private StaffRepository staffRepository;
//
//    @Mock
//    private ChannelTypeService channelTypeService;
//
//    @Mock
//    private MapPlanChannelTargetRepository mapPlanChannelTargetRepository;
//
//    @Mock
//    private MessageSource messageSource;
//
////    @Mock
//    @Value("${TEMPLATE_PATH}")
//    private String templateFolder;
//
////    @Mock
//    @Value("${FILE_TEMP_UPLOAD_PATH}")
//    private String fileNameFullFolder;
//
//    @Mock
//    private ResponseFactory responseFactory;
//
//
//    @Before
//    public void setUp() {
////        MockitoAnnotations.initMocks(this);
//        when(responseFactory.error(any(HttpStatus.class),any(String.class), any())).thenCallRealMethod();
//    }
//
//    @Test
//    public void testExecuteImportPlanChannel() throws ParseException {
//        List<PlanChannelSale> targets = new ArrayList<>();
//        PlanChannelSale target = mock(PlanChannelSale.class);
//        targets.add(target);
//        ImportPlanChannelTargetRequest request = mock(ImportPlanChannelTargetRequest.class);
////        when(request.getPlanDate()).thenReturn("2023");
//        List<ShopTreeDTO> shopTreeList = new ArrayList<>();
//        ShopTreeDTO shopTree = mock(ShopTreeDTO.class);
//        shopTreeList.add(shopTree);
////        when(shopService.listShopTree(anyLong(), anyInt(), anyLong(), anyLong())).thenReturn(shopTreeList);
////        when(staffRepository.getListEmployeeOfShop(anyLong(), anyInt(), isNull(),isNull(), isNull())).thenReturn(new ArrayList<>());
////        when(channelTypeService.getListChannelCanPlanToDevelop()).thenReturn(new ArrayList<>());
////        when(messageSource.getMessage(anyString(),any(),any())).thenReturn("abc");
////        ResponseEntity<?> response = planChannelService.executeImportPlanChannel(file,request, null);
////
////        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
//////        verify(shopService, times(4)).listShopTree(anyLong(), anyInt(), anyLong(), anyLong());
//
//    }
//}
