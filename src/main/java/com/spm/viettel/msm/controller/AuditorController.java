package com.spm.viettel.msm.controller;

import com.poiji.exception.PoijiException;
import com.spm.viettel.msm.Constant;
import com.spm.viettel.msm.dto.AuditorCheckListDTO;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.*;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.dto.response.ResultAuditorResponse;
import com.spm.viettel.msm.exceptions.BadRequestValidationException;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.repository.smartphone.entity.MapAuditorCheckList;
import com.spm.viettel.msm.repository.smartphone.entity.StaffSmartPhone;
import com.spm.viettel.msm.service.AuditorService;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.ShopService;
import com.spm.viettel.msm.service.StaffService;
import com.spm.viettel.msm.utils.FileUtils;
import com.spm.viettel.msm.utils.MessageKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auditor-config")
public class AuditorController extends BaseController {

    private final Logger loggerFactory = LoggerFactory.getLogger(EvaluationController.class);
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final AuthenticateService authenticateService;
    private final AuditorService auditorService;
    private final ShopService shopService;
    private final StaffService staffService;

    @Autowired
    public AuditorController(
            HttpServletRequest request,
            ResponseFactory responseFactory,
            AuthenticateService authenticateService,
            AuditorService auditorService,
            ShopService shopService, StaffService staffService) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.authenticateService = authenticateService;
        this.auditorService = auditorService;
        this.shopService = shopService;
        this.staffService = staffService;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String templateFolder;

    @Autowired
    private MessageSource messageSource;

    @Operation(summary = "import data auditor config")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Import file"),
            @ApiResponse(responseCode = "400", description = "invalid request review parameters"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping(value = "/import",consumes = {
            "multipart/form-data"
    })
    public ResponseEntity<?> importConfigAuditor(@RequestParam("file") MultipartFile fileImport, @RequestParam("action") ActionChannelType actionType) throws IOException {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            UserTokenDto userInformation = authenticateService.getUserInformation(getUserLogined());
            File dirUpload = new File(templateFolder);
            if (!dirUpload.exists()) {
                dirUpload.mkdir();
            }
            String currentDate = com.spm.viettel.msm.utils.DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date());
            String fileName = currentDate + "_" + FileUtils.getSafeFileName(fileImport.getOriginalFilename());
            File excelFile = FileUtils.convertMultipartFileToFile(fileImport);
            FileUtils.saveFile(excelFile, fileName, dirUpload);
            File file = new File(dirUpload + File.separator + fileName);
            List<AuditorConfig> dataExcels = convertToExcel(file, AuditorConfig.class);
            if (file != null) {
                file.delete();
            }
            List<AuditorConfig> listAuditorConfigChecked = auditorService.executeFileImport(dataExcels);
            switch (actionType){
                case ADD:
                case DELETE:
                    List<AuditorConfig> auditorConfigNoComments = new ArrayList<AuditorConfig>();
                    for (AuditorConfig auditorConfigNoComment : listAuditorConfigChecked) {
                        if(auditorConfigNoComment.getComment() == null || auditorConfigNoComment.getComment().equals("") || auditorConfigNoComment.getComment().isEmpty()){
                            auditorConfigNoComments.add(auditorConfigNoComment);
                        }
                    }
                    List<MapAuditorCheckList> auditorImport = auditorService.processExcelData(auditorConfigNoComments,userInformation,actionType);
                    String[] args = { String.valueOf(auditorImport.size()), String.valueOf(dataExcels.size()) };
                    String errorMessageImport = messageSource.getMessage(MessageKey.SURVEY_IMPORT_RESULT, args, locale);
                    if (auditorImport.size() == dataExcels.size()) {
                        return responseFactory.success(args);
                    }else{
                        return responseFactory.error(HttpStatus.valueOf(200), buildImportResultFile(dataExcels, "template_import_auditor_result.xls", "import_auditor_result_error","xls"),
                                errorMessageImport);
                    }
            }
        }catch (Exception e){
            if (e instanceof PoijiException) {
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_FILE_INVALID, null, locale)));
            } else if (e instanceof JDBCConnectionException) {
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(Constant.PLAN_CHANNEL_IMPORT_SQL_CONNECTION_ERROR, null, locale)));
            } else {
                loggerFactory.error(e.getMessage());
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(e.getMessage(), null, locale)));
            }
        }
        return responseFactory.success();
    }

    @GetMapping("/download-file")
    public ResponseEntity<?> export(@RequestParam(value = "branchId",required = false) Long branchId,
                                    @RequestParam(value = "ChannelTypeId",required = false) Long channelTypeId,
                                    @RequestParam(value = "auditorId", required = false) Long auditorId,
                                    @RequestParam(value = "shopChannelId", required = false) Long shopChannelId,
                                    @RequestParam(value = "fromDate",required = false) String fromDate,
                                    @RequestParam(value = "toDate",required = false) String toDate,
                                    @RequestParam(value = "currentPage",required = false) Integer currentPage){
        String excelFilePath = auditorService.auditorSearchResultExport(new SearchAuditorRequest(branchId, channelTypeId, auditorId,shopChannelId, fromDate, toDate, currentPage));
        if (excelFilePath != null) {
            try {
                InputStream inputStream = new FileInputStream(excelFilePath);
                String fileName = "auditors_export_result.xls";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileName);
                InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(inputStreamResource);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        //</editor-fold>
        return ResponseEntity.notFound().build();
    }

    @PostMapping("search")
    public ResponseEntity<ResultAuditorResponse> searchAuditorResult(@RequestBody SearchAuditorRequest request){
        ResultAuditorResponse result =  auditorService.searchAuditorConfig(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/id={id}")
    public ResponseEntity<List<AuditorCheckListDTO>> detailsAuditorConfig(@PathVariable("id") Long id){
        List<AuditorCheckListDTO> result = auditorService.getAuditorDetails(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/staff-auditor")
    public List<StaffSmartPhone> staffAuditor(){
        return staffService.getStaffAuditorInSmartPhones();
    }

    @PostMapping("/update-auditor")
    public ResponseEntity<?> updateAuditorConfig(@RequestBody List<AuditorUpdateRequest> requests){
        try {
            UserTokenDto userInformation = authenticateService.getUserInformation(getUserLogined());
            boolean isEditResult = auditorService.editAuditor(requests, userInformation);
            return responseFactory.success(isEditResult);
        }catch (BadRequestValidationException e){
            loggerFactory.error(e.getMessage(), e);
            return responseFactory.error(HttpStatus.BAD_REQUEST,e.getErrorStatus().getCode(), messageSource.getMessage(e.getErrorStatus().getCode(), null, locale));
        }catch (Exception e){
            loggerFactory.error(e.getMessage(), e);
            return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.GENERAL_ERROR);
        }
    }

    @PostMapping("/fill-channelCode")
    public ResponseEntity<?> fillChannelCode(@RequestBody getShopRequest request){
        if(request.getShopId() == null){
            return responseFactory.error(HttpStatus.BAD_REQUEST,ResponseStatusEnum.FIELD_EMPTY,messageSource.getMessage(MessageKey.REQUEST_INVALID, null, locale));
        } else if (request.getChannelTypeId() == null){
            return responseFactory.error(HttpStatus.BAD_REQUEST,ResponseStatusEnum.FIELD_EMPTY,messageSource.getMessage(MessageKey.REQUEST_INVALID, null, locale));
        }else {
            return responseFactory.success(shopService.getChannelcodeInChannelTypeIdByBranch(request.getShopId(), request.getChannelTypeId()));
        }
    }
}
