
package com.spm.viettel.msm.controller;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiException;
import com.poiji.option.PoijiOptions;
import com.spm.viettel.msm.dto.ItemConfigDto;
import com.spm.viettel.msm.dto.PenaltyDto;
import com.spm.viettel.msm.dto.UserTokenDto;
import com.spm.viettel.msm.dto.enums.ActionChannelType;
import com.spm.viettel.msm.dto.request.SearchPenaltyRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.dto.response.SearchPenaltyResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.smartphone.entity.Penalty;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api/penalty")
public class PenaltyController extends BaseController{
    private final Logger loggerFactory = LoggerFactory.getLogger(PenaltyController.class);
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private VisitPlanService visitPlanService;
    @Autowired
    private ItemConfigService itemConfigService;
    @Autowired
    private PenaltyService penaltyService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthenticateService authenticateService;
    @Override
    public HttpServletRequest getRequest() {
        return request;
    }
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String templateFolder;

    @PostMapping(path = "/search")
    public ResponseEntity<GeneralResponse<SearchPenaltyResponse>> search(@RequestBody SearchPenaltyRequest searchPenaltyRequest){
        searchPenaltyRequest.setCurrentPage(searchPenaltyRequest.getCurrentPage()-1);
        SearchPenaltyResponse response = penaltyService.searchPenalty(searchPenaltyRequest);
        return responseFactory.success(response);
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<GeneralResponse<Penalty>> edit(@RequestBody Penalty penalty){
        Penalty result = penaltyService.save(penalty);
        if(result == null){
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), "Actualizar error de penalización");
        }
        return responseFactory.success(result);
    }

    @GetMapping(path = "/fil-user-type")
    public ResponseEntity<GeneralResponse<String[]>> filUserType(){
        String[] userTypeUsing = itemConfigService.getParamsByTypeAndCode("IMPORT_PENALTY", "PENALTY_USER_TYPE").getValue().split(";");
        return responseFactory.success(userTypeUsing);
    }

    @Operation(summary = "import data penalty")
    @PostMapping(value = "/import",consumes = {
            "multipart/form-data"
    })
    public ResponseEntity<?> importPenalty(@RequestParam("file") MultipartFile fileInput, @RequestParam("action") ActionChannelType actionType){
        ResponseEntity<GeneralResponse<List<ItemConfigDto>>> response = null;
        try{
            UserTokenDto userToken = authenticateService.getUserInformation(getUserLogined());
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
            List<Penalty> listPenaltyOk = new ArrayList<>();
            List<Penalty> listPenaltyRemoveOk = new ArrayList<>();
            List<PenaltyDto> penaltyDtoList = Poiji.fromExcel(file, PenaltyDto.class, options);

            listPenaltyOk = penaltyService.addPenalty(penaltyDtoList, listPenaltyOk, locale, userToken);
            if (listPenaltyOk.size() < penaltyDtoList.size()) {
                String dataError = String.valueOf(listPenaltyOk.size()) + "/" + String.valueOf(penaltyDtoList.size());
                response = responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),penaltyService.buildImportResultFile(penaltyDtoList), dataError);
            }else{
                response = responseFactory.success();
            }
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
            ResponseEntity responseEntity = responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), Constants.ERROR);
            if (e instanceof PoijiException) {
                response = responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), MessageKey.PLAN_SALE_IMPORT_FILE_INVALID);
            } else if (e instanceof JDBCConnectionException) {
                response = responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), MessageKey.PLAN_CHANNEL_IMPORT_SQL_CONNECTION_ERROR);
            } else {
                response = responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), e.getMessage());
            }
            return responseEntity;
        }finally {
            return response;
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<GeneralResponse<Penalty>> detail(@RequestParam("penaltyId") Long penaltyId){
        Penalty penalty = penaltyService.detail(penaltyId);
        return responseFactory.success(penalty);
    }

    @PostMapping(path = "/download-search")
    public ResponseEntity<?> dowloadSearch(@RequestBody SearchPenaltyRequest request){
        request.setPaging(false);
        SearchPenaltyResponse response = penaltyService.searchPenalty(request);
        File fileToDownload = new File(penaltyService.buildSearchResultFile(response.getPenaltyList()));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "search_penalty.xls";
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
}

