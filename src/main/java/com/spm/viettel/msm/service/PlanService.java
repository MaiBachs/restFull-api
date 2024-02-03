package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.ChannelTypeDTO;
import com.spm.viettel.msm.dto.request.PlanRequest;
import com.spm.viettel.msm.dto.response.SearchChannelResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.ChannelTypeRepository;
import com.spm.viettel.msm.repository.sm.entity.ChannelType;
import com.spm.viettel.msm.repository.smartphone.PlanJobRepository;
import com.spm.viettel.msm.repository.smartphone.PlanRepository;
import com.spm.viettel.msm.repository.smartphone.PlanResultRepository;
import com.spm.viettel.msm.repository.smartphone.entity.Plan;
import com.spm.viettel.msm.repository.smartphone.entity.PlanJob;
import com.spm.viettel.msm.repository.smartphone.entity.PlanResult;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PlanService {
    public static final String NOT_FOUND = "NOT FOUND";
    public static final Long PDV = 80043L;
    public static final String SHOP = "1";
    public static final long SHOPPL = 1L;
    public static final String STAFF = "nuxt/assets/images/user/2.jpg";
    public static final int STAFFPL = 2;
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private PlanResultRepository planResultRepository;

    @Autowired
    private PlanJobRepository planJobRepository;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    public String[] getChannelTypeIdsUsingOfCcAudit() {
        return planRepository.getChannelTypeIdsUsingOfCcAudit();
    }

    public List<ChannelTypeDTO> getChannelTypesUsingOfAddCheckList() {
        return channelTypeRepository.getAllChannelTypeUsingOfAddCheckList();
    }

    public ResponseEntity<?> searchPlanByChannel(PlanRequest request) {
        PlanRequest planRequest;
        List<Plan> planList = planRepository.searchPlanByChannel(request.getChannelTypeId(),
                request.getPlanName().trim().length() == 0 ? null: request.getPlanName().toLowerCase().trim(),
                request.getStatus(), request.getObjectLevel(), request.getObjectType());
        SearchChannelResponse searchChannelResponse = new SearchChannelResponse();
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        List<PlanRequest> planRequests = new ArrayList<>();
        for (Plan plan : planList) {
            String objectLevelName = null;
            if (plan.getObjectLevel() == null) {
                objectLevelName = null;
            } else if (plan.getObjectLevel() == 1) {
                objectLevelName = "PDV de Bitel (Celular)";
            } else if (plan.getObjectLevel() == 2) {
                objectLevelName = "PDV de Bitel (solo recarga)";
            } else if (plan.getObjectLevel() == 3) {
                objectLevelName = "PDV de Bitel (Atencion de Cliente)";
            } else if (plan.getObjectLevel() == 4) {
                objectLevelName = "PDV del Distribuidor";
            } else if (plan.getObjectLevel() == 5) {
                objectLevelName = "PDV del Mercado libre";
            } else if (plan.getObjectLevel() == 6) {
                objectLevelName = "PDV de Bitel (viaje + hotel)";
            }
            ChannelType channelTypes = channelTypeList.stream().filter(ct ->
                    ct.getChannelTypeId().equals(plan.getChannelTypeId())).findFirst().orElse(null);
            if (channelTypes == null) {
                planRequest = PlanRequest.builder()
                        .planId(plan.getPlanId())
                        .channelTypeId(null)
                        .channelTypeName(null)
                        .planName(plan.getName())
                        .objectType(plan.getObjectType())
                        .objectLevel(plan.getObjectLevel())
                        .objectLevelName(objectLevelName)
                        .status(plan.getStatus())
                        .lastUpdate(plan.getLastUpdate())
                        .build();
                planRequests.add(planRequest);
            } else {
                planRequest = PlanRequest.builder()
                        .planId(plan.getPlanId())
                        .channelTypeId(channelTypes.getChannelTypeId())
                        .channelTypeName(channelTypes.getName())
                        .planName(plan.getName())
                        .objectType(plan.getObjectType())
                        .objectLevel(plan.getObjectLevel())
                        .objectLevelName(objectLevelName)
                        .status(plan.getStatus())
                        .lastUpdate(plan.getLastUpdate())
                        .build();
                planRequests.add(planRequest);
            }
        }
        if (request.isPaging()) {
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
            int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 0;
            Long countResults = (long) planRequests.size();
            int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
            if (currentPage < 0) {
                currentPage = 0;
            } else if (currentPage > lastPageNumber) {
                currentPage = lastPageNumber;
            }
            searchChannelResponse.setTotalRecord(countResults);
            searchChannelResponse.setTotalPage(lastPageNumber);
            searchChannelResponse.setCurrentPage(currentPage);
            int startIndex = currentPage * pageSize;
            int endIndex = Math.min(startIndex + pageSize, planRequests.size());
            searchChannelResponse.setPlanRequests(planRequests.subList(startIndex, endIndex));
        }
        return new ResponseEntity<>(searchChannelResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> addNewPlan(PlanRequest request) {
        Plan plan = new Plan();
        Plan planCheckChannelTypeAndObjectLevel = planRepository.
                getDataPlanByChannelTypeAndObjectLevel(PDV, request.getObjectLevel());
        if (planCheckChannelTypeAndObjectLevel != null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value())
                    , messageSource.getMessage(MessageKey.CHANNEL_TYPE_WITH_THIS_OBJECT_LEVEL_ALREADY_EXISTS, null, locale));
        }
        if (StringUtils.isEmpty(String.valueOf(request.getChannelTypeId()))) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value())
                    , messageSource.getMessage(MessageKey.CHANNEL_TYPE_NOT_NULL, null, locale));
        }
        if (StringUtils.isEmpty(request.getPlanName().trim())) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value())
                    , messageSource.getMessage(MessageKey.PLAN_NAME_NOT_NULL, null, locale));
        }
        ChannelType channelType = channelTypeRepository.findById(request.getChannelTypeId()).orElse(null);
        if (channelType.getObjectType().equals(SHOP)) {
            if (request.getObjectType() != SHOPPL) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.OBJECT_TYPE_FALSE, null, locale));
            }
            plan.setObjectType(request.getObjectType());
        } else {
            if (request.getObjectType() != STAFFPL) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.OBJECT_TYPE_FALSE, null, locale));
            }
            plan.setObjectType(request.getObjectType());
        }
        if (planRepository.getQuantityPlanByPlanName(request.getPlanName().trim()) != null) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.PLAN_NAME_IS_EXISTED, null, locale));
        }
        if (channelType.getChannelTypeId().equals(PDV)) {
            if (request.getObjectLevel() == null || request.getObjectLevel().toString().isEmpty()) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.OBJECT_LEVEL_NOT_NULL, null, locale));
            }
            plan.setObjectLevel(request.getObjectLevel());
        } else {
            if (request.getObjectLevel() != null) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.OBJECT_LEVEL_IS_NOT_VALID, null, locale));
            }
            plan.setObjectLevel(null);
        }
        plan.setChannelTypeId(request.getChannelTypeId());
        plan.setName(request.getPlanName().trim());
        plan.setFrequency(request.getFrequency());
        plan.setFrequencyUnit(request.getFrequencyUnit());
        plan.setCreatedDate(new Date());
        plan.setLastUpdate(new Date());
        plan.setStatus(Constants.STATUS_ACTIVE);
        planRepository.save(plan);
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    public ResponseEntity<?> updatePlan(PlanRequest request) {
        Plan plan = planRepository.findById(request.getPlanId()).orElse(null);
        Plan planCheckChannelTypeAndObjectLevel = planRepository.
                getDataPlanByChannelTypeAndObjectLevel(PDV, request.getObjectLevel());
        if (planCheckChannelTypeAndObjectLevel != null && !planCheckChannelTypeAndObjectLevel.getPlanId().equals(request.getPlanId())) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value())
                    , messageSource.getMessage(MessageKey.CHANNEL_TYPE_WITH_THIS_OBJECT_LEVEL_ALREADY_EXISTS, null, locale));
        }
        if (StringUtils.isEmpty(String.valueOf(request.getChannelTypeId()))) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value())
                    , messageSource.getMessage(MessageKey.CHANNEL_TYPE_NOT_NULL, null, locale));
        }
        if (StringUtils.isEmpty(request.getPlanName().trim())) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value())
                    , messageSource.getMessage(MessageKey.PLAN_NAME_NOT_NULL, null, locale));
        }
        ChannelType channelType = channelTypeRepository.findById(request.getChannelTypeId()).orElse(null);
        if (channelType.getObjectType().equals(SHOP)) {
            if (request.getObjectType() != SHOPPL) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.OBJECT_TYPE_FALSE, null, locale));
            }
            plan.setObjectType(request.getObjectType());
        } else {
            if (request.getObjectType() != STAFFPL) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.OBJECT_TYPE_FALSE, null, locale));
            }
            plan.setObjectType(request.getObjectType());
        }
        Plan planCheck = planRepository.getQuantityPlanByPlanName(request.getPlanName().trim());
        if (planCheck != null && !planCheck.getPlanId().equals(request.getPlanId())) {
            return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                    messageSource.getMessage(MessageKey.PLAN_NAME_IS_EXISTED, null, locale));
        }
        if (channelType.getChannelTypeId().equals(PDV)) {
            if (request.getObjectLevel() == null || request.getObjectLevel().toString().isEmpty()) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.OBJECT_LEVEL_NOT_NULL, null, locale));
            }
            plan.setObjectLevel(request.getObjectLevel());
        } else {
            if (request.getObjectLevel() != null) {
                return responseFactory.error(HttpStatus.OK, String.valueOf(HttpStatus.OK.value()),
                        messageSource.getMessage(MessageKey.OBJECT_LEVEL_IS_NOT_VALID, null, locale));
            }
            plan.setObjectLevel(null);
        }
        plan.setLastUpdate(new Date());
        plan.setObjectType(request.getObjectType());
        plan.setChannelTypeId(request.getChannelTypeId());
        plan.setName(request.getPlanName().trim());
        plan.setFrequency(request.getFrequency());
        plan.setFrequencyUnit(request.getFrequencyUnit());
        plan.setStatus(Constants.STATUS_ACTIVE);
        plan.setStatus(request.getStatus());
        planRepository.save(plan);
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteChannelPlan(PlanRequest request) {
        List<PlanJob> planJobs = planJobRepository.checkPlanJobUsingManagementPlan(request.getPlanId());
        Plan plan = planRepository.findById(request.getPlanId()).orElse(null);
        if (plan == null) {
            return responseFactory.error(HttpStatus.NOT_FOUND, String.valueOf(HttpStatus.NOT_FOUND.value()), MessageKey.PLAN_CHANNEL_NOT_FOUND);
        }
        if(planJobs.isEmpty()){
            planRepository.delete(plan);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        for (PlanJob planJob : planJobs) {
            PlanResult planResult = planResultRepository.checkPlanResultUsingAssignChecklist(planJob.getPlanJobId());
            if (planResult != null) {
                planResult.setStatus(Constants.INACTIVE);
                planResultRepository.save(planResult);
                for (PlanJob planJob1 : planJobs) {
                    planJob1.setStatus(Constants.INACTIVE);
                    planJobRepository.save(planJob);
                }
                plan.setStatus(Constants.INACTIVE);
                planRepository.save(plan);
                return new ResponseEntity<>(messageSource.getMessage(MessageKey.PLAN_IS_USE_AND_SET_STATUS_INACTIVE,
                        null, locale), HttpStatus.OK);
            }
        }
        planJobRepository.deleteAll(planJobs);
        planRepository.delete(plan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
