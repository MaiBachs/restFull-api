package com.spm.viettel.msm.controller;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiException;
import com.poiji.option.PoijiOptions;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.PlanSaleSearchRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.dto.response.ListMapPlanResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.entity.MapPlanSaleTarget;
import com.spm.viettel.msm.service.*;
import com.spm.viettel.msm.utils.DateUtil;
import com.spm.viettel.msm.utils.FileUtils;
import com.spm.viettel.msm.utils.MessageKey;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plan-sale")
public class PlanSaleController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(ItemConfigController.class);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MapPlanSaleTargetService mapPlanSaleTargetService;
    @Autowired
    private VisitPlanService visitPlanService;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private MapPlanSaleResultService mapPlanSaleResultService;
    @Autowired
    private MapPlanSaleService mapPlanSaleService;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String templateFolder;

    @PostMapping(path = "/search")
    public ResponseEntity<List<PlanSaleTargetDto>> search(@RequestBody PlanSaleSearchRequest request){
        List<PlanSaleTargetDto> planSaleTargetDtos = new ArrayList<>();
        try{
            planSaleTargetDtos = mapPlanSaleTargetService.search(request);
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return new ResponseEntity<>(planSaleTargetDtos, HttpStatus.OK);
    }

    @GetMapping(path = "/download-search")
    public ResponseEntity<?> downloadSearch(@RequestParam(value = "brCode",required = false) String brCode
            , @RequestParam(value = "bcCode",required = false) String bcCode
            , @RequestParam(value = "staffCode",required = false) String staffCode
            , @RequestParam(value = "planDate",required = true) String planDate
            , @RequestParam(value = "targetLevel",required = true) Integer targetLevel){
        PlanSaleSearchRequest request = new PlanSaleSearchRequest();
        request.setBrCode(brCode);
        request.setBcCode(bcCode);
        request.setStaffCode(staffCode);
        request.setPlanDate(planDate);
        request.setTargetLevel(targetLevel);
        File fileToDownload = new File(mapPlanSaleTargetService.downloadSearch(request));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "template_plan_sale_search_result.xls";
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

    @GetMapping(path = "/download-search-detail")
    public ResponseEntity<?> downloadSearchDetail(@RequestParam(value = "brCode",required = false) String brCode
            , @RequestParam(value = "bcCode",required = false) String bcCode
            , @RequestParam(value = "planDate",required = true) String planDate){
        PlanSaleSearchRequest request = new PlanSaleSearchRequest();
        request.setBrCode(brCode);
        request.setBcCode(bcCode);
        request.setPlanDate(planDate);

        File fileToDownload = new File(mapPlanSaleTargetService.downloadSearchDetail(request));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "template_plan_sale_search_result.xls";
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

    @Operation(summary = "import plan sale")
    @PostMapping(path = "/import",consumes = {
            "multipart/form-data"
    })
    public ResponseEntity<GeneralResponse<List<MapPlanSaleTarget>>> importPlanSale(
            @RequestParam(value = "file",required = false) MultipartFile fileInput
            , @RequestParam(value = "brCode",required = false) String brCode
            , @RequestParam(value = "bcCode",required = false) String bcCode
            , @RequestParam(value = "targetLevel",required = true) Integer targetLevel
            , @RequestParam(value = "planDate",required = true) String planDate){
        ResponseEntity<GeneralResponse<List<MapPlanSaleTarget>>> response = null;
        UserTokenDto userToken = authenticateService.getUserInformation(getUserLogined());
        try{
            File dirUpload = new File(templateFolder);
            if (!dirUpload.exists()) {
                dirUpload.mkdir();
            }
            String currentDate = DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date());
            String fileName = currentDate + "_"  + FileUtils.getSafeFileName(fileInput.getOriginalFilename());
            File fileConvert = visitPlanService.convertMultipartFileToFile(fileInput);
            FileUtils.saveFile(fileConvert, fileName, dirUpload);
            File file = new File(dirUpload + File.separator + fileName);
            if (fileConvert != null) {
                fileConvert.delete();
            }
            PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().headerStart(0).addListDelimiter(";").build();
            List<MapPlanSaleTarget> listTargetOk = new ArrayList<>();
            List<MapPlanSaleTarget> listTargetDelete = new ArrayList<>();
            List<MapPlanSaleTarget> targets = Poiji.fromExcel(file, MapPlanSaleTarget.class, options);
            mapPlanSaleTargetService.importPlanSale(targets, listTargetDelete, listTargetOk, brCode, bcCode, targetLevel, planDate, userToken, locale);
            response = mapPlanSaleTargetService.saveAndResponse(targets, listTargetOk, listTargetDelete, response, locale, targetLevel, file);
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
            if (e instanceof PoijiException) {
                response = responseFactory.error(HttpStatus.NOT_ACCEPTABLE, String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), MessageKey.ITEM_IMPORT_IMPORT_FILE_INVALID);
            }
        }finally {
            return response;
        }
    }

    @PostMapping(path = "/search-result")
    public ResponseEntity<?> searchResult(@RequestBody PlanSaleSearchRequest request){
        Page<PlanSaleResultDto> page = mapPlanSaleResultService.searchResult(request);
        return responseFactory.success(RecordList.builder().currentPage(page.getNumber())
                .pageSize(page.getSize()).total(page.getTotalElements())
                .records(page.stream().collect(Collectors.toList())).build());
    }

    @GetMapping(path = "/download-search-result")
    public ResponseEntity<?> downloadSearchResult(@RequestParam(value = "brCode",required = false) String brCode
            , @RequestParam(value = "bcCode",required = false) String bcCode
            , @RequestParam(value = "staffCode",required = false) String staffCode
            , @RequestParam(value = "fromDate",required = false) String fromDate
            , @RequestParam(value = "toDate",required = false) String toDate
            , @RequestParam(value = "isdn",required = false) String isdn){
        PlanSaleSearchRequest request = new PlanSaleSearchRequest();
        request.setBrCode(brCode);
        request.setBcCode(bcCode);
        request.setStaffCode(staffCode);
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        request.setIsdn(isdn);
        File fileToDownload = new File(mapPlanSaleResultService.downloadSearchResult(request));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "plan_sale_result.xls";
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

    @GetMapping(path = "download-search-result-detail")
    public ResponseEntity<?> downloadSearchResultDetail(@RequestParam(value = "brCode",required = false) String brCode
            , @RequestParam(value = "bcCode",required = false) String bcCode
            , @RequestParam(value = "staffCode",required = false) String staffCode
            , @RequestParam(value = "channelCode", required = false) String channelCode
            , @RequestParam(value = "status", required = false) Integer status
            , @RequestParam(value = "fromDate",required = false) String fromDate
            , @RequestParam(value = "toDate",required = false) String toDate){
        PlanSaleSearchRequest request = new PlanSaleSearchRequest();
        request.setBrCode(brCode);
        request.setBcCode(bcCode);
        request.setStatus(status);
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        File fileToDownload = new File(mapPlanSaleResultService.downloadSearchResultDetail(request));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "template_plan_sale_export_detail.xls";
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

    @PostMapping(path = "/list-map-plan")
    public ResponseEntity<?> listMapPlan(@RequestBody PlanSaleSearchRequest request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse("01/" + request.getFromDate(), formatter);
        LocalDate firstDay = localDate.with(TemporalAdjusters.firstDayOfMonth());
        request.setFromDate(firstDay.format(formatter));
        LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
        request.setToDate(lastDay.format(formatter));
        Page<MapPlanSaleDto> page = mapPlanSaleService.mapSearchPlan(request);
        ListMapPlanResponse response = new ListMapPlanResponse();
        response.setMapPlanSaleDtos(page.stream().collect(Collectors.toList()));
        response.setTotalRecord(page.getTotalElements());
        response.setCurrentPage(page.getNumber()+1);
        response.setTotalPage(page.getTotalPages());
        return responseFactory.success(response);
    }

    @PostMapping(path = "/view")
    public ResponseEntity<?> view(@RequestBody PlanSaleSearchRequest request){
        MapPlanSaleTarget currentTarget = new MapPlanSaleTarget();
        try{
            Date planDateYear = DateUtils.parseDate(request.getPlanDate(), "YYYY");
            if (request.getTargetLevel() != null && request.getTargetLevel() == 1) {
                currentTarget = mapPlanSaleTargetService.findSaleTargetByBrCodeAndTargetLevelAndPlanDate(request.getBrCode(), 1, planDateYear);
            } else if (request.getTargetLevel() != null && request.getTargetLevel() == 2) {
                currentTarget = mapPlanSaleTargetService.findSaleTargetByBcCodeAndTargetLevelAndPlanDate(request.getBcCode(), 2, planDateYear);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return responseFactory.success(currentTarget);
        }
    }
}
