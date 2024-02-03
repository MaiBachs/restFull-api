package com.spm.viettel.msm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poiji.exception.PoijiException;
import com.spm.viettel.msm.Constant;
import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import com.spm.viettel.msm.dto.PlanChannelSale;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.request.ImportPlanChannelTargetRequest;
import com.spm.viettel.msm.dto.request.PlanChannelSearchRequest;
import com.spm.viettel.msm.dto.response.PlanChannelResponse;
import com.spm.viettel.msm.dto.response.PlanChannelTargetImportResponse;
import com.spm.viettel.msm.exceptions.NotFoundException;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.factory.ResponseStatusEnum;
import com.spm.viettel.msm.repository.sm.entity.MapPlanChannelTarget;
import com.spm.viettel.msm.service.AuthenticateService;
import com.spm.viettel.msm.service.ChannelTypeService;
import com.spm.viettel.msm.service.PlanChannelCheckListService;
import com.spm.viettel.msm.service.PlanChannelService;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.FileUtils;
import com.spm.viettel.msm.utils.MessageKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@RestController
@RequestMapping("/api/plan-channel")
public class PlanChannelController extends BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(PlanChannelController.class);

    private final HttpServletRequest request;
    private final ChannelTypeService channelTypeService;
    private final ResponseFactory responseFactory;
    private final MessageSource messageSource;
    private final PlanChannelService planChannelService;
    private final PlanChannelCheckListService planChannelCheckListService;
    private final AuthenticateService authenticateService;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String templateFolder;
    @Autowired
    public PlanChannelController(HttpServletRequest request, ChannelTypeService channelTypeService, ResponseFactory responseFactory, MessageSource messageSource, PlanChannelService planChannelService, PlanChannelCheckListService planChannelCheckListService, AuthenticateService authenticateService) {
        this.request = request;
        this.channelTypeService = channelTypeService;
        this.responseFactory = responseFactory;
        this.messageSource = messageSource;
        this.planChannelService = planChannelService;
        this.planChannelCheckListService = planChannelCheckListService;
        this.authenticateService = authenticateService;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Operation(summary = "plan channel checkList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "400", description = "invalid request review parameters"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping("/search")
    public ResponseEntity<?> searchPlanChannel(@RequestBody PlanChannelSearchRequest request) throws ParseException {
        ResponseEntity response = null;
        PlanChannelResponse resultReturn = new PlanChannelResponse();
        List<MapPlanChannelTarget> currentTarget = new ArrayList<>();

        switch (request.getAction()) {
            case LIST:
                resultReturn.setTargetList(planChannelService.searchToResponse(request));
                break;
            case VIEW:
                List<ChannelWithGroupDTO> channelTypes = channelTypeService.getListChannelCanPlanToDevelop();
                Date planDateYear = DateUtils.parseDate(request.getPlanDate(), "YYYY");
                for (ChannelWithGroupDTO c : channelTypes) {
                    if (request.getTargetLevel() != null && request.getTargetLevel() == 1) {
                        List<MapPlanChannelTarget> targets = planChannelService.findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(request.getBrCode(), c.getChannelTypeId(), 1, planDateYear);
                        if (CollectionUtils.isNotEmpty(targets)) {
                            targets.forEach(t -> t.setChannelKey(c.getObjectType()));
                            currentTarget.addAll(targets);
                        }
                    } else if (request.getTargetLevel() != null && request.getTargetLevel() == 2) {
                        List<MapPlanChannelTarget> targets = planChannelService.findTargetByBcCodeAndChannelIdAndTargetLevelAndPlanDate(request.getBcCode(), c.getChannelTypeId(), 2, planDateYear);
                        if (CollectionUtils.isNotEmpty(targets)) {
                            targets.forEach(t -> t.setChannelKey(c.getObjectType()));
                            currentTarget.addAll(targets);
                        }
                    }
                }
                currentTarget.sort(Comparator.comparing(MapPlanChannelTarget::getChannelCode));
                resultReturn.setCurrentTarget(currentTarget);
                break;
            case SET_STATUS:
                String result = planChannelService.setStatus(request);
                if (StringUtils.isNotEmpty(result)) {
                    response = responseFactory.error(HttpStatus.OK, result, messageSource.getMessage(MessageKey.PLAN_CHANNEL_ACTION_TARGET_NOT_FOUND,null,locale));
                }
                break;
            case SET_STATUS_PLAN:
                request.setStaffCode(getUserLogined().getStaffCode());
                planChannelService.setStatusPlan(request);
                break;
            case POINTS_NEAR_CHANNEL:
                resultReturn.setPointNears(planChannelService.listPointsNearChannelPlaned(request));
                break;
            case LIST_MAP_PLAN:
                if (request != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DD_MM_YYYY);
                    LocalDate localDate = LocalDate.parse("01/" + request.getFromDate(), formatter);
                    LocalDate firstDay = localDate.with(TemporalAdjusters.firstDayOfMonth());
                    request.setFromDate(firstDay.format(formatter));
                    LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
                    request.setToDate(lastDay.format(formatter));
                    resultReturn.setChannelPlans(planChannelService.searchPlanChannelDevelop(request));
                }
                break;
            case CHECK_LIST_RESULT_OF_CHANNEL:
                try {
                    resultReturn.setChannelCheckListResults(planChannelCheckListService.getListCheckListOfChannel(request.getId()));
                } catch (NotFoundException e) {
                    response = responseFactory.error(HttpStatus.OK, messageSource.getMessage(MessageKey.PLAN_CHANNEL_NOT_FOUND, null, locale),  messageSource.getMessage(MessageKey.PLAN_CHANNEL_NOT_FOUND, null, locale));
                }
                break;
        }
        response = responseFactory.success(resultReturn);
        return response;
    }

    @PostMapping(value = "/import",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> importPlanChannel(@ModelAttribute ImportPlanChannelTargetRequest request){
        ResponseEntity<?> responseEntity = null;
        try {
            File dirUpload = new File(templateFolder);
            if (!dirUpload.exists()) {
                dirUpload.mkdir();
            }
            String currentDate = com.spm.viettel.msm.utils.DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date());
            String fileName = currentDate + "_" + FileUtils.getSafeFileName(request.getFile().getOriginalFilename());
            File excelFile = FileUtils.convertMultipartFileToFile(request.getFile());
            FileUtils.saveFile(excelFile, fileName, dirUpload);
            File file = new File(dirUpload + File.separator + fileName);
            if (file != null) {
                excelFile.delete();
            }
            UserTokenDto userInformation = authenticateService.getUserInformation(getUserLogined());
            responseEntity = planChannelService.executeImportPlanChannel(file, request, userInformation);
        }catch (Exception e) {
            if (e instanceof PoijiException) {
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_FILE_INVALID, null, locale)));
            } else if (e instanceof JDBCConnectionException) {
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(Constant.PLAN_CHANNEL_IMPORT_SQL_CONNECTION_ERROR, null, locale)));
            } else {
                loggerFactory.error(e.getMessage());
                return responseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusEnum.valueOf(messageSource.getMessage(e.getMessage(), null, locale)));
            }
        }
        return responseEntity;
    }

    @PostMapping("/download-file")
    public ResponseEntity<?> exportPlanChannel(@RequestBody PlanChannelSearchRequest searchRequest){
        String fileTemplate = "";
        String fileToDownload = planChannelService.searchPlan2Export(searchRequest);
        if (fileToDownload != null) {
            try {
                if(searchRequest.getTargetLevel() == 1) {
                    fileTemplate = "Template_channel_target_sale_search_v2.xls";
                }else if (searchRequest.getTargetLevel() == 2 || searchRequest.getTargetLevel() == 3) {
                    fileTemplate = "Template_channel_target_br_search_v2.xls";
                    if (searchRequest.getTargetLevel() == 3) {
                        fileTemplate = "Template_channel_target_bc_search_v2.xls";
                    }
                }
                InputStream inputStream = new FileInputStream(fileToDownload);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileTemplate);
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

    @PostMapping("/export-channel-develop-report")
    public ResponseEntity<?> exportChannelDevelopReport(@RequestBody PlanChannelSearchRequest searchRequest){
        String fileToDownload = planChannelService.exportChannelDevelopReport(searchRequest);
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileToDownload);
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
}
