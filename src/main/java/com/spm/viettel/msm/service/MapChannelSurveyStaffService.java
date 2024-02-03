package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.BranchBcDTO;
import com.spm.viettel.msm.dto.SurveyConfig;
import com.spm.viettel.msm.dto.request.SearchSurveyRequest;
import com.spm.viettel.msm.repository.sm.MapChannelSurveyStaffRepository;
import com.spm.viettel.msm.repository.sm.StaffRepository;
import com.spm.viettel.msm.repository.sm.entity.MapChannelSurveyStaff;
import com.spm.viettel.msm.repository.sm.entity.Shop;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import com.spm.viettel.msm.utils.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MapChannelSurveyStaffService {
    private final org.slf4j.Logger loggerFactory = LoggerFactory.getLogger(ItemConfigService.class);
    @Autowired
    private MapChannelSurveyStaffRepository mapChannelSurveyStaffRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffRepository staffRepository;

    public List<MapChannelSurveyStaff> searchSurveyConfig(SearchSurveyRequest request){
        return mapChannelSurveyStaffRepository.searchSurveyConfig(request.getBranchId()
                , request.getBcId()
                , request.getUserId()
                , request.getChannelCode()
                , request.getStatus()
                , request.getFromDate()
                , request.getToDate());
    }

    public Date isValidDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public void pvdImportSurveyac(List<SurveyConfig> surveyConfigData, List<MapChannelSurveyStaff> surveyStaffs, Locale locale){
        Set<SurveyConfig> duplicateRows = new HashSet<>();
        for(int i = 0; i< surveyConfigData.size(); i++){
            SurveyConfig survey = surveyConfigData.get(i);
            String brCode = StringUtils.trim(survey.getBrCode());
            String bcCode = StringUtils.trim(survey.getBcCode());
            String staffCode = StringUtils.trim(survey.getStaffCode());
            String channelCode = StringUtils.trim(survey.getChannelCode());
            String start_vote_date = StringUtils.trim(survey.getStart_vote_date());
            Date dateNow = new Date();
            Date start_vote_dateTest = isValidDateFormat(start_vote_date);
            if (StringUtils.isEmpty(brCode)) {
                survey.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BR_CODE_REQUIRED, null,locale));
            } else if (StringUtils.isEmpty(bcCode)) {
                survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_BC_CODE_IS_REQUIRED,  null,locale));
            }else if (StringUtils.isEmpty(staffCode)) {
                survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_STAFF_CODE_IS_REQUIRED,  null,locale));
            }else if (StringUtils.isEmpty(channelCode)) {
                survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_CHANNEL_CODE_IS_REQUIRED,  null,locale));
            } else if (StringUtils.isEmpty(start_vote_date)) {
                survey.setComment(messageSource.getMessage(MessageKey.COMMON_START_DATE_INVALID,  null,locale));
            } else if (isValidDateFormat(start_vote_date) == null) {
                survey.setComment(messageSource.getMessage(MessageKey.COMMON_START_DATE_INVALID,  null,locale));
            } else if (duplicateRows.contains(survey)) {
                survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_DUPLICATE_ROW,  null,locale));
            } else if (isValidDateFormat(start_vote_date).getTime() < new Date().getTime() && (new Date().getTime() - isValidDateFormat(start_vote_date).getTime()) > 0 ) {
                survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_DATE_IS_AFTER_NOW,  null,locale));
            } else {
                Shop bc = shopService.getShopByCode(bcCode);
                Shop br = shopService.getShopByCode(brCode);
                Staff pdv = staffRepository.findByStaffCode(channelCode);
                Staff ac = staffRepository.findByStaffCode(staffCode);
                if(br == null){
                    survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_BR_CODE_IS_DO_NOT_EXIST,  null,locale));
                } else if(bc == null){
                    survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_BC_CODE_IS_DO_NOT_EXIST,  null,locale));
                } else if(ac == null){
                    survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_AC_CODE_IS_DO_NOT_EXIST,  null,locale));
                } else if(pdv == null){
                    survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_CHANNEL_CODE_IS_DO_NOT_EXIST,  null,locale));
                } else if (!bc.getParentShopId().equals(br.getShopId())){
                    survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_BR_OR_BC_IS_NOT_THE_SAME_SHOP,  null,locale));
                } else if (!ac.getShopId().equals(bc.getShopId())) {
                    survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_AC_IS_NOT_THE_SAME_SHOP_BC,  null,locale));
                } else if (!pdv.getShopId().equals(bc.getShopId())) {
                    survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_AC_IS_NOT_THE_SAME_SHOP_BC,  null,locale));
                } else if (!pdv.getStaffOwnerId().equals(ac.getStaffId())) {
                    survey.setComment(messageSource.getMessage(MessageKey.SURVEY_IMPORT_CONFIG_AC_AND_PDV_NO_CONNECTION,  null,locale));
                }
                if(survey.getComment() == null || survey.getComment().equals("") || survey.getComment().isEmpty()){
                    MapChannelSurveyStaff surveyStaff = processExcelData(survey);
                    if(surveyStaff != null){
                        surveyStaffs.add(surveyStaff);
                        duplicateRows.add(survey);
                    }
                }
            }
        }
    }

    public MapChannelSurveyStaff convertBeantOEntityMapChannelSurveyStaff(SurveyConfig surveyBean){
        MapChannelSurveyStaff resultEntity = new MapChannelSurveyStaff();
        Boolean checkInput = true;
        BranchBcDTO shop = null;
        Staff pvd = staffRepository.findFirstByStaffCodeIgnoreCase(surveyBean.getStaffCode());
        Staff ac = staffRepository.findByStaffOwnerId(pvd.getStaffOwnerId());
        BranchBcDTO shopbcOfPdv = shopService.getBranchBcOfShop(pvd.getShopId());
        BranchBcDTO shopbcOfAc = shopService.getBranchBcOfShop(ac.getShopId());
        if(pvd.getShopId().equals(ac.getShopId()) || shopbcOfPdv.getBcId() == shopbcOfAc.getBcId()){
            shop = shopbcOfPdv;
        }
        if(pvd == null){
            checkInput = false;
        } else if (ac == null || !ac.getStaffCode().equals(surveyBean.getStaffCode())) {
            checkInput = false;
        } else if (!shop.getBcCode().equals(surveyBean.getBcCode())) {
            checkInput = false;
        } else if (!shop.getBranchCode().equals(surveyBean.getBrCode())) {
            checkInput = false;
        } else if (checkInput && shop !=null) {
            resultEntity.setBranchCode(shop.getBranchCode());
            resultEntity.setBranchId(shop.getBranchId());
            resultEntity.setBcId(shop.getBcId());
            resultEntity.setBcCode(shop.getBcCode());
            resultEntity.setChannelId(pvd.getStaffId());
            resultEntity.setChannelCode(pvd.getStaffCode());
            resultEntity.setUserId(ac.getStaffId());
            resultEntity.setUserCode(ac.getStaffCode());
            resultEntity.setStatus(0l);
            resultEntity.setCreatedDate(new Date());
            resultEntity.setStart_vote_date(isValidDateFormat(surveyBean.getStart_vote_date()));
        }
        return resultEntity;
    }

    public MapChannelSurveyStaff processExcelData(SurveyConfig data) {
        StringBuilder sqlCount = new StringBuilder();
        MapChannelSurveyStaff surveyStaff;
        try {
            surveyStaff = convertBeantOEntityMapChannelSurveyStaff(data);
            if (surveyStaff != null) {
                MapChannelSurveyStaff surveyStaff_DB = mapChannelSurveyStaffRepository.findToProcessExcelData(surveyStaff.getBranchId()
                        , surveyStaff.getBcId()
                        , surveyStaff.getUserId()
                        , surveyStaff.getChannelId()
                        , convertDateToString(surveyStaff.getStart_vote_date()));

                if(surveyStaff_DB != null){
                    surveyStaff = surveyStaff_DB;
                    surveyStaff.setCreatedDate(new Date());
                }
                return mapChannelSurveyStaffRepository.save(surveyStaff);
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    public String convertDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        return dateString;
    }
}
