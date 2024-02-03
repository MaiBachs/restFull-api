package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.VisitPlanBean;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.SearchVisitPlanRequest;
import com.spm.viettel.msm.dto.response.SearchVisitPlanResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.repository.sm.entity.VisitPlanMap;
import com.spm.viettel.msm.service.VisitPlanService;
import com.spm.viettel.msm.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author HieuND
 */
@RestController
@RequestMapping("/api/visit-plan")
public class VisitPlanController extends BaseController {

    private final Logger loggerFactory = LoggerFactory.getLogger(VisitPlanController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private VisitPlanService visitPlanService;
    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping(path = "/search")
    public ResponseEntity<?> getListParamByType(@RequestBody SearchVisitPlanRequest request) throws ParseException {
        return responseFactory.success(visitPlanService.searchVisitPlan(request));
    }

    @Operation(summary = "import data visitPlan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Import file"),
            @ApiResponse(responseCode = "400", description = "invalid request review parameters"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping(value = "/import", consumes = {
            "multipart/form-data"
    })
    public ResponseEntity<?> importVisitPlan(@RequestParam("file") MultipartFile file,
                                             @RequestParam("actionType") ActionChannelType actionType) {
        String fileImportResultName = null;
        String errorMessageImport;
        List<VisitPlanMap> visitPlansImported = null;
        try {
            if (file.isEmpty()) {
                return responseFactory.error(HttpStatus.BAD_REQUEST, ResponseStatusEnum.FIELD_EMPTY);
            }

            if (!visitPlanService.isExcelFile(file.getOriginalFilename())) {
                return responseFactory.error(HttpStatus.BAD_REQUEST, ResponseStatusEnum.FIELD_FORMAT);
            }

            if (file.getSize() > Constants.FILE_MAX_SIZE) {
                return responseFactory.error(HttpStatus.BAD_REQUEST, ResponseStatusEnum.FIELD_TOO_LARGE);
            }

            File excelFile = visitPlanService.convertMultipartFileToFile(file);
            List<VisitPlanMap> visitPlanMaps = visitPlanService.readExcelFile(excelFile,actionType);
            visitPlansImported = visitPlanService.processExcelData(visitPlanMaps, actionType);
            excelFile.delete();

            if (!visitPlansImported.isEmpty() && visitPlansImported.size() == visitPlansImported.size()) {
                String[] importedArgs = {new StringBuilder().append(visitPlanMaps.size()).toString()};
                errorMessageImport =  messageSource.getMessage("visit_plan.import.success_message", importedArgs, locale);
            } else {
                String[] importedArgs = {new StringBuilder().append(visitPlansImported.size()).toString(),
                        new StringBuilder().append(visitPlanMaps.size()).toString()};
                errorMessageImport =  messageSource.getMessage("visit_plan.import.error_message", importedArgs, locale);
                fileImportResultName = visitPlanService.buildImportResultFile(visitPlanMaps);
            }

        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
            return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.GENERAL_ERROR);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("errorMessageImport", errorMessageImport);
        response.put("fileImportResultName", fileImportResultName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/download-result")
    public ResponseEntity<?> downLoadFile(@RequestBody SearchVisitPlanRequest request) throws ParseException {
        File fileToDownload = new File(visitPlanService.buildSearchVisitPlanResultFile(request));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "visit_plan_search_result.xls";
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
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/download-result-by-month")
    public ResponseEntity<?> downLoadFileByMonth(@RequestBody SearchVisitPlanRequest request) throws ParseException {
        List<VisitPlanBean> response = visitPlanService.getDataExportVisitPlanByMonth(request);
        File fileToDownload = new File(visitPlanService.buildFileExportVisitPlanByMonth(response, request.getExportMonth()));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "visit_channel_detail_report.xls";
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
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/delete-visit-plan")
    public ResponseEntity<?> deleteVisitPlan(@RequestBody SearchVisitPlanRequest request) {
        return visitPlanService.deleteVisitPlan(request);
    }
}
