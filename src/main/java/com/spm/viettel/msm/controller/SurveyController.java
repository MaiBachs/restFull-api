package com.spm.viettel.msm.controller;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiException;
import com.poiji.option.PoijiOptions;
import com.spm.viettel.msm.dto.SurveyConfig;
import com.spm.viettel.msm.dto.request.SearchSurveyRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.entity.MapChannelSurveyStaff;
import com.spm.viettel.msm.service.MapChannelSurveyStaffService;
import com.spm.viettel.msm.service.VisitPlanService;
import com.spm.viettel.msm.utils.DateUtil;
import com.spm.viettel.msm.utils.FileUtils;
import com.spm.viettel.msm.utils.MessageKey;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/survey")
public class SurveyController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(ItemConfigController.class);
    @Autowired
    private MapChannelSurveyStaffService mapChannelSurveyStaffService;
    @Autowired
    private ResponseFactory responseFactory;
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String templateFolder;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private VisitPlanService visitPlanService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping("/search")
    public ResponseEntity<GeneralResponse<List<MapChannelSurveyStaff>>> searchSurveyConfig(SearchSurveyRequest request){
        List<MapChannelSurveyStaff> mapChannelSurveyStaffs = mapChannelSurveyStaffService.searchSurveyConfig(request);
        return responseFactory.success(mapChannelSurveyStaffs);
    }

    @Operation(summary = "import data surveyac")
    @PostMapping(value = "/pvd-import-surveyac",consumes = {
            "multipart/form-data"
    })
    public ResponseEntity<?> pvdImportSurveyac(@RequestParam("file") MultipartFile fileInput){
        ResponseEntity<GeneralResponse<List<SurveyConfig>>> response = null;
        try {
            File dirUpload = new File(templateFolder);
            if (!dirUpload.exists()) {
                dirUpload.mkdir();

            }
            String currentDate = DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date());
            String fileName = currentDate + "_" + FileUtils.getSafeFileName(fileInput.getOriginalFilename());
            File fileConvert = visitPlanService.convertMultipartFileToFile(fileInput);
            FileUtils.saveFile(fileConvert, fileName, dirUpload);
            File file = new File(dirUpload + File.separator + fileName);
            if (fileConvert != null) {
                fileConvert.delete();
            }
            PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().headerStart(0).addListDelimiter(";").build();
            List<SurveyConfig> surveyConfigData = Poiji.fromExcel(file, SurveyConfig.class, options);
            List<MapChannelSurveyStaff> surveyStaffs = new ArrayList<>();
            mapChannelSurveyStaffService.pvdImportSurveyac(surveyConfigData, surveyStaffs, locale);

            String resultImport = surveyStaffs.size()+"/"+surveyConfigData.size();
            if (surveyStaffs.size() < surveyConfigData.size()) {
                response = responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),buildImportResultFile(surveyConfigData,"Template_import_survey_result_docs2.xls","import_survey_result_", ".xls"), resultImport);
            }else {
                response = responseFactory.success();
            }
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
            if (e instanceof PoijiException) {
                response = responseFactory.error(HttpStatus.NOT_ACCEPTABLE, String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), MessageKey.ITEM_IMPORT_IMPORT_FILE_INVALID);
            } else if (e instanceof JDBCConnectionException) {
                response = responseFactory.error(HttpStatus.REQUEST_TIMEOUT, String.valueOf(HttpStatus.REQUEST_TIMEOUT.value()), MessageKey.ITEM_IMPORT_SQL_CONNECTION_ERROR);
            } else {
                response = responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
            }
        }finally {
            return response;
        }
    }
}
