package com.spm.viettel.msm.controller;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiException;
import com.poiji.option.PoijiOptions;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.PlanSaleBtsConfigSearchRequest;
import com.spm.viettel.msm.dto.request.SearchVisitPlanRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.entity.MapSaleBtsConfig;
import com.spm.viettel.msm.repository.sm.entity.MapSalePolicy;
import com.spm.viettel.msm.service.*;
import com.spm.viettel.msm.utils.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
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
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sale-bts-config")
public class SaleBtsConfigController extends BaseController {
    private final Logger loggerFactory = LoggerFactory.getLogger(SaleBtsConfigController.class);
    @Autowired
    private SaleBtsConfigService saleBtsConfigService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SalePolicyService salePolicyService;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private VisitPlanService visitPlanService;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping(path = "/search")
    public ResponseEntity<?> searchBtsCofig(@RequestBody PlanSaleBtsConfigSearchRequest request) {
        return responseFactory.success(saleBtsConfigService.searchBtsCofig(request));
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addMapSaleBtsConfig(@RequestBody PlanSaleBtsConfigSearchRequest request) {
        ResponseEntity responseEntity = null;
        String btsCode = StringUtils.trim(request.getBtsCode()).toUpperCase();
        String brCode = StringUtils.trim(request.getBrCode()).toUpperCase();
        List<ShopTreeDTO> branches = shopService.listShopTree(Constants.VTP_SHOP_ID, 3,
                Constants.VTP_SHOP_ID, null);
        List<ShopTreeDTO> brs = branches.stream().filter(b -> b.getCode().equalsIgnoreCase(brCode))
                .collect(Collectors.toList());
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(brs)) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BRANCH_NOT_FOUND, null, locale));
        } else {
            List<BTS> btsList = saleBtsConfigService.findListBtsByCode(btsCode, brCode);
            List<String> btsNameList = btsList.stream().map(BTS::getName).collect(Collectors.toList());
            if (btsNameList.isEmpty() || !btsNameList.contains(btsCode)) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BTS_NOT_FOUND, null, locale));
            } else {
                MapSaleBtsConfig saleBtsConfig = saleBtsConfigService.findByBrCodeAndBtsCodeAndSalePolicyId(
                        brCode, btsCode, request.getSalePolicyId());
                MapSalePolicy salePolicy = salePolicyService.findById(request.getSalePolicyId());
                if (saleBtsConfig != null) {
                    Object[] paramsObject = new Object[2];
                    paramsObject[0] = btsCode;
                    paramsObject[1] = salePolicy.getName();
                    saleBtsConfig.setStatus(Constants.ACTIVE_INT);
                    return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                            messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BTS_ALREADY_APPLY_POLICY, null, locale), paramsObject);
                } else {
                    //Create new
                    saleBtsConfig = saleBtsConfigService.createNewMapSaleBtsConfig(request, btsCode, brCode, salePolicy);
                }
                if (responseEntity == null) {
                    try {
                        MapSaleBtsConfig mapSaleBtsConfig = saleBtsConfigService.save(saleBtsConfig);
                        saleBtsConfigService.settingPolicyNormalForBTS(saleBtsConfig);
                        return responseFactory.success(mapSaleBtsConfig);
                    } catch (Exception e) {
                        return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), Constants.ERROR);
                    }
                }
            }
        }
        return null;
    }

    @PutMapping(path = "/delete")
    public ResponseEntity<GeneralResponse<MapSaleBtsConfig>> delete(@RequestBody PlanSaleBtsConfigSearchRequest request) {
        MapSaleBtsConfig mapSaleBtsConfig = saleBtsConfigService.findById(request.getId());
        if (mapSaleBtsConfig == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_SALE_BTS_CONFOG_NOT_FOUND, null, locale));
        } else {
            mapSaleBtsConfig.setStatus(0);
            try {
                saleBtsConfigService.save(mapSaleBtsConfig);
            } catch (Exception e) {
                loggerFactory.error(e.getMessage());
            }
        }
        return responseFactory.success(mapSaleBtsConfig);
    }

    @PostMapping(path = "/search-list-bts")
    public ResponseEntity<GeneralResponse<List<BTS>>> searchListBts(@RequestBody PlanSaleBtsConfigSearchRequest request) {
        String btsCode = StringUtils.isNotEmpty(request.getBtsCode()) ? StringUtils.trim(request.getBtsCode()).toUpperCase() : "";
        String brCode = StringUtils.isNotEmpty(request.getBrCode()) ? StringUtils.trim(request.getBrCode()).toUpperCase() : "";
        return responseFactory.success(saleBtsConfigService.findListBtsByCode(btsCode, brCode));
    }

    @PutMapping(path = "/set-status")
    public ResponseEntity<?> setStatus(@RequestBody PlanSaleBtsConfigSearchRequest request) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        MapSaleBtsConfig mapSaleBtsConfig = saleBtsConfigService.findById(request.getId());
        if (mapSaleBtsConfig == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_SALE_BTS_CONFOG_NOT_FOUND, null, locale));
        } else {
            MapSaleBtsConfig mapSaleBtsConfigClone = (MapSaleBtsConfig) BeanUtils.cloneBean(mapSaleBtsConfig);
            mapSaleBtsConfig.setStatus(request.getStatus());
            try {
                saleBtsConfigService.save(mapSaleBtsConfig);
                saleBtsConfigService.saveHistoryBts(mapSaleBtsConfigClone, mapSaleBtsConfig);
                saleBtsConfigService.settingPolicyNormalForBTS(mapSaleBtsConfig);
                return responseFactory.success(mapSaleBtsConfig);
            } catch (Exception e) {
                loggerFactory.error(e.getMessage());
            }
        }
        return null;
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<?> edit(@RequestBody PlanSaleBtsConfigSearchRequest request) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, ParseException {
        MapSaleBtsConfig bts = saleBtsConfigService.findById(request.getId());
        ResponseEntity responseEntity = null;
        if (bts == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_NOT_FOUND, null, locale));
        } else {
            MapSaleBtsConfig btsClone = (MapSaleBtsConfig) BeanUtils.cloneBean(bts);
            String btsCode = bts.getBtsCode().toUpperCase();
            String brCode = bts.getBrCode().toUpperCase();
            if (bts.getSalePolicyId().equals(request.getSalePolicyId())) {
                bts.setStatus(request.getStatus());
                UserTokenDto userToken = authenticateService.getUserInformation(getUserLogined());
                if (userToken != null) {
                    bts.setCreatedUser(userToken.getStaffCode());
                    bts.setUpdateUser(userToken.getStaffCode());
                }
                if (StringUtils.isNotEmpty(request.getStartDate())) {
                    try {
                        bts.setStartDate(DateUtils.parseDate(request.getStartDate(), Constants.DD_MM_YYYY));
                    } catch (Exception ex) {
                        loggerFactory.error(ex.getMessage());
                        return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                                messageSource.getMessage(MessageKey.COMMON_START_DATE_INVALID, null, locale));
                    }
                } else {
                    bts.setStartDate(new Date());
                }
                if (StringUtils.isNotEmpty(request.getEndDate())) {
                    try {
                        bts.setEndDate(DateUtils.parseDate(request.getEndDate(), Constants.DD_MM_YYYY));
                        if (bts.getEndDate().before(bts.getStartDate())) {
                            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                                    messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_END_DATE_GREATER_THAN_OR_EQUAL_START_DATE, null, locale));
                        }
                    } catch (Exception ex) {
                        loggerFactory.error(ex.getMessage());
                        return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                                messageSource.getMessage(MessageKey.COMMON_END_DATE_INVALID, null, locale));
                    }
                } else {
                    bts.setEndDate(null);
                }
                bts.setUpdateDate(new Date());
            } else {
                MapSaleBtsConfig existedConfig = saleBtsConfigService.findByBrCodeAndBtsCodeAndSalePolicyId(brCode, btsCode, request.getSalePolicyId());
                if (existedConfig == null) {
                    MapSalePolicy salePolicy = salePolicyService.findById(request.getSalePolicyId());
                    bts = saleBtsConfigService.createNewMapSaleBtsConfig(request, btsCode, brCode, salePolicy);
                } else {
                    MapSalePolicy salePolicy = salePolicyService.findById(request.getSalePolicyId());
                    Object[] paramsObject = new Object[2];
                    paramsObject[0] = btsCode;
                    paramsObject[1] = salePolicy.getName();
                    responseEntity = responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                            messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BTS_ALREADY_APPLY_POLICY, null, locale));
                }
            }

            if (responseEntity == null) {
                try {
                    MapSaleBtsConfig mapSaleBtsConfig = saleBtsConfigService.save(bts);
                    if (btsClone.getId() == bts.getId()) {
                        saleBtsConfigService.saveHistoryBts(btsClone, bts);
                    }
                    //Update BTS has policy normal
                    saleBtsConfigService.settingPolicyNormalForBTS(bts);
                    return responseFactory.success(mapSaleBtsConfig);
                } catch (Exception e) {
                    loggerFactory.error(e.getMessage());
                }
            }
        }
        return responseEntity;
    }

    @Operation(summary = "import data Bts")
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
    public ResponseEntity<?> importBts(@RequestParam("file") MultipartFile fileInput) {
        String fileImportResultName = null;
        String errorMessageImport;
        try {
            File dirUpload = new File(this.fileNameFullFolder);
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

            PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().addListDelimiter(";").build();
            List<MapSaleBtsConfig> listTargetOk = new ArrayList<>();
            List<SaleBtsConfigDto> btsConfigs = Poiji.fromExcel(file, SaleBtsConfigDto.class, options);
            List<SaleBtsConfigDto> normalActiveBtsConfig = btsConfigs.stream().filter(btsConfig ->
                    Constants.SALE_POLICY_FOR_BTS_NORMAL_NAME.equalsIgnoreCase(btsConfig.getPolicyName() != null ? btsConfig.getPolicyName().trim() : null)
                            && Constants.ACTIVE_STATUS_NAME.equalsIgnoreCase(btsConfig.getStatus() != null ? btsConfig.getStatus().trim() : null)
            ).collect(Collectors.toList());
            List<ShopTreeDTO> branches = shopService.listShopTree(Constants.VTP_SHOP_ID, 3, Constants.VTP_SHOP_ID, null);
            for (int index = 0; index < btsConfigs.size(); index++) {
                SaleBtsConfigDto bts = btsConfigs.get(index);
                String btsSite = StringUtils.trim(bts.getBtsSite());
                String brCode = StringUtils.trim(bts.getBrCode());
                String policyName = StringUtils.trim(bts.getPolicyName());
                String status = StringUtils.trim(bts.getStatus());

                if (StringUtils.isEmpty(policyName)) {
                    bts.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_POLICY_IS_REQUIRED, null, locale));
                } else if (StringUtils.isNotEmpty(bts.getStartDate()) && !GenericValidator.isDate(bts.getStartDate(), "dd/MM/yyyy", true)) {
                    bts.setComment(messageSource.getMessage(MessageKey.COMMON_START_DATE_INVALID, null, locale));
                } else if (StringUtils.isEmpty(bts.getStartDate())) {
                    bts.setComment(messageSource.getMessage(MessageKey.COMMON_START_DATE_INVALID, null, locale));
                } else if (StringUtils.isNotEmpty(bts.getEndDate())
                        && (!GenericValidator.isDate(bts.getEndDate(), "dd/MM/yyyy", true) || policyName.equalsIgnoreCase(Constants.SALE_POLICY_FOR_BTS_NORMAL_NAME))) {
                    bts.setComment(messageSource.getMessage(MessageKey.COMMON_END_DATE_INVALID, null, locale));
                } else if (StringUtils.isEmpty(brCode)) {
                    bts.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BR_CODE_REQUIRED, null, locale));
                } else if (StringUtils.isEmpty(btsSite)) {
                    bts.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BTS_CODE_REQUIRED, null, locale));
                } else if (StringUtils.isEmpty(bts.getStatus())) {
                    bts.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_STATUS_INVALID, null, locale));
                } else if (!(Constants.ACTIVE_STATUS_NAME.equalsIgnoreCase(status) || Constants.INACTIVE_STATUS_NAME.equalsIgnoreCase(status))) {
                    bts.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_STATUS_INVALID, null, locale));
                } else if (Constants.INACTIVE_STATUS_NAME.equalsIgnoreCase(status) && policyName.equalsIgnoreCase(Constants.SALE_POLICY_FOR_BTS_NORMAL_NAME)) {
                    bts.setComment(messageSource.getMessage(MessageKey.IMPORT_SALE_BTS_CONFIG_ROW_IGNORED, null, locale));
                } else if (Constants.ACTIVE_STATUS_NAME.equalsIgnoreCase(status)
                        && !policyName.equalsIgnoreCase(Constants.SALE_POLICY_FOR_BTS_NORMAL_NAME)
                        && (normalActiveBtsConfig.stream().anyMatch(config -> bts.getBtsSite().equalsIgnoreCase(config.getBtsSite().trim())
                        && bts.getBrCode().equalsIgnoreCase(config.getBrCode().trim())))) {
                    //Check if have normal active for same BTS, then skip this row
                    bts.setComment(messageSource.getMessage(MessageKey.IMPORT_SALE_BTS_CONFIG_ROW_IGNORED_DUE_TO_NORMAL_ACTIVE, null, locale));
                } else {
                    String upperBtsSite = btsSite.toUpperCase();
                    String upperBrCode = brCode.toUpperCase();
                    for (int k = 0; k < listTargetOk.size(); k++) {
                        MapSaleBtsConfig config = listTargetOk.get(k);
                        if (config.getBtsCode().equalsIgnoreCase(btsSite) && config.getSalePolicyName().equalsIgnoreCase(policyName)) {
                            bts.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_DUPLICATE_DATA, null, locale));
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(bts.getComment())) {
                        List<ShopTreeDTO> brs = branches.stream().filter(b -> b.getCode().equalsIgnoreCase(upperBrCode))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(brs)) {
                            bts.setComment(messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_BR_CODE_INVALID, null, locale));
                        } else {
                            List<BTS> btsList = saleBtsConfigService.findListBtsByCode(upperBtsSite, upperBrCode);
                            List<String> btsNameList = btsList.stream().map(BTS::getName).collect(Collectors.toList());
                            if (btsNameList.isEmpty() || !btsNameList.contains(upperBtsSite)) {
                                bts.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BTS_NOT_FOUND, null, locale));
                            } else {
                                MapSalePolicy salePolicy = salePolicyService.findByName(policyName);
                                if (salePolicy != null) {
                                    MapSaleBtsConfig saleBtsConfig = saleBtsConfigService.findByBrCodeAndBtsCodeAndSalePolicyId(upperBrCode, upperBtsSite, salePolicy.getId());
                                    if (saleBtsConfig == null) {
                                        saleBtsConfig = new MapSaleBtsConfig();
                                    }
                                    saleBtsConfig.setBrCode(upperBrCode);
                                    saleBtsConfig.setBtsCode(upperBtsSite);
                                    saleBtsConfig.setSalePolicyId(salePolicy.getId());
                                    saleBtsConfig.setSalePolicyName(salePolicy.getName());

                                    if (Constants.ACTIVE_STATUS_NAME.equalsIgnoreCase(status)) {
                                        saleBtsConfig.setStatus((int) Constants.ACTIVE);
                                    } else if ((Constants.INACTIVE_STATUS_NAME.equalsIgnoreCase(status))) {
                                        saleBtsConfig.setStatus((int) Constants.INACTIVE);
                                    }
                                    if (StringUtils.isNotEmpty(bts.getStartDate())) {
                                        try {
                                            saleBtsConfig.setStartDate(DateUtils.parseDate(bts.getStartDate(), Constants.DD_MM_YYYY));
                                        } catch (Exception ex) {
                                            bts.setComment(messageSource.getMessage(MessageKey.COMMON_START_DATE_INVALID, null, locale));
                                            loggerFactory.error(ex.getMessage());
                                        }
                                    } else {
                                        saleBtsConfig.setStartDate(new Date());
                                    }
                                    if (StringUtils.isNotEmpty(bts.getEndDate())) {
                                        try {
                                            saleBtsConfig.setEndDate(DateUtils.parseDate(bts.getEndDate(), Constants.DD_MM_YYYY));
                                            if (saleBtsConfig.getEndDate().before(saleBtsConfig.getStartDate())) {
                                                bts.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_END_DATE_GREATER_THAN_OR_EQUAL_START_DATE, null, locale));
                                            }
                                        } catch (Exception ex) {
                                            loggerFactory.error(ex.getMessage());
                                            bts.setComment(messageSource.getMessage(MessageKey.COMMON_END_DATE_INVALID, null, locale));
                                        }
                                    } else {
                                        saleBtsConfig.setEndDate(null);
                                    }

                                    if (StringUtils.isEmpty(bts.getComment())) {
                                        UserTokenDto userToken = authenticateService.getUserInformation(getUserLogined());
                                        if (userToken != null) {
                                            saleBtsConfig.setCreatedUser(userToken.getStaffCode());
                                            saleBtsConfig.setUpdateUser(userToken.getStaffCode());
                                        }
                                        saleBtsConfig.setCreatedDate(saleBtsConfig.getCreatedDate() != null ? saleBtsConfig.getCreatedDate() : new Date());
                                        saleBtsConfig.setUpdateDate(new Date());
                                        listTargetOk.add(saleBtsConfig);
                                    }
                                } else {
                                    bts.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_POLICY_NOT_FOUND, null, locale));
                                }
                            }
                        }
                    }
                }
            }
            listTargetOk.forEach(conf -> {
                saleBtsConfigService.save(conf);
            });
            errorMessageImport = messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_RESULT, new String[]{String.valueOf(listTargetOk.size()), String.valueOf(btsConfigs.size())}, locale);
            if (listTargetOk.size() < btsConfigs.size()) {
                fileImportResultName = saleBtsConfigService.buildImportResultFile(btsConfigs);
            }
            org.apache.commons.io.FileUtils.deleteQuietly(file);
            for (MapSaleBtsConfig btsConfig : listTargetOk) {
                saleBtsConfigService.settingPolicyNormalForBTS(btsConfig);
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
            errorMessageImport = Constants.ERROR;
            if (e instanceof PoijiException) {
                errorMessageImport = messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_FILE_INVALID, null, locale);
            } else if (e instanceof JDBCConnectionException) {
                errorMessageImport = messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_SQL_CONNECTION_ERROR, null, locale);
            } else {
                errorMessageImport = e.getMessage();
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("errorMessageImport", errorMessageImport);
        response.put("fileImportResultName", fileImportResultName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/download-result")
    public ResponseEntity<?> downLoadFile(@RequestBody PlanSaleBtsConfigSearchRequest request) throws ParseException {
        File fileToDownload = new File(saleBtsConfigService.buildSearchBTSResultFile(request));
        if (fileToDownload != null) {
            try {
                InputStream inputStream = new FileInputStream(fileToDownload);
                String fileName = "Template_search_bts_export.xls";
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
