package com.spm.viettel.msm.controller;

//<editor-fold defaultstate="collapsed" desc="thư viện import">
import com.poiji.exception.PoijiException;
import com.spm.viettel.msm.Constant;
import com.spm.viettel.msm.dto.ChannelTypeDTO;
import com.spm.viettel.msm.dto.PlanChannelSale;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.EvaluationConfig;
import com.spm.viettel.msm.dto.request.MapCheckListRequest;
import com.spm.viettel.msm.dto.request.SearchEvaluationRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.dto.response.ResultEvaluationResp;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.repository.smartphone.entity.Job;
import com.spm.viettel.msm.repository.smartphone.entity.MapChannelTypeCheckList;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.EvaluationService;
import com.spm.viettel.msm.utils.DateUtil;
import com.spm.viettel.msm.utils.FileUtils;
import com.spm.viettel.msm.utils.MessageKey;
import freemarker.template.TemplateException;
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
import java.text.ParseException;
import java.util.*;
//</editor-fold>

@RestController
@RequestMapping("/api/evaluation-config")
public class EvaluationController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(EvaluationController.class);
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final EvaluationService evaluationService;
    private final AuthenticateService authenticateService;
    private final MessageSource messageSource;


    @Autowired
    public EvaluationController(
            HttpServletRequest request,
            ResponseFactory responseFactory,
            EvaluationService evaluationService,
            AuthenticateService authenticateService,
            MessageSource messageSource
    ) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.evaluationService = evaluationService;
        this.authenticateService = authenticateService;
        this.messageSource = messageSource;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String templateFolder;

    @Operation(summary = "import data evaluation")
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
    public ResponseEntity<?> importConfigEvaluation(@RequestParam("file") MultipartFile fileImport, @RequestParam("action") ActionChannelType actionType){
        Locale locale = LocaleContextHolder.getLocale();
        UserTokenDto userInformation = authenticateService.getUserInformation(getUserLogined());
        //<editor-fold defaultstate="collapsed" desc="đọc file và thực thi yêu cầu">
        try {
            List<MapChannelTypeCheckList> mapChannelTypeCheckLists = new ArrayList<>();
            File dirUpload = new File(templateFolder);
            if (!dirUpload.exists()) {
                dirUpload.mkdir();
            }
            String currentDate = com.spm.viettel.msm.utils.DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date());
            String fileName = currentDate + "_" + FileUtils.getSafeFileName(fileImport.getOriginalFilename());
            File excelFile = FileUtils.convertMultipartFileToFile(fileImport);
            FileUtils.saveFile(excelFile, fileName, dirUpload);
            File file = new File(dirUpload + File.separator + fileName);
            List<EvaluationConfig> listDataExcel = convertToExcel(file, EvaluationConfig.class);
            List<MapChannelTypeCheckList> allEvaluationConfig = evaluationService.getAllEvaluationConfig();
            if (file != null) {
                file.delete();
            }
            List<EvaluationConfig> listEvaluationCheckNoDatabase = null;
            switch (actionType){
                case ADD:
                    listEvaluationCheckNoDatabase = evaluationService.executeFileImport(listDataExcel);
                    if(listEvaluationCheckNoDatabase.size() > 0){
                        List<EvaluationConfig> listEvaluationConfigNoComment = new ArrayList<>();
                        for (EvaluationConfig evaluationConfig : listEvaluationCheckNoDatabase) {
                            if(evaluationConfig.getComment() == null || evaluationConfig.getComment().equals("") || evaluationConfig.getComment().isEmpty()){
                                listEvaluationConfigNoComment.add(evaluationConfig);
                            }
                        }
                        if(listEvaluationConfigNoComment.size() > 0){
                            List<MapChannelTypeCheckList> checkList = evaluationService.processExcelData(listEvaluationConfigNoComment,userInformation,actionType,allEvaluationConfig);
                            if(checkList != null){
                                mapChannelTypeCheckLists.addAll(checkList);
                            }
                        }
                    }
                    break;
                case DELETE:
                    listEvaluationCheckNoDatabase = evaluationService.executeFileImportDelete(listDataExcel); // lấy ra danh sách bản ghi đã được valiadate ngoài Database
                    List<EvaluationConfig> listEvaluationCheckInDatabase = evaluationService.checkEvaluationConfigInDatabase(listEvaluationCheckNoDatabase,allEvaluationConfig);// lấy ra danh sách bản ghi đã được valiadate trong Database
                    List<EvaluationConfig> listEvaluationConfigNoComment = new ArrayList<>(); //  biến để lưu danh sách các bản ghi không có comment
                    for (EvaluationConfig evaluationConfig : listEvaluationCheckInDatabase) {
                        if(evaluationConfig.getComment() == null || evaluationConfig.getComment().equals("") || evaluationConfig.getComment().isEmpty()){
                            listEvaluationConfigNoComment.add(evaluationConfig);
                        }
                    }
                    if(listEvaluationConfigNoComment.size() > 0){
                        List<MapChannelTypeCheckList> checkList = evaluationService.processExcelData(listEvaluationConfigNoComment,userInformation,actionType,null);
                        if(checkList != null){
                            mapChannelTypeCheckLists.addAll(checkList);
                        }
                    }
            }
            String[] args = { String.valueOf(mapChannelTypeCheckLists.size()), String.valueOf(listDataExcel.size()) };
            String errorMessageImport;
            if(actionType == ActionChannelType.ADD){
                errorMessageImport = messageSource.getMessage(MessageKey.SURVEY_IMPORT_RESULT, args, locale);
            }else {
                errorMessageImport = messageSource.getMessage(MessageKey.REMOVE_RESULT, args, locale);
            }
            if (mapChannelTypeCheckLists.size() < listDataExcel.size()) {
                return responseFactory.error(HttpStatus.valueOf(200), buildImportResultFile(listEvaluationCheckNoDatabase, "Template_import_evaluation_result_error.xls", "import_evaluation_result_","xls"),
                        errorMessageImport);
            }else{
                return responseFactory.success(args);
            }
        } catch (Exception e){
            if (e instanceof PoijiException) {
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_FILE_INVALID, null, locale)));
            } else if (e instanceof JDBCConnectionException) {
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(Constant.PLAN_CHANNEL_IMPORT_SQL_CONNECTION_ERROR, null, locale)));
            } else {
                loggerFactory.error(e.getMessage());
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(e.getMessage(), null, locale)));
            }
        }
        //</editor-fold>
    }

    @GetMapping("/channel-types")
    public ResponseEntity<List<ChannelTypeDTO>> getChannelTypeToPlan(){
        List<ChannelTypeDTO> channelTypeToPlanList = evaluationService.getListChannelTypeToPlan();
        return new ResponseEntity<>(channelTypeToPlanList,HttpStatus.OK);
    }

    @GetMapping("/get-evaluation-audit")
    public ResponseEntity<List<Job>> getJobCCAudit() {
        List<Job> jobs = evaluationService.findJobsByAudit();
        return new ResponseEntity<>(jobs,HttpStatus.OK);
    }

    @PostMapping("search")
    public ResponseEntity<ResultEvaluationResp> searchEvaluationResult(@RequestBody SearchEvaluationRequest request) throws TemplateException, IOException {
        ResultEvaluationResp dataResponse =  evaluationService.searchEvaluationConfig(request);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("details/id={evaluationId}")
    public ResponseEntity<MapChannelTypeCheckList> getDetailsEvaluationConfig(@PathVariable("evaluationId") Long evaluationId){
        MapChannelTypeCheckList detailData = evaluationService.getEvaluationDetails(evaluationId);
        return new ResponseEntity(detailData, HttpStatus.OK);
    }

    @GetMapping("/download-file")
    public ResponseEntity<?> exportEvaluationConfig(@RequestParam(value = "branchId",required = false) Long branchId,
                                                    @RequestParam(value = "ChannelTypeId",required = false) Long channelTypeId,
                                                    @RequestParam(value = "jobId", required = false) Long jobId,
                                                    @RequestParam(value = "fromDate",required = false) String fromDate,
                                                    @RequestParam(value = "toDate",required = false) String toDate,
                                                    @RequestParam(value = "currentPage",required = false) Integer currentPage) {
        SearchEvaluationRequest req = new SearchEvaluationRequest(branchId, channelTypeId, jobId, fromDate, toDate,currentPage);
        String excelFilePath = evaluationService.evaluationSearchResultExport(req);
        //<editor-fold defaultstate="collapsed" desc="download file kết quả">
        if (excelFilePath != null) {
            try {
                InputStream inputStream = new FileInputStream(excelFilePath);
                String fileName = "evaluation_result.xls";
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

    @PostMapping("/edit")
    public ResponseEntity<?> updateEvaluationConfig(@RequestBody MapCheckListRequest request) throws ParseException {
        UserTokenDto userInformation = authenticateService.getUserInformation(getUserLogined());
        return new ResponseEntity<>(evaluationService.updateEvaluationConfig(request,userInformation),HttpStatus.OK);
    }
}
