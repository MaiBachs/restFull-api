package com.spm.viettel.msm.controller;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiException;
import com.poiji.option.PoijiOptions;
import com.spm.viettel.msm.dto.ConfigureItemDto;
import com.spm.viettel.msm.dto.FileDto;
import com.spm.viettel.msm.dto.ItemConfigDto;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.SearchConfigureItemRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.dto.response.SearchItemResultResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.smartphone.entity.AppParamsSmartPhone;
import com.spm.viettel.msm.repository.smartphone.entity.ItemConfig;
import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import com.spm.viettel.msm.service.*;
import com.spm.viettel.msm.utils.*;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/api/item-config")
public class ItemConfigController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(ItemConfigController.class);
    @Autowired
    private ItemConfigService itemConfigService;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private VisitPlanService visitPlanService;
    @Autowired
    private PlanService planService;
    @Autowired
    private AppParamService appParamService;
    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String templateFolder;

    @PostMapping("/search")
    public ResponseEntity<GeneralResponse<SearchItemResultResponse>> searchConfigItem(@RequestBody SearchConfigureItemRequest request){
        SearchItemResultResponse response = itemConfigService.searchConfigItem(request);
        return responseFactory.success(response);
    }

    @GetMapping("/gravedad")
    public ResponseEntity<GeneralResponse<List<AppParamsSmartPhone>>> getGravedad(){
        List<AppParamsSmartPhone> response = appParamService.findAppParamsByTypeAndStatus(Constants.CODE_GRAVEDAD_USING);
        return responseFactory.success(response);
    }

    @GetMapping("/reason")
    public ResponseEntity<GeneralResponse<List<Reason>>> getReason(){
        List<Reason> result = itemConfigService.findReasonByStatus();
        return responseFactory.success(result);
    }

    @PutMapping("/edit")
    public ResponseEntity<GeneralResponse<ConfigureItemDto>> edit(@RequestBody ConfigureItemDto configureItemDto){
        ItemConfig itemConfig = itemConfigService.edit(configureItemDto);
        configureItemDto.setFileDto(null);
        if(itemConfig == null){
            return responseFactory.error(HttpStatus.NOT_ACCEPTABLE, String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), MessageKey.ITEM_EDIT_INVALID);
        }
        return responseFactory.success(configureItemDto);
    }

    @GetMapping("/detail-item-config")
    public ResponseEntity<GeneralResponse<ConfigureItemDto>> detailItemCOnfig(@RequestParam Long itemConfigId){
        ConfigureItemDto configureItemDto= itemConfigService.detailItemConfig(itemConfigId);
        return responseFactory.success(configureItemDto);
    }

    @Operation(summary = "import data itemConfig")
    @PostMapping(value = "/import")
    public ResponseEntity<GeneralResponse<List<ItemConfigDto>>> importItemConfig(@RequestParam("file") MultipartFile fileInput, @RequestParam("action") ActionChannelType actionType) throws IOException {
        ResponseEntity<GeneralResponse<List<ItemConfigDto>>> response = null;
        try {
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
            PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().headerStart(1).addListDelimiter(";").build();
            List<ItemConfigDto> listItemOk = new ArrayList<>();
            List<ItemConfigDto> itemConfigDtoList = Poiji.fromExcel(file, ItemConfigDto.class, options);
            String[] channelListUsing = planService.getChannelTypeIdsUsingOfCcAudit();

            listItemOk = itemConfigService.importItemOrRemove(itemConfigDtoList, channelListUsing, locale, actionType);
            org.apache.commons.io.FileUtils.deleteQuietly(file);
            String resultImport = listItemOk.size()+"/"+itemConfigDtoList.size();
            if (listItemOk.size() < itemConfigDtoList.size()) {
                response = responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),itemConfigService.buildImportResultFile(itemConfigDtoList), resultImport);
            }else{
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

    @GetMapping(path = "/download-search")
    public ResponseEntity<?> dowloadSearch(@RequestParam(value = "evaluationId",required = false) Long evaluationId,
                                           @RequestParam(value = "channelTypeId",required = false) Long channelTypeId,
                                           @RequestParam(value = "groupId",required = false) Long groupId,
                                           @RequestParam(value = "fromDate",required = false) String fromDate,
                                           @RequestParam(value = "toDate",required = false) String toDate){
        SearchConfigureItemRequest request = new SearchConfigureItemRequest();
        request.setPaging(false);
        request.setEvaluationId(evaluationId);
        request.setChannelTypeId(channelTypeId);
        request.setGroupId(groupId);
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        SearchItemResultResponse response = itemConfigService.searchAndDownloadConfigItem(request);
        File fileToDownload = new File(itemConfigService.buildSeachItemResultFile(response.getItemConfigDtoList()));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "search_item_config.xls";
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

    @GetMapping(path = "/download-template")
    public ResponseEntity<?> downloadTemplateItemconfig() {
        String fileName = "Template_import_item_rendata.xls";
        String fileToDownload = itemConfigService.rendataImportItem();
        if(!fileToDownload.isEmpty()){
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileName);
                InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(inputStreamResource);
            }catch (Exception e){
                loggerFactory.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/getImageFromFtp")
    public ResponseEntity<GeneralResponse<FileDto>> getImageFromFtp(@RequestParam(value = "filePath", required = true) String filePath){
        FileDto fileDto = itemConfigService.downloadFileToFtpServer(filePath);
        return responseFactory.success(fileDto);
    }
}
