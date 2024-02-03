package com.spm.viettel.msm.service;


import com.spm.viettel.msm.dto.PlanSalePolicyDTO;
import com.spm.viettel.msm.dto.SaleTimeConfigDTO;
import com.spm.viettel.msm.dto.request.SalePolicyRequest;
import com.spm.viettel.msm.dto.request.SaleTimeConfigRequest;
import com.spm.viettel.msm.dto.response.SalePolicyDetailRespone;
import com.spm.viettel.msm.dto.response.SearchSalePolicyResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.MapSalePolicyRepository;
import com.spm.viettel.msm.repository.sm.entity.MapSalePolicy;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.DataUtils;
import com.spm.viettel.msm.utils.DateUtil;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class SalePolicyService {
    private final Logger loggerFactory = LoggerFactory.getLogger(SalePolicyService.class);
    @Autowired
    private MapSalePolicyRepository mapSalePolicyRepository;
    @Autowired
    private MapSaleTimeConfigService mapSaleTimeConfigService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ResponseFactory responseFactory;

    private Locale locale = LocaleContextHolder.getLocale();
    private Map<Integer, String> listAddTimeErrors = new HashMap<>();
    private Map<Integer, String> listEditTimeErrors = new HashMap<>();
    private Map<Integer, String> listDeleteTimeErrors = new HashMap<>();

    public List<MapSalePolicy> getListSalePolicy(){
        return mapSalePolicyRepository.getListSalePolicy();
    }

    public List<MapSalePolicy> findListPolicy() {
        return mapSalePolicyRepository.getListPolicy();
    }

    public MapSalePolicy findById(Long id) {
        return mapSalePolicyRepository.findById(id).get();
    }

    public MapSalePolicy findByName(String name) {
        return mapSalePolicyRepository.findByName(name);
    }

    public SearchSalePolicyResponse getListSalePolicyWithSaleTime(SalePolicyRequest request) {
        SearchSalePolicyResponse searchSalePolicyResponse = new SearchSalePolicyResponse();
        List<PlanSalePolicyDTO> planSalePolicyDTOS = mapSalePolicyRepository.getListSalePolicyWithSaleTime(
                request.getName().trim(),
                request.getStatus(),
                request.getStartDate(),
                request.getEndDate());
        if (request.isPaging()) {
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
            int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 1;
            Long countResults = (long) planSalePolicyDTOS.size();
            int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
            if (currentPage < 1) {
                currentPage = 1;
            } else if (currentPage > lastPageNumber) {
                currentPage = lastPageNumber;
            }
            searchSalePolicyResponse.setTotalRecord(countResults);
            searchSalePolicyResponse.setTotalPage(lastPageNumber);
            searchSalePolicyResponse.setCurrentPage(currentPage);
            int startIndex = (currentPage - 1) * pageSize;
            if (startIndex < 0) {
                startIndex = 0;
            }
            int endIndex = Math.min(startIndex + pageSize, planSalePolicyDTOS.size());
            searchSalePolicyResponse.setPlanSalePolicyDTOS(planSalePolicyDTOS.subList(startIndex, endIndex));
        }
        return searchSalePolicyResponse;
    }

    public MapSalePolicy save(MapSalePolicy mapSalePolicy) {
        return mapSalePolicyRepository.save(mapSalePolicy);
    }

    public MapSalePolicy findByNameAndNotDeleted(String name) {
        return mapSalePolicyRepository.findByNameAndNotDeleted(name);
    }

    public ResponseEntity<?> addNewSalePolicy(SalePolicyRequest request) {
        MapSalePolicy mapSalePolicy = new MapSalePolicy();
        String name = StringUtils.trim(request.getName());
        if (mapSalePolicyRepository.findByNameAndNotDeleted(name) != null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.SALE_POLICY_NAME_DUPLICATED, null, locale));
        }
        if (StringUtils.isEmpty(name)) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.SALE_POLICY_NAME_REQUIRED, null, locale));
        }
        mapSalePolicy.setName(name);
        mapSalePolicy.setContent(StringUtils.trim(request.getContent()));
        if (StringUtils.isNotEmpty(request.getStartDate())) {
            try {
                mapSalePolicy.setStartDate(
                        DateUtils.parseDate(request.getStartDate(), Constants.DD_MM_YYYY));
            } catch (Exception ex) {
                loggerFactory.error(ex.getMessage());
            }
        } else {
            mapSalePolicy.setStartDate(new Date());
        }
        if (StringUtils.isNotEmpty(request.getEndDate())) {
            try {
                mapSalePolicy.setEndDate(
                        DateUtils.parseDate(request.getEndDate(), Constants.DD_MM_YYYY));
                if (mapSalePolicy.getEndDate().before(mapSalePolicy.getStartDate())) {
                    return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value())
                            ,messageSource.getMessage( MessageKey.PLAN_POLICY_END_DATE_GREATER_THAN_OR_EQUAL_START_DATE, null, locale));
                }
            } catch (Exception ex) {
                loggerFactory.error(ex.getMessage());
            }
        }
        mapSalePolicy.setStatus(Constants.STATUS_ACTIVE.intValue());
        List<SaleTimeConfigRequest> addTimeConfigs = request.getAddSaleTimeConfigs();

        for (int i = 0; i < addTimeConfigs.size(); i++) {
            SaleTimeConfigRequest configRequest = addTimeConfigs.get(i);
            String timeErrorMessage = DataUtils.validateTimeFromAndTimeTo(configRequest.getTimeFrom(), configRequest.getTimeTo(), messageSource, locale);
            if (StringUtils.isNotEmpty(timeErrorMessage)) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        listAddTimeErrors.put(i, timeErrorMessage));
            }
        }
        //add new map-sale-policy
        save(mapSalePolicy);
        //add sale-time-config
        mapSaleTimeConfigService.insertSaleTimeConfig(addTimeConfigs, mapSalePolicy.getId());
        return responseFactory.success(mapSalePolicy);
    }

    public ResponseEntity<?> editSalePolicy(SalePolicyRequest request) {
        MapSalePolicy mapSalePolicy = findById(request.getId());
        if (mapSalePolicy == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.SALE_POLICY_NOT_FOUND, null, locale));
        } else {
            String name = StringUtils.trim(request.getName());
            if (StringUtils.isEmpty(name)) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.SALE_POLICY_NAME_REQUIRED, null, locale));
            }
            MapSalePolicy oldPolicy = mapSalePolicyRepository.findByNameAndNotDeleted(name);
            if (oldPolicy != null && !oldPolicy.getId().equals(request.getId())) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.SALE_POLICY_NAME_DUPLICATED, null, locale));
            } else {
                mapSalePolicy.setStatus(request.getStatus());
                mapSalePolicy.setContent(StringUtils.trim(request.getContent()));
                mapSalePolicy.setName(request.getName());
            }

            if (StringUtils.isNotEmpty(request.getStartDate())) {
                try {
                    mapSalePolicy.setStartDate(
                            DateUtils.parseDate(request.getStartDate(), Constants.DD_MM_YYYY));
                } catch (Exception ex) {
                    loggerFactory.error(ex.getMessage());
                }
            } else {
                mapSalePolicy.setStartDate(new Date());
            }
            if (StringUtils.isNotEmpty(request.getEndDate())) {
                try {
                    mapSalePolicy.setEndDate(
                            DateUtils.parseDate(request.getEndDate(), Constants.DD_MM_YYYY));
                    if (mapSalePolicy.getEndDate().before(mapSalePolicy.getStartDate())) {
                        return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value())
                                ,messageSource.getMessage( MessageKey.PLAN_POLICY_END_DATE_GREATER_THAN_OR_EQUAL_START_DATE, null, locale));
                    }
                } catch (Exception ex) {
                    loggerFactory.error(ex.getMessage());
                }
            } else {
                mapSalePolicy.setEndDate(null);
            }

            List<SaleTimeConfigRequest> addTimeConfigs = request.getAddSaleTimeConfigs();
            for (int i = 0; i < addTimeConfigs.size(); i++) {
                SaleTimeConfigRequest configRequest = addTimeConfigs.get(i);
                String timeErrorMessage = DataUtils.validateTimeFromAndTimeTo(configRequest.getTimeFrom(), configRequest.getTimeTo(), messageSource, locale);
                if (StringUtils.isNotEmpty(timeErrorMessage)) {
                    listAddTimeErrors.put(i, timeErrorMessage);
                }
            }

            List<SaleTimeConfigDTO> existingConfigs = mapSaleTimeConfigService.getListSaleTimeConfigBySalePolicyId(request.getId());
            List<Long> existingConfigIds = existingConfigs.stream().map(SaleTimeConfigDTO::getId).collect(Collectors.toList());
            List<SaleTimeConfigRequest> editTimeConfigs = request.getEditSaleTimeConfigs();
            for (int i = 0; i < editTimeConfigs.size(); i++) {
                SaleTimeConfigRequest configRequest = editTimeConfigs.get(i);
                String timeErrorMessage = DataUtils.validateTimeFromAndTimeTo(configRequest.getTimeFrom(), configRequest.getTimeTo(), messageSource, locale);
                if (StringUtils.isNotEmpty(timeErrorMessage)) {
                    return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), listEditTimeErrors.put(i, timeErrorMessage));
                } else if (!existingConfigIds.contains(configRequest.getId())) {
                    return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                            messageSource.getMessage(MessageKey.SALE_TIME_CONFIG_NOT_FOUND, null, locale));
                }
            }

            List<SaleTimeConfigRequest> deleteTimeConfigs = request.getDeleteSaleTimeConfigs();
            for (int i = 0; i < deleteTimeConfigs.size(); i++) {
                SaleTimeConfigRequest configRequest = deleteTimeConfigs.get(i);
                if (!existingConfigIds.contains(configRequest.getId())) {
                    return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                            messageSource.getMessage(MessageKey.SALE_TIME_CONFIG_NOT_FOUND, null, locale));
                }
            }
            this.save(mapSalePolicy);
            //Update sale time configs
            mapSaleTimeConfigService.insertSaleTimeConfig(addTimeConfigs, mapSalePolicy.getId());
            mapSaleTimeConfigService.updateSaleTimeConfig(editTimeConfigs);
            mapSaleTimeConfigService.deleteSaleTimeConfig(deleteTimeConfigs);
        }
        return new ResponseEntity<>(mapSalePolicy, HttpStatus.OK);
    }

    public ResponseEntity<?> detailSalePolicy(SalePolicyRequest request) throws ParseException {
        if (request.getId() == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.SALE_POLICY_ID_REQUIRED, null, locale));
        }
        SalePolicyDetailRespone salePolicyDetailRespone = new SalePolicyDetailRespone();
        Optional<MapSalePolicy> salePolicy = mapSalePolicyRepository.findById(request.getId());
        if (salePolicy == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.SALE_POLICY_NOT_FOUND, null, locale));
        } else {
            MapSalePolicy mapSalePolicy = salePolicy.get();
            salePolicyDetailRespone.setId(mapSalePolicy.getId());
            salePolicyDetailRespone.setName(mapSalePolicy.getName());
            salePolicyDetailRespone.setContent(mapSalePolicy.getContent());
            salePolicyDetailRespone.setStartDate(mapSalePolicy.getStartDate());
            salePolicyDetailRespone.setEndDate(mapSalePolicy.getEndDate());
            salePolicyDetailRespone.setStatus(mapSalePolicy.getStatus());
            List<SaleTimeConfigDTO> mapSaleTimeConfigs = mapSaleTimeConfigService.getListSaleTimeConfigBySalePolicyId(mapSalePolicy.getId());
            salePolicyDetailRespone.setSaleTimeConfigDTOS(mapSaleTimeConfigs);
        }
        return new ResponseEntity<>(salePolicyDetailRespone, HttpStatus.OK);
    }

    public ResponseEntity<?> setStatusSalePolicy(SalePolicyRequest request) {
        MapSalePolicy mapSalePolicy = this.findById(request.getId());
        if (mapSalePolicy == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.SALE_POLICY_NOT_FOUND, null, locale));
        } else {
            mapSalePolicy.setStatus(request.getStatus());
            try {
                this.save(mapSalePolicy);
            } catch (Exception e) {
                loggerFactory.error(e.getMessage());
            }
        }
        return new ResponseEntity<>(mapSalePolicy, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteSalePolicy(SalePolicyRequest request) {
        MapSalePolicy mapSalePolicy = this.findById(request.getId());
        if (mapSalePolicy == null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.SALE_POLICY_NOT_FOUND, null, locale));
        } else {
            mapSalePolicy.setStatus(Constants.STATUS_DELETE);
            mapSaleTimeConfigService.deleteSaleTimeConfigByPolicyId(request.getId());
            try {
                this.save(mapSalePolicy);
            } catch (Exception e) {
                loggerFactory.error(e.getMessage());
            }
        }
        return new ResponseEntity<>(mapSalePolicy, HttpStatus.OK);
    }
}
