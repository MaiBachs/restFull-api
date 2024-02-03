package com.spm.viettel.msm.controller;

import com.poiji.exception.PoijiException;
import com.spm.viettel.msm.Constant;
import com.spm.viettel.msm.dto.TemplateUpdateLoationDto;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.GetChannelCodeDto;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.LocationManagerService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/Location-manager")
public class LocationManagerController extends BaseController {

    private final Logger loggerFactory = LoggerFactory.getLogger(LocationManagerController.class);
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final MessageSource messageSource;
    private final AuthenticateService authenticateService;
    private final LocationManagerService locationManagerService;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String templateFolder;

    @Autowired
    public LocationManagerController(HttpServletRequest request, ResponseFactory responseFactory, MessageSource messageSource, AuthenticateService authenticateService, LocationManagerService locationManagerService) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.messageSource = messageSource;
        this.authenticateService = authenticateService;
        this.locationManagerService = locationManagerService;
    }


    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Operation(summary = "import update Location")
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
    public ResponseEntity<GeneralResponse<List<TemplateUpdateLoationDto>>> importUpdateLocation(@RequestParam("file") MultipartFile fileImport) {
        Locale locale = LocaleContextHolder.getLocale();
        List<TemplateUpdateLoationDto> dataSuccess = new ArrayList<>();
        List<TemplateUpdateLoationDto> dataNoComment = new ArrayList<>();
        ResponseEntity<GeneralResponse<List<TemplateUpdateLoationDto>>> response;
        try {
            File dirUpload = new File(templateFolder);
            if (!dirUpload.exists()) {
                dirUpload.mkdir();
            }
            String currentDate = com.spm.viettel.msm.utils.DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date());
            String fileName = currentDate + "_" + FileUtils.getSafeFileName(fileImport.getOriginalFilename());
            File excelFile = FileUtils.convertMultipartFileToFile(fileImport);
            FileUtils.saveFile(excelFile, fileName, dirUpload);
            File file = new File(dirUpload + File.separator + fileName);
            List<TemplateUpdateLoationDto> listDataExcel = convertToExcel(file, TemplateUpdateLoationDto.class);
            if (file != null) {
                file.delete();
            }
            List<TemplateUpdateLoationDto> listDataRead = locationManagerService.readContentFile(listDataExcel);
            for (TemplateUpdateLoationDto data : listDataRead) {
                if (data.getComment() == null) {
                    dataNoComment.add(data);
                }
            }
            if (dataNoComment.size() > 0) {
                UserTokenDto userInformation = authenticateService.getUserInformation(getUserLogined());
                List<TemplateUpdateLoationDto> listDataUpdated = locationManagerService.updateLocationsForObjectType(dataNoComment, userInformation);
                dataSuccess.addAll(listDataUpdated);
            }
            String[] args = {String.valueOf(dataSuccess.size()), String.valueOf(listDataExcel.size())};
            String errorMessageImport = messageSource.getMessage(MessageKey.SURVEY_IMPORT_RESULT, args, locale);
            if (dataSuccess.size() < listDataExcel.size()) {
                response = responseFactory.error(HttpStatus.valueOf(200), buildImportResultFile(listDataRead, "template_import_update_location_result.xls", "import_update_location_result_error", "xls"),
                        errorMessageImport);
            } else {
                response = responseFactory.success(listDataExcel);
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
            if (e instanceof PoijiException) {
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_FILE_INVALID, null, locale)));
            } else if (e instanceof JDBCConnectionException) {
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(Constant.PLAN_CHANNEL_IMPORT_SQL_CONNECTION_ERROR, null, locale)));
            } else {
                loggerFactory.error(e.getMessage());
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(e.getMessage(), null, locale)));
            }
        }
        return response;
    }

    @PostMapping("/get-channelCode")
    public ResponseEntity<?> getChannelCode(@RequestBody GetChannelCodeDto request) {
        return new ResponseEntity<>(locationManagerService.getChannelTypeByChannelType(request), HttpStatus.OK);
    }

    @PostMapping("/update-location")
    public ResponseEntity<?> updateLocationObjectType(@RequestBody TemplateUpdateLoationDto req) {
        List<TemplateUpdateLoationDto> listUpdate = new ArrayList<>();
        listUpdate.add(req);
        UserTokenDto userInformation = authenticateService.getUserInformation(getUserLogined());
        List<TemplateUpdateLoationDto> templateUpdateLoationDtos = locationManagerService.updateLocationsForObjectType(listUpdate, userInformation);
        if (templateUpdateLoationDtos.size() > 0) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

}
