package com.spm.viettel.msm.service;

import com.spm.viettel.msm.controller.EvaluationController;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.GetChannelCodeDto;
import com.spm.viettel.msm.repository.sm.entity.Shop;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationManagerService {

    private final Logger loggerFactory = LoggerFactory.getLogger(EvaluationController.class);
    private final ShopService shopService;
    private final MessageSource messageSource;
    private final StaffService staffService;

    @Value("${list_channel_type_id_is_not_import_location}")
    private String channelTypeIdCheck;

    public LocationManagerService(ShopService shopService, StaffService staffService, MessageSource messageSource) {
        this.shopService = shopService;
        this.staffService = staffService;
        this.messageSource = messageSource;
    }

    public List<TemplateUpdateLoationDto> readContentFile(List<TemplateUpdateLoationDto> dataExcel) {
        List<String> channelTypeIdChecks = Arrays.asList(channelTypeIdCheck.split(","));
        Locale locale = LocaleContextHolder.getLocale();
        List<String> channelCodes = dataExcel.stream().map(TemplateUpdateLoationDto::getFriendlyChannelCode).collect(Collectors.toList());
        List<StaffDto> listChannelByChannelCode = shopService.getListChannelByChannelCode(channelCodes);
        Set<String> uniqueChannelCodes = new HashSet<>();
        List<String> duplicatedChannelCodes = new ArrayList<>();
        for (String channelCode : channelCodes) {
            if (uniqueChannelCodes.contains(channelCode)) {
                duplicatedChannelCodes.add(channelCode);
            } else {
                uniqueChannelCodes.add(channelCode);
            }
        }
        for (int index = 0; index < dataExcel.size(); index++) {
            TemplateUpdateLoationDto dataFile = dataExcel.get(index);
            String channelCode = StringUtils.trim(dataFile.getChannelCode());
            Boolean listChannelCheck = listChannelByChannelCode.stream().anyMatch(lc -> lc.getCode().equals(channelCode.toUpperCase())
                    && channelTypeIdChecks.contains(String.valueOf(lc.getChannelTypeId())));
            if (StringUtils.isEmpty(channelCode)) {
                dataFile.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_CHANNEL_CODE_IS_REQUIRED, null, locale));
            }else if(duplicatedChannelCodes.contains(channelCode)) {
                dataFile.setComment(messageSource.getMessage(MessageKey.CHANNEL_CODE_IS_NOT_REPEATED, null, locale));
            }else if (dataFile.getLongitude() == null) {
                dataFile.setComment(messageSource.getMessage(MessageKey.LONGITUDE_IS_REQUIRED, null, locale));
            } else if (dataFile.getLatitude() == null) {
                dataFile.setComment(messageSource.getMessage(MessageKey.LATIDUDE_IS_REQUIRED, null, locale));
            } else if (dataFile.getRadius() == null) {
                dataFile.setComment(messageSource.getMessage(MessageKey.RADIUS_IS_REQUIRED, null, locale));
            }else if (dataFile.getRadius() < 0) {
                dataFile.setComment(messageSource.getMessage(MessageKey.RADIUS_MUST_BE_POSITIVE, null, locale));
            } else if (dataFile.getLatitude() < -90 || dataFile.getLatitude() > 90) {
                dataFile.setComment(messageSource.getMessage(MessageKey.LATITUDE_IS_NOT_SPECIFIED_IN_DEGREES_WITHIN_THE_RANGE, null, locale));
            } else if (dataFile.getLongitude() < -180 || dataFile.getLongitude() >= 180) {
                dataFile.setComment(messageSource.getMessage(MessageKey.LONGITUDE_IS_NOT_SPECIFIED_IN_DEGREES_WITHIN_THE_RANGE, null, locale));
            } else if (CollectionUtils.isEmpty(listChannelByChannelCode)) {
                dataFile.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_CHANNEL_CODE_IS_DO_NOT_EXIST, null, locale));
            } else if (listChannelCheck) {
                dataFile.setComment(messageSource.getMessage(MessageKey.CHANNEL_TYPE_IS_NOT_UPDATE, null, locale));
            } else {
                StaffDto channel = listChannelByChannelCode.stream().filter(v -> v.getCode().equalsIgnoreCase(channelCode)).findFirst().orElse(null);
                if (channel == null) {
                    dataFile.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_CHANNEL_CODE_IS_DO_NOT_EXIST, null, locale));
                } else {
                    dataFile.setChannelCodeId(channel.getId());
                    dataFile.setObjectType(channel.getObjectType());
                }
            }
        }
        return dataExcel;
    }

    public List<TemplateUpdateLoationDto> updateLocationsForObjectType(List<TemplateUpdateLoationDto> dataNoComment, UserTokenDto userInfo) {
        List<TemplateUpdateLoationDto> dataResult = new ArrayList<>();
        List<TemplateUpdateLoationDto> listStaffUpdate = new ArrayList<>();
        List<TemplateUpdateLoationDto> listShopUpdate = new ArrayList<>();
        try {
            for (int index = 0; index < dataNoComment.size(); index++) {
                TemplateUpdateLoationDto dataFile = dataNoComment.get(index);
                if (dataFile.getObjectType().equals("2")) {
                    listStaffUpdate.add(dataFile);
                } else if (dataFile.getObjectType().equals("1")) {
                    listShopUpdate.add(dataFile);
                }
            }
            List<TemplateUpdateLoationDto> dataShopUpdate = shopService.updateLocations(listShopUpdate, userInfo);
            List<TemplateUpdateLoationDto> dataStaffUpdate = staffService.updateLocations(listStaffUpdate, userInfo);
            dataResult.addAll(dataShopUpdate);
            dataResult.addAll(dataStaffUpdate);
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        return dataResult;
    }

    public List<?> getChannelTypeByChannelType(GetChannelCodeDto req) {
        if (req.getChannelTypeId() != null && req.getObjectType() == 2 && req.getShopId() != null) {
            List<StaffDto> staffList = staffService.getStaffByShopIdAndChannelTypeId(req);
            return staffList;
        } else if (req.getChannelTypeId() != null && req.getObjectType() == 1 && req.getShopId() != null) {
            List<Shop> shopList = shopService.getShopByParrentIdAndChannelTypeId(req);
            return shopList;
        }
        return null;
    }
}
