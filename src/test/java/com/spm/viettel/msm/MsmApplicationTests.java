package com.spm.viettel.msm;

//import com.spm.viettel.msm.controller.AuditorController;
//import com.spm.viettel.msm.dto.UserTokenDto;
//import com.spm.viettel.msm.dto.enums.ActionChannelType;
//import com.spm.viettel.msm.repository.smartphone.AuditorRepository;
//import com.spm.viettel.msm.repository.smartphone.entity.MapAuditorCheckList;
//import com.spm.viettel.msm.service.AuditorService;
//import org.aspectj.lang.annotation.Before;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.StopWatch;
//import org.springframework.web.context.WebApplicationContext;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class MsmApplicationTests {

//	@Autowired
//	@Qualifier("smartphoneSessionFactory")
//	private SessionFactory smartPhoneSessionFactory;
//	@Mock
//	private AuditorService auditorService;
//
//	@InjectMocks
//	private AuditorController auditorController;
//
//	@Mock
//	private AuditorRepository auditorRepository;
//
//	@Autowired
//	private WebApplicationContext context;
//
//	private MockMvc mockMvc;
//
//	@Before("")
//	public void setUp() {
//		MockitoAnnotations.initMocks(this);
//		this.mockMvc = MockMvcBuilders.standaloneSetup(auditorController).build();
//	}
//
//	MapAuditorCheckList RECORD_1 = new MapAuditorCheckList(null,1002172l,null,108112800l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_2 = new MapAuditorCheckList(null,1002159l,null,1067176l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_3 = new MapAuditorCheckList(null,1002172l,null,108112800l,5l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_4 = new MapAuditorCheckList(null,1002172l,null,108112800l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_5 = new MapAuditorCheckList(null,1002172l,null,108112800l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_6 = new MapAuditorCheckList(null,1002172l,null,108112800l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_7 = new MapAuditorCheckList(null,1002172l,null,108112800l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_8 = new MapAuditorCheckList(null,1002172l,null,108112800l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_9 = new MapAuditorCheckList(null,1002172l,null,108112800l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//	MapAuditorCheckList RECORD_10 = new MapAuditorCheckList(null,1002172l,null,108112800l,101052639l,101199761l,new Date(),"THANHNV",new Date(),"THANHNV");
//
//	@Test
//	public void testAuditorActionExecutionTime() {
//		Session smartphoneSession = null;
//		smartphoneSession = smartPhoneSessionFactory.openSession();
//		// Tạo dữ liệu kiểm thử
//		List<MapAuditorCheckList> auditorCheckList = new ArrayList<>();
//		auditorCheckList.add(RECORD_1);
//		auditorCheckList.add(RECORD_2);
//		auditorCheckList.add(RECORD_3);
//		ActionChannelType action = ActionChannelType.ADD;
//		UserTokenDto userProfile = new UserTokenDto(); // Điền thông tin userProfile của bạn ở đây
//
//		// Khởi tạo StopWatch để đo thời gian
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//
//		// Gọi hàm cần đo thời gian ở đây
//		auditorService.auditorAction(auditorCheckList, action, userProfile);
//
//		// Dừng đo thời gian
//		stopWatch.stop();
//
//		// Lấy thời gian chạy
//		long executionTime = stopWatch.getTotalTimeMillis();
//
//		System.out.println("Thời gian chạy của hàm auditorAction: " + executionTime + " ms");
//	}
//
////	@Test
////	public void testImportConfigAuditor() throws Exception {
////		// Đường dẫn tới file cần đưa vào test
////		String filePath = "C:/Users/ADMIN/Desktop/excel_check_list/template_import_auditor.xls"; // Thay đổi đường dẫn và tên file thực tế
////
////		// Đọc nội dung file từ đường dẫn
////		File fileToUpload = new File(filePath);
////		byte[] fileContent = FileUtils.readFileToByteArray(fileToUpload);
////
////		// Tạo một MockMultipartFile từ nội dung file
////		MockMultipartFile file = new MockMultipartFile(
////				"file",
////				fileToUpload.getName(),
////				MediaType.MULTIPART_FORM_DATA_VALUE,
////				fileContent
////		);
////
////		// Thực hiện request POST đến endpoint /import
////		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/auditor-config/import")
////						.file(file)
////						.param("action", "ADD")) // Điền tham số actionType tùy thuộc vào test case
////				.andExpect(status().isOk()); // Kiểm tra kết quả trả về
////
////		// Thêm các kiểm tra khác tùy theo logic của phương thức importConfigAuditor
////	}
}