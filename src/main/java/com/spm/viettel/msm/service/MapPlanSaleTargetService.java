package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.PlanSaleSearchRequest;
import com.spm.viettel.msm.dto.response.GeneralResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.MapPlanSaleTargetRepository;
import com.spm.viettel.msm.repository.sm.entity.MapPlanSaleTarget;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.JasperReportExporter;
import com.spm.viettel.msm.utils.MessageKey;
import com.spm.viettel.msm.utils.NumberUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.internal.SessionImpl;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapPlanSaleTargetService {
    @Autowired
    private MapPlanSaleTargetRepository mapPlanSaleTargetRepository;
    @Autowired
    private ShopTreeService shopTreeService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Value("${TEMPLATE_PATH}")
    private String templateFolder;
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    public MapPlanSaleTarget findSaleTargetByBrCodeAndTargetLevelAndPlanDate(String brCode, Integer targetLevel, Date planDate){
        MapPlanSaleTarget mapPlanSaleTarget = mapPlanSaleTargetRepository.findSaleTargetByBrCodeAndTargetLevelAndPlanDate(brCode, targetLevel, planDate);
        return mapPlanSaleTarget;
    }

    public MapPlanSaleTarget findSaleTargetByBcCodeAndTargetLevelAndPlanDate(String brCode, Integer targetLevel, Date planDate){
        MapPlanSaleTarget mapPlanSaleTarget = mapPlanSaleTargetRepository.findSaleTargetByBcCodeAndTargetLevelAndPlanDate(brCode, targetLevel, planDate);
        return mapPlanSaleTarget;
    }

    public List<PlanSaleTargetDto> search(PlanSaleSearchRequest request){
         List<PlanSaleTargetDto> list = mapPlanSaleTargetRepository.search(request.getBrCode()
                , request.getBcCode()
                , request.getPlanType()
                , request.getTargetLevel()
                , request.getStaffCode()
                , request.getPlanDate());

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            PlanSaleTargetDto summary = new PlanSaleTargetDto();
            if (request.getTargetLevel() == 1) {
                summary.setBrCode(Constants.TOTAL);
            } else if (request.getTargetLevel() == 2) {
                summary.setBrCode(request.getBrCode());
                summary.setBcCode(Constants.TOTAL);
            } else if (request.getTargetLevel() == 3) {
                summary.setBrCode(request.getBrCode());
                summary.setBcCode(request.getBcCode());
                summary.setStaffCode(Constants.TOTAL);
            }
            for (PlanSaleTargetDto t : list) {
                summary.setT1Target(NumberUtils.plusInt(summary.getT1Target(), t.getT1Target()));
                summary.setT2Target(NumberUtils.plusInt(summary.getT2Target(), t.getT2Target()));
                summary.setT3Target(NumberUtils.plusInt(summary.getT3Target(), t.getT3Target()));
                summary.setT4Target(NumberUtils.plusInt(summary.getT4Target(), t.getT4Target()));
                summary.setT5Target(NumberUtils.plusInt(summary.getT5Target(), t.getT5Target()));
                summary.setT6Target(NumberUtils.plusInt(summary.getT6Target(), t.getT6Target()));
                summary.setT7Target(NumberUtils.plusInt(summary.getT7Target(), t.getT7Target()));
                summary.setT8Target(NumberUtils.plusInt(summary.getT8Target(), t.getT8Target()));
                summary.setT9Target(NumberUtils.plusInt(summary.getT9Target(), t.getT9Target()));
                summary.setT10Target(NumberUtils.plusInt(summary.getT10Target(), t.getT10Target()));
                summary.setT11Target(NumberUtils.plusInt(summary.getT11Target(), t.getT11Target()));
                summary.setT12Target(NumberUtils.plusInt(summary.getT12Target(), t.getT12Target()));
                summary.setTotalTarget(NumberUtils.plusInt(summary.getTotalTarget(), t.getTotalTarget()));

                summary.setT1Plan(NumberUtils.plusInt(summary.getT1Plan(), t.getT1Plan()));
                summary.setT2Plan(NumberUtils.plusInt(summary.getT2Plan(), t.getT2Plan()));
                summary.setT3Plan(NumberUtils.plusInt(summary.getT3Plan(), t.getT3Plan()));
                summary.setT4Plan(NumberUtils.plusInt(summary.getT4Plan(), t.getT4Plan()));
                summary.setT5Plan(NumberUtils.plusInt(summary.getT5Plan(), t.getT5Plan()));
                summary.setT6Plan(NumberUtils.plusInt(summary.getT6Plan(), t.getT6Plan()));
                summary.setT7Plan(NumberUtils.plusInt(summary.getT7Plan(), t.getT7Plan()));
                summary.setT8Plan(NumberUtils.plusInt(summary.getT8Plan(), t.getT8Plan()));
                summary.setT9Plan(NumberUtils.plusInt(summary.getT9Plan(), t.getT9Plan()));
                summary.setT10Plan(NumberUtils.plusInt(summary.getT10Plan(), t.getT10Plan()));
                summary.setT11Plan(NumberUtils.plusInt(summary.getT11Plan(), t.getT11Plan()));
                summary.setT12Plan(NumberUtils.plusInt(summary.getT12Plan(), t.getT12Plan()));
                summary.setTotalPlan(NumberUtils.plusInt(summary.getTotalPlan(), t.getTotalPlan()));
                summary.setT1Result(NumberUtils.plusInt(summary.getT1Result(), t.getT1Result()));
                summary.setT2Result(NumberUtils.plusInt(summary.getT2Result(), t.getT2Result()));
                summary.setT3Result(NumberUtils.plusInt(summary.getT3Result(), t.getT3Result()));
                summary.setT4Result(NumberUtils.plusInt(summary.getT4Result(), t.getT4Result()));
                summary.setT5Result(NumberUtils.plusInt(summary.getT5Result(), t.getT5Result()));
                summary.setT6Result(NumberUtils.plusInt(summary.getT6Result(), t.getT6Result()));
                summary.setT7Result(NumberUtils.plusInt(summary.getT7Result(), t.getT7Result()));
                summary.setT8Result(NumberUtils.plusInt(summary.getT8Result(), t.getT8Result()));
                summary.setT9Result(NumberUtils.plusInt(summary.getT9Result(), t.getT9Result()));
                summary.setT10Result(NumberUtils.plusInt(summary.getT10Result(), t.getT10Result()));
                summary.setT11Result(NumberUtils.plusInt(summary.getT11Result(), t.getT11Result()));
                summary.setT12Result(NumberUtils.plusInt(summary.getT12Result(), t.getT12Result()));
                summary.setTotalResult(NumberUtils.plusInt(summary.getTotalResult(), t.getTotalResult()));
            }
            list.add(summary);
        }
        return list;
    }

    public void importPlanSale(List<MapPlanSaleTarget> targets, List<MapPlanSaleTarget> listTargetDelete, List<MapPlanSaleTarget> listTargetOk, String brCode, String bcCode, Integer targetLevel, String planDate, UserTokenDto userToken, Locale locale){
        try{
            List<ShopTreeDTO> branches = shopTreeService.listShopTree(Constants.VTP_SHOP_ID, 3, Constants.VTP_SHOP_ID, null);
            List<ShopTreeDTO> bcList = new ArrayList<>();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(brCode)) {
                List<ShopTreeDTO> brs = branches.stream().filter(b -> b.getCode().equalsIgnoreCase(brCode)).collect(Collectors.toList());
                bcList = shopTreeService.listShopTree(brs.get(0).getShopId(), 4, null, null);
            }
            MapPlanSaleTarget targetBr = null;
            MapPlanSaleTarget targetBc = null;
            List<StaffBaseDto> listStaffs = new ArrayList<>();
            Date planDateYear = DateUtils.parseDate(planDate, "YYYY");
            String currentUserCode = userToken.getStaffCode();
            if (targetLevel == 2) {
                targetBr = mapPlanSaleTargetRepository.findSaleTargetByBrCodeAndTargetLevelAndPlanDate(brCode, 1, planDateYear);
            } else if (targetLevel == 3) {
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(bcCode)) {
                    List<ShopTreeDTO> bcs = bcList.stream().filter(b -> b.getCode().equalsIgnoreCase(bcCode)).collect(Collectors.toList());
                    listStaffs = staffService.getListEmployeeOfShop(bcs.get(0).getShopId(), 1, Constants.USER.BR_ASISTENTE_FIJO, null, null);
                    listStaffs.addAll(staffService.getListEmployeeOfShop(bcs.get(0).getShopId(), 1, Constants.USER.COLABORADOR_AC, null, null));
                    listStaffs.addAll(staffService.getListEmployeeOfShop(bcs.get(0).getShopId(), 0, Constants.USER.CE_DIRECTOR, null, null));
                    listStaffs.addAll(staffService.getListEmployeeOfShop(bcs.get(0).getShopId(), 0, Constants.USER.BR_DIRECTOR, null, null));
                    listStaffs.addAll(staffService.getListEmployeeOfShop(bcs.get(0).getShopId(), 0, Constants.USER.BR_SUB_DIRECTOR, null, null));
                }
                targetBc = mapPlanSaleTargetRepository.findSaleTargetByBcCodeAndTargetLevelAndPlanDate(bcCode, 2, planDateYear);
            }
            Date currentDateTime = new Date();
            int numberOfLines = targets.size() - 1;
            for (int index = 0; index < numberOfLines; index++) {
                MapPlanSaleTarget t = targets.get(index);
                t.setTargetLevel(targetLevel);
                t.setPlanDate(planDateYear);
                t.setStatus(Constants.STATUS_DRAFT);
                t.setCreateDate(currentDateTime);
                t.setCreateUser(currentUserCode);
                t.setUpdateDate(currentDateTime);
                t.setUpdateUser(currentUserCode);
                Calendar cal = Calendar.getInstance();
                int currentMonth = cal.get(Calendar.MONTH);
                int currentYear = cal.get(Calendar.YEAR);
                int planYear = Integer.valueOf(planDate);
                if (planYear > currentYear) {
                    currentMonth = 0;
                }
                if (planYear < currentYear) {
                    currentMonth = 12;
                }
                if (targetLevel == 1) {
                    validateTarget(t, currentMonth, locale);
                    for (int k = 0; k < listTargetOk.size(); k++) {
                        MapPlanSaleTarget channelTarget = listTargetOk.get(k);
                        if (channelTarget.getBrCode().equalsIgnoreCase(t.getBrCode())) {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_DUPLICATE_DATA, null, locale));
                            break;
                        }
                    }
                    List<ShopTreeDTO> brs = branches.stream().filter(b -> b.getCode().equalsIgnoreCase(t.getBrCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(brs)) {
                        t.setComment(messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_BR_CODE_INVALID, null,locale));
                    }
                    if (t.getTotalTarget() != null && t.getTotalTarget() < 0) {
                        t.setComment(commentBuild(t.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_INVALID, null, locale)));
                    }
                    t.setPlanType(1); // theo nam
                    buildTargetByMonth(t, t, true, planDate);
                    if (org.apache.commons.lang3.StringUtils.isEmpty(t.getComment())) {
                        MapPlanSaleTarget targetInDb = mapPlanSaleTargetRepository.findSaleTargetByBrCodeAndTargetLevelAndPlanDate(t.getBrCode(), targetLevel, planDateYear);
                        if (targetInDb != null) {
                            targetInDb.setTotalTarget(t.getTotalTarget());
                            buildTargetByMonth(t, targetInDb, false, planDate);
                            targetInDb.setUpdateDate(currentDateTime);
                            targetInDb.setUpdateUser(currentUserCode);
                            if (targetInDb.isNoData()) {
                                listTargetDelete.add(targetInDb);
                            } else {
                                listTargetOk.add(targetInDb);
                            }
                        } else {
                            if (t.isNoData()) {
                                t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_NO_DATA, null, locale));
                                continue;
                            }
                            listTargetOk.add(t);
                        }
                    }
                }else if (targetLevel == 2 && org.apache.commons.lang3.StringUtils.isEmpty(t.getComment())) {
                    validateTarget(t, currentMonth, locale);
                    for (int k = 0; k < listTargetOk.size(); k++) {
                        MapPlanSaleTarget channelTarget = listTargetOk.get(k);
                        if (channelTarget.getBrCode().equalsIgnoreCase(brCode)
                                && channelTarget.getBcCode().equalsIgnoreCase(t.getBcCode())) {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_DUPLICATE_DATA, null, locale));
                            break;
                        }
                    }
                    if (org.apache.commons.lang3.StringUtils.isEmpty(t.getComment())) {
                        List bcLstFilter = bcList.stream().filter(bc -> bc.getCode().equalsIgnoreCase(t.getBcCode())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(bcLstFilter)) {
                            t.setComment(commentBuild(t.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_BC_CODE_INVALID, null, locale)));
                        } else {
                            MapPlanSaleTarget tBc = mapPlanSaleTargetRepository.findSaleTargetByBcCodeAndTargetLevelAndPlanDate(t.getBcCode(), targetLevel, planDateYear);
                            buildTargetByMonth(t, t, true, planDate);
                            if (targetBr != null) {
                                t.setParentId(targetBr.getId());
                            }
                            t.setBrCode(brCode);
                            t.setPlanType(2); // theo thang

                            if (tBc != null) {
                                buildTargetByMonth(t, tBc, false, planDate);
                                tBc.setUpdateDate(currentDateTime);
                                tBc.setUpdateUser(currentUserCode);
                                if (tBc.isNoData()){
                                    listTargetDelete.add(tBc);
                                }else {
                                    listTargetOk.add(tBc);
                                }
                            } else {
                                if (t.isNoData()) {
                                    t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_NO_DATA, null, locale));
                                    continue;
                                }
                                listTargetOk.add(t);
                            }
                        }
                    }
                } else if (targetLevel == 3) {
                    validateTarget(t, currentMonth, locale);
                    for (int k = 0; k < listTargetOk.size(); k++) {
                        MapPlanSaleTarget channelTarget = listTargetOk.get(k);
                        if (channelTarget.getBrCode().equalsIgnoreCase(brCode)
                                && channelTarget.getBcCode().equalsIgnoreCase(bcCode)
                                && channelTarget.getStaffCode().equalsIgnoreCase(t.getStaffCode())) {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_DUPLICATE_DATA, null, locale));
                            break;
                        }
                    }
                    if (org.apache.commons.lang3.StringUtils.isEmpty(t.getComment())) {
                        String staffCode = org.apache.commons.lang3.StringUtils.trim(t.getStaffCode());
                        List<StaffBaseDto> afLstFilter = listStaffs.stream().filter(s -> s.getCode().equalsIgnoreCase(staffCode)).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(afLstFilter)) {
                            t.setComment(commentBuild(t.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_STAFF_CODE_INVALID, null, locale)));
                        } else {
                            MapPlanSaleTarget tStaff = mapPlanSaleTargetRepository.findSaleTargetByBcCodeAndStaffCodeAndTargetLevelAndPlanDate(bcCode, t.getStaffCode(), targetLevel, planDateYear);
                            buildTargetByMonth(t, t, true, planDate);
                            if (targetBc != null) {
                                t.setParentId(targetBc.getId());
                            }
                            t.setBrCode(brCode);
                            t.setBcCode(bcCode);
                            t.setStaffCode(staffCode.toUpperCase());
                            t.setPlanType(2); // theo thang
                            t.setStatus(Constants.STATUS_APPROVED);
                            t.setStaffId(afLstFilter.get(0).getId());
                            if (tStaff != null) {
                                buildTargetByMonth(t, tStaff, false, planDate);
                                tStaff.setUpdateDate(currentDateTime);
                                tStaff.setUpdateUser(currentUserCode);
                                if (tStaff.isNoData()){
                                    listTargetDelete.add(tStaff);
                                }else {
                                    listTargetOk.add(tStaff);
                                }
                            } else {
                                if (t.isNoData()) {
                                    t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_NO_DATA, null, locale));
                                    continue;
                                }
                                listTargetOk.add(t);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String commentBuild(String s1, String s2) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s1)) {
            return s2;
        } else {
            return s1 + ", " + s2;
        }
    }

    public void validateTarget(MapPlanSaleTarget target, int month, Locale locale) {
        switch (month) {
            case 0:
                if (target.getT1Target() != null && target.getT1Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JAN, null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 1:
                if (target.getT2Target() != null && target.getT2Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_FEB,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 2:
                if (target.getT3Target() != null && target.getT3Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_MAR,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 3:
                if (target.getT4Target() != null && target.getT4Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_APR,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 4:
                if (target.getT5Target() != null && target.getT5Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_MAY,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 5:
                if (target.getT6Target() != null && target.getT6Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JUN,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 6:
                if (target.getT7Target() != null && target.getT7Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JUL,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 7:
                if (target.getT8Target() != null && target.getT8Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_AUG,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 8:
                if (target.getT9Target() != null && target.getT9Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_SEP,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 9:
                if (target.getT10Target() != null && target.getT10Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_OCT,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 10:
                if (target.getT11Target() != null && target.getT11Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_NOV,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
            case 11:
                if (target.getT12Target() != null && target.getT12Target() < Constants.TARGET_MIN) {
                    String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_DEC,null ,locale);
                    target.setComment(commentBuild(target.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_TARGET_MONTH_INVALID, new String[]{monthString}, locale)));
                }
        }
    }

    public static void buildTargetByMonth(MapPlanSaleTarget source, MapPlanSaleTarget dist, boolean monthInThePastToZero, String planDate) {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        int planYear = Integer.valueOf(planDate);
        if (planYear > currentYear) {
            currentMonth = 0;
        }
        if (planYear < currentYear) {
            currentMonth = 12;
        }
        switch (currentMonth) {
            case 0:
                dist.setT1Target(source.getT1Target());
                dist.setT2Target(source.getT2Target());
                dist.setT3Target(source.getT3Target());
                dist.setT4Target(source.getT4Target());
                dist.setT5Target(source.getT5Target());
                dist.setT6Target(source.getT6Target());
                dist.setT7Target(source.getT7Target());
                dist.setT8Target(source.getT8Target());
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 1:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                }
                dist.setT2Target(source.getT2Target());
                dist.setT3Target(source.getT3Target());
                dist.setT4Target(source.getT4Target());
                dist.setT5Target(source.getT5Target());
                dist.setT6Target(source.getT6Target());
                dist.setT7Target(source.getT7Target());
                dist.setT8Target(source.getT8Target());
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 2:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                }
                dist.setT3Target(source.getT3Target());
                dist.setT4Target(source.getT4Target());
                dist.setT5Target(source.getT5Target());
                dist.setT6Target(source.getT6Target());
                dist.setT7Target(source.getT7Target());
                dist.setT8Target(source.getT8Target());
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 3:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                }
                dist.setT4Target(source.getT4Target());
                dist.setT5Target(source.getT5Target());
                dist.setT6Target(source.getT6Target());
                dist.setT7Target(source.getT7Target());
                dist.setT8Target(source.getT8Target());
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 4:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                    dist.setT4Target(0);
                }
                dist.setT5Target(source.getT5Target());
                dist.setT6Target(source.getT6Target());
                dist.setT7Target(source.getT7Target());
                dist.setT8Target(source.getT8Target());
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 5:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                    dist.setT4Target(0);
                    dist.setT5Target(0);
                }
                dist.setT6Target(source.getT6Target());
                dist.setT7Target(source.getT7Target());
                dist.setT8Target(source.getT8Target());
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 6:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                    dist.setT4Target(0);
                    dist.setT5Target(0);
                    dist.setT6Target(0);
                }
                dist.setT7Target(source.getT7Target());
                dist.setT8Target(source.getT8Target());
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 7:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                    dist.setT4Target(0);
                    dist.setT5Target(0);
                    dist.setT6Target(0);
                    dist.setT7Target(0);
                }
                dist.setT8Target(source.getT8Target());
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 8:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                    dist.setT4Target(0);
                    dist.setT5Target(0);
                    dist.setT6Target(0);
                    dist.setT7Target(0);
                    dist.setT8Target(0);
                }
                dist.setT9Target(source.getT9Target());
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 9:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                    dist.setT4Target(0);
                    dist.setT5Target(0);
                    dist.setT6Target(0);
                    dist.setT7Target(0);
                    dist.setT8Target(0);
                    dist.setT9Target(0);
                }
                dist.setT10Target(source.getT10Target());
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 10:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                    dist.setT4Target(0);
                    dist.setT5Target(0);
                    dist.setT6Target(0);
                    dist.setT7Target(0);
                    dist.setT8Target(0);
                    dist.setT9Target(0);
                    dist.setT10Target(0);
                }
                dist.setT11Target(source.getT11Target());
                dist.setT12Target(source.getT12Target());
                break;
            case 11:
                if (monthInThePastToZero) {
                    dist.setT1Target(0);
                    dist.setT2Target(0);
                    dist.setT3Target(0);
                    dist.setT4Target(0);
                    dist.setT5Target(0);
                    dist.setT6Target(0);
                    dist.setT7Target(0);
                    dist.setT8Target(0);
                    dist.setT9Target(0);
                    dist.setT10Target(0);
                    dist.setT11Target(0);
                }
                dist.setT12Target(source.getT12Target());
                break;
        }
        dist.setParentId(source.getParentId());
        dist.setStatus(source.getStatus());
        dist.summaryTotalTarget();
    }

    @Transactional
    public ResponseEntity<GeneralResponse<List<MapPlanSaleTarget>>> saveAndResponse(List<MapPlanSaleTarget> targets, List<MapPlanSaleTarget> listTargetOk, List<MapPlanSaleTarget> listTargetDelete, ResponseEntity<GeneralResponse<List<MapPlanSaleTarget>>> response, Locale locale, Integer targetLevel, File file){
        if (listTargetOk.size() > 0 || CollectionUtils.isNotEmpty(listTargetDelete)) {
            List<MapPlanSaleTarget> targetHaveComment = listTargetOk.stream().filter(t -> StringUtils.isNotEmpty(t.getComment())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(targetHaveComment)) {
                try {
                    if (CollectionUtils.isNotEmpty(listTargetOk)) {
                        mapPlanSaleTargetRepository.saveAll(listTargetOk);
                    }
                    if (CollectionUtils.isNotEmpty(listTargetDelete)){
                        mapPlanSaleTargetRepository.saveAll(listTargetDelete);
                    }
                    if ((listTargetOk.size() + listTargetDelete.size()) < targets.size() - 1) {
                        response = responseFactory.error(
                                HttpStatus.OK
                                , String.valueOf(HttpStatus.OK.value())
                                , messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_RESULT, new String[]{String.valueOf(listTargetOk.size() + listTargetDelete.size()), String.valueOf(targets.size() - 1)}, locale)
                                , buildImportResultFile(targets, targetLevel));
                    }else {
                        response = responseFactory.error(
                                HttpStatus.OK
                                , String.valueOf(HttpStatus.OK.value())
                                , messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_RESULT, new String[]{String.valueOf(listTargetOk.size() + listTargetDelete.size()), String.valueOf(targets.size() - 1)}, locale));
                    }
                } catch (Exception ex) {
                    if (ex instanceof JDBCConnectionException) {
                        response = responseFactory.error(
                                HttpStatus.REQUEST_TIMEOUT
                                , Constants.ERROR
                                , messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_SQL_CONNECTION_ERROR, null, locale));
                    } else {
                        response = responseFactory.error(
                                HttpStatus.REQUEST_TIMEOUT
                                , Constants.ERROR
                                , ex.getMessage());
                    }
                }
            } else {
                response = responseFactory.error(
                        HttpStatus.OK
                        , String.valueOf(HttpStatus.OK.value())
                        , messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_RESULT, new String[]{"0", String.valueOf(targets.size() - 1)}, locale)
                        , buildImportResultFile(targets, targetLevel));
            }
        } else {
            response = responseFactory.error(
                    HttpStatus.OK
                    , String.valueOf(HttpStatus.OK.value())
                    , messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_RESULT, new String[]{"0", String.valueOf(targets.size() - 1)}, locale)
                    , buildImportResultFile(targets, targetLevel));
        }
        org.apache.commons.io.FileUtils.deleteQuietly(file);
        return response;
    }

    public String buildImportResultFile(List<MapPlanSaleTarget> saleTargets, int level) {
        String fileName = "import_sale_plan_target_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "Template_import_sale_plan_target_strategy_result.xls";
        if (level == 2) {
            fileTemplate = "Template_import_sale_plan_target_br_result.xls";
        } else if (level == 3) {
            fileTemplate = "Template_import_sale_plan_target_bc_result.xls";
        }
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", saleTargets);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String downloadSearch(PlanSaleSearchRequest request){
        List<PlanSaleTargetDto> planSaleTargetDtos = search(request);
        String fileName = "plan_sale_target_search_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = fileNameFullFolder + File.separator + fileName;
        String templateFileName = "Template_sale_plan_target_strategy_search.xls";
        if (request.getTargetLevel() == 2) {
            templateFileName = "Template_sale_plan_target_br_search.xls";
        } else if (request.getTargetLevel() == 3) {
            templateFileName = "Template_sale_plan_target_bc_search.xls";
        }
        File templateFile = new File(templateFolder, templateFileName);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", planSaleTargetDtos);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public String downloadSearchDetail(PlanSaleSearchRequest request) {
        String fileName = "sale_control_report_" + Calendar.getInstance().getTimeInMillis() + ".xlsx";
        String fileNameFull = fileNameFullFolder + File.separator + fileName;

        File templateFile = new File(templateFolder, "Sale_control.jrxml");
        Map<String, Object> params = new HashMap<>();
        params.put("p_year", request.getPlanDate());
        if (StringUtils.isNotEmpty(request.getBrCode())) {
            params.put("p_br", "AND BR_CODE ='" + request.getBrCode() + "'");
        } else {
            params.put("p_br", " ");
        }
        if (StringUtils.isNotEmpty(request.getBcCode())) {
            params.put("p_bc", "AND BC_CODE ='" + request.getBcCode() + "'");
        } else {
            params.put("p_bc", " ");
        }
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(templateFile.getAbsolutePath());
//            Connection sqlConnection = ((SessionImpl) session).connection();
            Connection connection = dataSource.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setDetectCellType(true); // Detect cell types (date and etc.)
            configuration.setWhitePageBackground(false); // No white background!
            configuration.setFontSizeFixEnabled(false);

            // No spaces between rows and columns
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setRemoveEmptySpaceBetweenColumns(true);

            JasperReportExporter instance = new JasperReportExporter();
            byte[] fileInBytes = instance.exportToXlsx(jasperPrint, "Sale Control");
            FileUtils.writeByteArrayToFile(new File(fileNameFull), fileInBytes);

            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
