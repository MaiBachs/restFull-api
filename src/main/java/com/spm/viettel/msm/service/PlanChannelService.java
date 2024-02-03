package com.spm.viettel.msm.service;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import com.spm.viettel.msm.Constant;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.ImportPlanChannelTargetRequest;
import com.spm.viettel.msm.dto.request.PlanChannelSearchRequest;
import com.spm.viettel.msm.dto.response.MapPlanChannelSearchListResponse;
import com.spm.viettel.msm.dto.response.PlanChannelTargetImportResponse;
import com.spm.viettel.msm.dto.response.PlanChannelTargetResponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.repository.sm.*;
import com.spm.viettel.msm.repository.sm.entity.*;
import com.spm.viettel.msm.utils.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.internal.SessionImpl;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;



@Service
public class PlanChannelService {
    public static final String TOTAL = "Total";
    private final Logger loggerFactory = LoggerFactory.getLogger(PlanChannelService.class);
    private final MapPlanChannelTargetRepository mapPlanChannelTargetRepository;
    private final ChannelTypeService channelTypeService;
    private final MapPlanChannelRepository mapPlanChannelRepository;
    private final AppParamService appParamService;
    private final ShopRepository shopRepository;
    private final ShopService shopService;
    private final StaffRepository staffRepository;
    private final MessageSource messageSource;
    private Locale locale = LocaleContextHolder.getLocale();
    private final ResponseFactory responseFactory;
    @Autowired
    private DataSource dataSource;
    @Getter(AccessLevel.NONE)
    private Map<String, String> headerMapper = new HashMap<>();
    @Setter(AccessLevel.NONE)
    private Map<String, String> headerMapperRevert = new HashMap<>();
    @Value("${TEMPLATE_PATH}")
    private String templateFolder;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    @Autowired
    public PlanChannelService(AuthenticateService authenticateService, MapPlanChannelTargetRepository mapPlanChannelTargetRepository, ChannelTypeService channelTypeService, MapPlanChannelRepository mapPlanChannelRepository, AppParamService appParamService, ShopRepository shopRepository, ShopService shopService, StaffRepository staffRepository, MessageSource messageSource, ResponseFactory responseFactory) {
        this.mapPlanChannelTargetRepository = mapPlanChannelTargetRepository;
        this.channelTypeService = channelTypeService;
        this.mapPlanChannelRepository = mapPlanChannelRepository;
        this.appParamService = appParamService;
        this.shopRepository = shopRepository;
        this.shopService = shopService;
        this.staffRepository = staffRepository;
        this.messageSource = messageSource;
        this.responseFactory = responseFactory;
    }

    public ResponseEntity<?> executeImportPlanChannel(File file,ImportPlanChannelTargetRequest request, UserTokenDto userTokenDto) throws ParseException {
        ResponseEntity<?> response = null;
        Date planDateYear = DateUtils.parseDate(request.getPlanDate(), "YYYY");

        try {
            PlanChannelTargetImportResponse result = new PlanChannelTargetImportResponse();
            List<ShopTreeDTO> branches = shopService.listShopTree(Constants.VTP_SHOP_ID, 3,
                    Constants.VTP_SHOP_ID, null);
            List<ShopTreeDTO> bcList = new ArrayList<>();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(request.getBrCode())) {
                List<ShopTreeDTO> brs = branches.stream().filter(b -> b.getCode().equalsIgnoreCase(request.getBrCode()))
                        .collect(Collectors.toList());
                bcList = shopService.listShopTree(brs.get(0).getShopId(), 4, null, null);
            }
            List<StaffBaseDto> listAfStaffs = new ArrayList<>();
            List<StaffBaseDto> listACStaffs = new ArrayList<>();
            List<StaffBaseDto> listCeDirectorStaffs = new ArrayList<>();
            List<StaffBaseDto> listStaffs = new ArrayList<>();
            List<MapPlanChannelTarget> targetsBrLeve1 = new ArrayList<>();
            List<MapPlanChannelTarget> targetsBrLeve2 = new ArrayList<>();
            if (request.getTargetLevel() == 3) {
                List<ShopTreeDTO> bcs = bcList.stream().filter(b -> b.getCode().equalsIgnoreCase(request.getBcCode())).collect(Collectors.toList());
                listAfStaffs = staffRepository.getListEmployeeOfShop(bcs.get(0).getShopId(), 0,null,0, Constants.POS_CODE_AF);
                listStaffs.addAll(listAfStaffs);
                listACStaffs = staffRepository.getListEmployeeOfShop(bcs.get(0).getShopId(), 0, Constants.POS_CODE_AC,0, null);
                listStaffs.addAll(listACStaffs);
                listCeDirectorStaffs = staffRepository.getListEmployeeOfShop(bcs.get(0).getShopId(), 0, Constants.USER.CE_DIRECTOR,0, null);
                listCeDirectorStaffs.addAll(staffRepository.getListEmployeeOfShop(bcs.get(0).getShopId(), 0, Constants.USER.BR_DIRECTOR,0, null));
                listCeDirectorStaffs.addAll(staffRepository.getListEmployeeOfShop(bcs.get(0).getShopId(), 0, Constants.USER.BR_SUB_DIRECTOR,0, null));
                listStaffs.addAll(listCeDirectorStaffs);
            } else if (request.getTargetLevel() == 2) {
//                if (targetType == Constants.TARGET_TYPE_DEVELOP_NEW) {
//                    targetsBrLeve1 = PlanChannelBiz.findTargetByBrCodeAndTargetLevelAndPlanDate(sessionSM, brCode, 1, planDateYear);
//                    targetsBrLeve2 = PlanChannelBiz.findTargetByBrCodeAndTargetLevelAndPlanDate(sessionSM, brCode, 2, planDateYear);
//                }else if (targetType == Constants.TARGET_TYPE_ACCUMULATED){
                targetsBrLeve1 = findTargetByBrCodeAndTargetLevelAndPlanDate(request.getBrCode(), 1, planDateYear, request.getTargetType());
                targetsBrLeve2 = findTargetByBrCodeAndTargetLevelAndPlanDate(request.getBrCode(), 2, planDateYear, request.getTargetType());
//                }
            }
            result.setChannelTypes(channelTypeService.getListChannelCanPlanToDevelop());

            for (int k = 0; k < result.getChannelTypes().size(); k++) {
                ChannelWithGroupDTO cg = result.getChannelTypes().get(k);
                headerMapper.put(cg.getObjectType(), String.format("C%s", k + 1));
                headerMapperRevert.put(String.format("C%s", k + 1), cg.getObjectType());
            }

            List<MapPlanChannelTarget> listTargetOk = new ArrayList<>();
            int totalRowOk = 0;
            Date currentDateTime = new Date();
            List<PlanChannelSale> targets = convertToExcelPlanChannel(file, PlanChannelSale.class);
            int numberOfLines = targets.size() - 1;

            for (int index = 0; index < numberOfLines; index++) {
                PlanChannelSale t = targets.get(index);

                if (request.getTargetLevel() == 1) {
                    List<ShopTreeDTO> brs = branches.stream().filter(b -> b.getCode().equalsIgnoreCase(t.getBrCode()))
                            .collect(Collectors.toList());
                    if (org.apache.commons.collections4.CollectionUtils.isEmpty(brs)) {
                        t.setComment(messageSource.getMessage(MessageKey.PLAN_BTS_CONFIG_BR_CODE_REQUIRED, null, locale));
                    }
                    validateTarget(t, request.getTargetLevel());
                    validateAccum(t, request.getTargetLevel());

                    for (int k = 0; k < listTargetOk.size(); k++) {
                        MapPlanChannelTarget channelTarget = listTargetOk.get(k);
                        if (channelTarget.getBrCode().equalsIgnoreCase(t.getBrCode())) {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_DUPLICATE_DATA,null, locale));
                            break;
                        }
                    }

                    if (org.apache.commons.lang3.StringUtils.isEmpty(t.getComment())) {
                        boolean hasData = false;
                        for (int i = 1; i < 21; i++) {
                            String channelAlias = Constants.PREFIX_ALIAS_CHANNEL_TYPE + i;
                            String channelAliasKey = null;
                            for (Map.Entry<String, String> entry : headerMapper.entrySet()) {
                                if ((channelAlias).equalsIgnoreCase(entry.getValue())) {
                                    channelAliasKey = entry.getKey();
                                    break;
                                }
                            }
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(channelAliasKey)) {
                                String channelAliasKeyFinal = channelAliasKey;
                                Optional<ChannelWithGroupDTO> channelTypeOpt = result.getChannelTypes().stream().filter(c -> channelAliasKeyFinal.equalsIgnoreCase(c.getObjectType())).findFirst();
                                if (!channelTypeOpt.isPresent()) {
                                    continue;
                                } else {
                                    ChannelWithGroupDTO cGroup = channelTypeOpt.get();
                                    // for develop new
                                    MapPlanChannelTarget tChannel = findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(t.getBrCode().trim().toUpperCase(), cGroup.getChannelTypeId(), 1, planDateYear, Constants.TARGET_TYPE_DEVELOP_NEW);
                                    if (tChannel == null) {
                                        tChannel = new MapPlanChannelTarget();
                                        tChannel.setPlanType(1);
                                        tChannel.setCreateUser(userTokenDto.getStaffCode());
                                        tChannel.setCreatedDate(currentDateTime);
                                        tChannel.setTargetLevel(request.getTargetLevel());
                                        tChannel.setBrCode(t.getBrCode().trim().toUpperCase());
                                        tChannel.setStatus(Constants.SALE_ASSIGN_BR);
                                        tChannel.setPlanDate(planDateYear);
                                        tChannel.setTargetType(Constants.TARGET_TYPE_DEVELOP_NEW);
                                    }
                                    tChannel.setChannelCode(cGroup.getChannelName());
                                    tChannel.setChannelId(cGroup.getChannelTypeId());
                                    tChannel.setUpdateUser(userTokenDto.getStaffCode());
                                    tChannel.setUpdatedDate(currentDateTime);
                                    tChannel.setChildStatus(Constants.SALE_ASSIGN_BR);

                                    try {
                                        String value = BeanUtils.getProperty(t.getTarget(), channelAlias.toLowerCase());
                                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
                                            tChannel.setTotalTarget(Integer.valueOf(value));
                                        } else {
                                            if (tChannel.getTotalTarget() == null) {
                                                tChannel.setTotalTarget(0);
                                            }
                                        }
                                    } catch (Exception ex) {
                                        loggerFactory.error(String.format("Copy data error: [%s] of [%s]", channelAlias, t.getBrCode()), ex);
                                    }
                                    if (!(tChannel.getId() == null && tChannel.getTotalTarget() == 0)) {
                                        hasData = true;
                                        listTargetOk.add(tChannel);
                                    }
                                    // for develop new
                                    MapPlanChannelTarget tChannelAccum = findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(t.getBrCode().trim().toUpperCase(), cGroup.getChannelTypeId(), 1, planDateYear, Constants.TARGET_TYPE_ACCUMULATED);
                                    if (tChannelAccum == null) {
                                        tChannelAccum = new MapPlanChannelTarget();
                                        tChannelAccum.setPlanType(1);
                                        tChannelAccum.setCreateUser(userTokenDto.getStaffCode());
                                        tChannelAccum.setCreatedDate(currentDateTime);
                                        tChannelAccum.setTargetLevel(request.getTargetLevel());
                                        tChannelAccum.setBrCode(t.getBrCode().trim().toUpperCase());
                                        tChannelAccum.setStatus(Constants.SALE_ASSIGN_BR);
                                        tChannelAccum.setPlanDate(planDateYear);
                                        tChannelAccum.setTargetType(Constants.TARGET_TYPE_ACCUMULATED);
                                    }
                                    tChannelAccum.setChannelCode(cGroup.getChannelName());
                                    tChannelAccum.setChannelId(cGroup.getChannelTypeId());
                                    tChannelAccum.setUpdateUser(userTokenDto.getStaffCode());
                                    tChannelAccum.setUpdatedDate(currentDateTime);
                                    tChannelAccum.setChildStatus(Constants.SALE_ASSIGN_BR);

                                    try {
                                        String value = BeanUtils.getProperty(t.getAccum(), channelAlias.toLowerCase());
                                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
                                            tChannelAccum.setTotalTarget(Integer.valueOf(value));
                                        } else {
                                            if (tChannelAccum.getTotalTarget() == null) {
                                                tChannelAccum.setTotalTarget(0);
                                            }
                                        }
                                    } catch (Exception ex) {
                                        loggerFactory.error(String.format("Copy data error: [%s] of [%s]", channelAlias, t.getBrCode()), ex);
                                    }
                                    if (!(tChannelAccum.getId() == null && tChannelAccum.getTotalTarget() == 0)) {
                                        hasData = true;
                                        listTargetOk.add(tChannelAccum);
                                    }
                                }
                            }
                        }
                        if (hasData) {
                            totalRowOk++;
                        } else {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_NO_DATA, null,locale));
                        }
                    }
                } else if (request.getTargetLevel() == 2) {
                    List<ShopTreeDTO> bcs = bcList.stream().filter(b -> b.getCode().equalsIgnoreCase(t.getBcCode()))
                            .collect(Collectors.toList());
                    if (org.apache.commons.collections4.CollectionUtils.isEmpty(bcs)) {
                        t.setComment(messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_BC_CODE_INVALID, null,locale));
                    }
                    validateTarget(t, request.getTargetLevel());
                    for (int k = 0; k < listTargetOk.size(); k++) {
                        MapPlanChannelTarget channelTarget = listTargetOk.get(k);
                        if (channelTarget.getBrCode().equalsIgnoreCase(request.getBrCode())
                                && channelTarget.getBcCode().equalsIgnoreCase(t.getBcCode())) {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_DUPLICATE_DATA, null,locale));
                            break;
                        }
                    }
                    if (org.apache.commons.lang3.StringUtils.isEmpty(t.getComment())) {
                        boolean hasData = false;
                        for (int i = 1; i < 21; i++) {
                            String channelAlias = Constants.PREFIX_ALIAS_CHANNEL_TYPE + i;
                            String channelAliasKey = null;
                            for (Map.Entry<String, String> entry : headerMapper.entrySet()) {
                                if ((channelAlias).equalsIgnoreCase(entry.getValue())) {
                                    channelAliasKey = entry.getKey();
                                    break;
                                }
                            }
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(channelAliasKey)) {
                                String channelAliasKeyFinal = channelAliasKey;
                                Optional<ChannelWithGroupDTO> channelTypeOpt = result.getChannelTypes().stream().filter(c -> c.getObjectType().equalsIgnoreCase(channelAliasKeyFinal)).findFirst();
                                if (!channelTypeOpt.isPresent()) {
                                    continue;
                                } else {
                                    ChannelWithGroupDTO cGroup = channelTypeOpt.get();
                                    Optional<MapPlanChannelTarget> tParent = targetsBrLeve1.stream().filter(tp -> tp.getChannelId().equals(cGroup.getChannelTypeId())).findFirst();
                                    if (!tParent.isPresent()) {
                                        t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_BR_TARGET_NOT_FOUND, null,locale));
                                    } else {
                                        Optional<MapPlanChannelTarget> tChannelOpt = targetsBrLeve2.stream().filter(tl2 -> tl2.getBcCode().equalsIgnoreCase(t.getBcCode()!=null?t.getBcCode().trim():"") && tl2.getChannelId().equals(cGroup.getChannelTypeId())).findFirst();
                                        MapPlanChannelTarget tChannel = null;
                                        if (!tChannelOpt.isPresent()) {
                                            tChannel = new MapPlanChannelTarget();
                                            tChannel.setPlanType(2);
                                            tChannel.setCreateUser(userTokenDto.getStaffCode());
                                            tChannel.setCreatedDate(currentDateTime);
                                            tChannel.setTargetLevel(request.getTargetLevel());
                                            tChannel.setBrCode(request.getBrCode());
                                            tChannel.setBcCode(t.getBcCode().trim().toUpperCase());
                                            tChannel.setPlanDate(planDateYear);
                                            tChannel.setTargetType(request.getTargetType());

                                        } else {
                                            tChannel = tChannelOpt.get();
                                        }
                                        tChannel.setParentId(tParent.get().getId());
                                        if (request.getTargetType() == Constants.TARGET_TYPE_DEVELOP_NEW) {
                                            tParent.get().setChildStatus(Constants.STATUS_DRAFT);
                                            tChannel.setStatus(Constants.STATUS_DRAFT);
                                        }else {
                                            tParent.get().setChildStatus(Constants.SALE_APPROVED);
                                            tChannel.setStatus(Constants.SALE_APPROVED);
                                        }
                                        tChannel.setChannelCode(cGroup.getChannelName());
                                        tChannel.setChannelId(cGroup.getChannelTypeId());
                                        tChannel.setUpdateUser(userTokenDto.getStaffCode());
                                        tChannel.setUpdatedDate(currentDateTime);

                                        setTargetForChannel(t, tChannel, channelAlias, request.getPlanDate());

                                        tChannel.summaryTotalTarget();
                                        if (!(tChannel.getId() == null && t.isNoData())) {
                                            hasData = true;
                                            listTargetOk.add(tChannel);
                                        }
                                    }
                                }
                            }
                        }
                        if (hasData) {
                            totalRowOk++;
                        } else {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_NO_DATA, null,locale));
                        }
                    }
                } else if (request.getTargetLevel() == 3) {
                    String staffCode = org.apache.commons.lang3.StringUtils.trim(t.getStaffCode());
                    List<StaffBaseDto> afLstFilter = listStaffs.stream().filter(s -> s.getCode().equalsIgnoreCase(staffCode)).collect(Collectors.toList());
                    if (org.apache.commons.collections4.CollectionUtils.isEmpty(afLstFilter)) {
                        t.setComment(StringUtils.commentBuild(t.getComment(), messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_STAFF_CODE_INVALID,null,locale)));
                    }

                    validateTarget(t, request.getTargetLevel());

                    for (int k = 0; k < listTargetOk.size(); k++) {
                        MapPlanChannelTarget channelTarget = listTargetOk.get(k);
                        if (channelTarget.getBrCode().equalsIgnoreCase(request.getBrCode())
                                && channelTarget.getBcCode().equalsIgnoreCase(request.getBcCode())
                                && channelTarget.getStaffCode().equalsIgnoreCase(t.getStaffCode())) {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_DUPLICATE_DATA,null,locale));
                            break;
                        }
                    }
                    if (org.apache.commons.lang3.StringUtils.isEmpty(t.getComment())) {
                        boolean hasData = false;
                        for (int i = 1; i < 21; i++) {
                            String channelAlias = Constants.PREFIX_ALIAS_CHANNEL_TYPE + i;
                            String channelAliasKey = null;
                            for (Map.Entry<String, String> entry : headerMapper.entrySet()) {
                                if ((channelAlias).equalsIgnoreCase(entry.getValue())) {
                                    channelAliasKey = entry.getKey();
                                    break;
                                }
                            }
                            String channelAliasKeyFinal = channelAliasKey;
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(channelAliasKey)) {
                                Optional<ChannelWithGroupDTO> channelTypeOpt = result.getChannelTypes().stream().filter(c -> c.getObjectType().equalsIgnoreCase(channelAliasKeyFinal)).findFirst();
                                if (!channelTypeOpt.isPresent()) {
                                    continue;
                                } else {
                                    ChannelWithGroupDTO cGroup = channelTypeOpt.get();
                                    List<StaffBaseDto> acLstFilter1 = null;
                                    if (cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_POS) {
                                        acLstFilter1 = listACStaffs.stream()
                                                .filter(s -> s.getCode().equalsIgnoreCase(t.getStaffCode().trim()))
                                                .collect(Collectors.toList());
                                        if (org.apache.commons.collections4.CollectionUtils.isEmpty(acLstFilter1)) {
                                            continue;
                                        }
                                    }
                                    List<StaffBaseDto> afLstFilter1 = null;
                                    if (cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_D2D ||
                                            cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_DF) {
                                        afLstFilter1 = listAfStaffs.stream()
                                                .filter(s -> s.getCode().equalsIgnoreCase(t.getStaffCode().trim()))
                                                .collect(Collectors.toList());
                                        if (org.apache.commons.collections4.CollectionUtils.isEmpty(afLstFilter1)) {
                                            continue;
                                        }
                                    }
                                    List<StaffBaseDto> ceDirectorLstFilter1 = null;
                                    if (cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_CD ||
                                            cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_SD ||
                                            cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_DL) {
                                        ceDirectorLstFilter1 = listCeDirectorStaffs.stream()
                                                .filter(s -> s.getCode().equalsIgnoreCase(t.getStaffCode().trim()))
                                                .collect(Collectors.toList());
                                        if (org.apache.commons.collections4.CollectionUtils.isEmpty(ceDirectorLstFilter1)) {
                                            continue;
                                        }
                                    }
                                    if ((cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_POS && org.apache.commons.collections4.CollectionUtils.isNotEmpty(acLstFilter1)) ||
                                            (cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_D2D || cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_DF) && org.apache.commons.collections4.CollectionUtils.isNotEmpty(afLstFilter1) ||
                                            (cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_CD ||
                                                    cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_SD ||
                                                    cGroup.getChannelTypeId() == Constants.CHANNEL_TYPE_ID_DL) && org.apache.commons.collections4.CollectionUtils.isNotEmpty(ceDirectorLstFilter1)
                                    ) {
                                        MapPlanChannelTarget tParent = findTargetByBcCodeAndChannelIdAndTargetLevelAndPlanDate(request.getBcCode(), cGroup.getChannelTypeId(), 2, planDateYear, request.getTargetType());
                                        if (tParent == null || request.getTargetType() == 1 && tParent.getStatus() != Constants.SALE_APPROVED) {
                                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_BC_TARGET_NOT_FOUND,null,locale));
                                        } else {
                                            MapPlanChannelTarget tChannel = findTargetByBcCodeAndStaffCodeAndChannelIdAndTargetLevelAndPlanDate(request.getBcCode(), staffCode.toUpperCase(), cGroup.getChannelTypeId(), 3, request.getTargetType(), planDateYear);
                                            if (tChannel == null) {
                                                tChannel = new MapPlanChannelTarget();
                                                tChannel.setPlanType(2);
                                                tChannel.setCreateUser(userTokenDto.getStaffCode());
                                                tChannel.setCreatedDate(currentDateTime);
                                                tChannel.setTargetLevel(request.getTargetLevel());
                                                tChannel.setTargetType(request.getTargetType());
                                                tChannel.setBrCode(request.getBrCode());
                                                tChannel.setBcCode(request.getBcCode());
                                                tChannel.setStaffCode(afLstFilter.get(0).getCode());
                                                tChannel.setStaffId(afLstFilter.get(0).getId());
                                                tChannel.setStatus(Constants.BC_ASSIGN_AC_AF);
                                                tChannel.setPlanDate(planDateYear);
                                                tChannel.setParentId(tParent.getId());
                                                tParent.setChildStatus(Constants.BC_ASSIGN_AC_AF);
                                            }
                                            tChannel.setChannelCode(cGroup.getChannelName());
                                            tChannel.setChannelId(cGroup.getChannelTypeId());
                                            tChannel.setUpdateUser(userTokenDto.getStaffCode());
                                            tChannel.setUpdatedDate(currentDateTime);

                                            setTargetForChannel(t, tChannel, channelAlias,request.getPlanDate());
                                            tChannel.summaryTotalTarget();
                                            if (!(tChannel.getId() == null && tChannel.getTotalTarget() == 0)) {
                                                hasData = true;
                                                listTargetOk.add(tChannel);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (hasData) {
                            totalRowOk++;
                        } else {
                            t.setComment(messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_PLAN_ROW_NO_DATA,null,locale));
                        }
                    }
                }
            }

            if (listTargetOk.size() > 0) {
                try {
                    listTargetOk.forEach(t -> mapPlanChannelTargetRepository.save(t));
                    if (request.getTargetLevel() == 2) {
                        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(targetsBrLeve1)) {
                            targetsBrLeve1.forEach(t -> {
                                if (request.getTargetType() == Constants.TARGET_TYPE_DEVELOP_NEW) {
                                    t.setChildStatus(Constants.STATUS_DRAFT);
                                } else {
                                    t.setChildStatus(Constants.SALE_APPROVED);
                                }
                                mapPlanChannelTargetRepository.save(t);
                            });
                        }
                        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(targetsBrLeve2)) {
                            List<MapPlanChannelTarget> notInImportFile = targetsBrLeve2.stream().filter(tl2 -> Constants.STATUS_DRAFT != tl2.getStatus()).collect(Collectors.toList());
                            notInImportFile.forEach(t -> {
                                if (request.getTargetType() == Constants.TARGET_TYPE_DEVELOP_NEW) {
                                    t.setStatus(Constants.STATUS_DRAFT);
                                } else {
                                    t.setStatus(Constants.SALE_APPROVED);
                                }
                                mapPlanChannelTargetRepository.save(t);
                            });
                        }
                    }
                    response = responseFactory.error(HttpStatus.OK, messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_RESULT,new String[]{String.valueOf(totalRowOk), String.valueOf(numberOfLines)},locale), null);
                    if (totalRowOk < numberOfLines) {
                        response = responseFactory.error(HttpStatus.OK, messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_RESULT, new String[]{String.valueOf(totalRowOk), String.valueOf(numberOfLines)},locale), buildImportResultFile(targets, request.getTargetLevel()));
                    }
                } catch (Exception ex) {
                    loggerFactory.error(ex.getMessage(), ex);
                    response = responseFactory.error(HttpStatus.OK, Constants.ERROR, null);
                    if (ex instanceof JDBCConnectionException) {
                        response = responseFactory.error(HttpStatus.OK, messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_SQL_CONNECTION_ERROR,null,locale), null);
                    } else {
                        response = responseFactory.error(HttpStatus.OK, null,ex.getMessage(), null);
                    }
                }
            } else {
                response = responseFactory.error(HttpStatus.OK, messageSource.getMessage(MessageKey.PLAN_SALE_IMPORT_RESULT,new String[]{"0", String.valueOf(targets.size())},locale),buildImportResultFile(targets, request.getTargetLevel()));
            }
        }catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
            response = responseFactory.error(HttpStatus.OK, Constants.ERROR, null);
        }
        return response;
    }

    public MapPlanChannelTarget findTargetByBcCodeAndStaffCodeAndChannelIdAndTargetLevelAndPlanDate(
            String bcCode, String staffCode, Long channelId, Integer targetLevel, Integer targetType,Date planDate) {
        List<MapPlanChannelTarget> results = mapPlanChannelTargetRepository.findTargetByBcCodeAndStaffCodeAndChannelIdAndTargetLevelAndPlanDate(bcCode,staffCode,channelId,targetLevel,targetType,planDate);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(results)) {
            return results.get(0);
        } else {
            return null;
        }
    }

    public void setTargetForChannel(PlanChannelSale target, MapPlanChannelTarget tChannel, String channelAlias,String planDate) {
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
        for (int i = currentMonth + 1; i < 13; i++) {
            String proName = "t" + i;
            try {
                Object targetMonthObj = PropertyUtils.getProperty(target, proName);
                if (targetMonthObj != null) {
                    ChannelImportTarget targetMonth = (ChannelImportTarget) targetMonthObj;
                    String value = BeanUtils.getProperty(targetMonth, channelAlias.toLowerCase());
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
                        PropertyUtils.setSimpleProperty(tChannel, proName + "Target", Integer.valueOf(value));
                    } else {
                        PropertyUtils.setSimpleProperty(tChannel, proName + "Target", null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void validateTarget(PlanChannelSale target, int targetLevel) {
        ChannelImportTarget t1 = target.getT1();
        if (targetLevel == 1) {
            validateChannelDevelopTarget(target, targetLevel, target.getTarget(), null, false);
        } else {
            String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JAN, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t1, monthString, false);
            ChannelImportTarget t2 = target.getT2();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_FEB, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t2, monthString, false);
            ChannelImportTarget t3 = target.getT3();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_MAR, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t3, monthString, false);
            ChannelImportTarget t4 = target.getT4();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_APR, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t4, monthString, false);
            ChannelImportTarget t5 = target.getT5();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_MAY, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t5, monthString, false);
            ChannelImportTarget t6 = target.getT6();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JUN, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t6, monthString, false);
            ChannelImportTarget t7 = target.getT7();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JUL, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t7, monthString, false);
            ChannelImportTarget t8 = target.getT8();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_AUG, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t8, monthString, false);
            ChannelImportTarget t9 = target.getT9();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_SEP, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t9, monthString, false);
            ChannelImportTarget t10 = target.getT10();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_OCT, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t10, monthString, false);
            ChannelImportTarget t11 = target.getT11();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_NOV, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t11, monthString, false);
            ChannelImportTarget t12 = target.getT12();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_DEC, null,locale);
            validateChannelDevelopTarget(target, targetLevel, t12, monthString, false);
        }
    }

    public void validateAccum(PlanChannelSale target, int targetLevel) {
        if (targetLevel == 1) {
            validateChannelDevelopTarget(target, targetLevel, target.getAccum(), null, true);
        } else {
            ChannelImportTarget t1 = target.getT1();
            String monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JAN,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t1, monthString, false);
            ChannelImportTarget t2 = target.getT2();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_FEB,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t2, monthString, false);
            ChannelImportTarget t3 = target.getT3();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_MAR,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t3, monthString, false);
            ChannelImportTarget t4 = target.getT4();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_APR,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t4, monthString, false);
            ChannelImportTarget t5 = target.getT5();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_MAY,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t5, monthString, false);
            ChannelImportTarget t6 = target.getT6();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JUN,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t6, monthString, false);
            ChannelImportTarget t7 = target.getT7();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_JUL,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t7, monthString, false);
            ChannelImportTarget t8 = target.getT8();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_AUG,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t8, monthString, false);
            ChannelImportTarget t9 = target.getT9();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_SEP,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t9, monthString, false);
            ChannelImportTarget t10 = target.getT10();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_OCT,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t10, monthString, false);
            ChannelImportTarget t11 = target.getT11();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_NOV,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t11, monthString, false);
            ChannelImportTarget t12 = target.getT12();
            monthString = messageSource.getMessage(MessageKey.COMMON_MONTH_DEC,null, locale);
            validateChannelDevelopTarget(target, targetLevel, t12, monthString, false);
        }
    }

    private void validateChannelDevelopTarget(PlanChannelSale target, int targetLevel, ChannelImportTarget t1, String month, boolean isAccum) {
        for (int i = 1; i < 21; i++) {
            try {
                String propertyName = Constants.PREFIX_ALIAS_CHANNEL_TYPE + i;
                String channelType = headerMapperRevert.get(propertyName);
                String value = BeanUtils.getProperty(t1, propertyName.toLowerCase());
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
                    try {
                        Integer targetValue = Integer.valueOf(value);
                        if (targetValue < Constants.TARGET_MIN) {
                            if (targetLevel == 1) {
                                if (isAccum){
                                    target.setComment(StringUtils.commentBuild(target.getComment(),
                                            messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_ACCUM_INVALID, new String[]{channelType},locale)));
                                }else {
                                    target.setComment(StringUtils.commentBuild(target.getComment(),
                                            messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_TARGET_INVALID, new String[]{channelType},locale)));
                                }
                            } else {
                                if (isAccum){
                                    target.setComment(StringUtils.commentBuild(target.getComment(),
                                            messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_ACCUM_MONTH_INVALID, new String[]{channelType, month},locale)));
                                }else {
                                    target.setComment(StringUtils.commentBuild(target.getComment(),
                                            messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_TARGET_MONTH_INVALID, new String[]{channelType, month},locale)));
                                }
                            }
                        }
                    } catch (Exception ex) {
                        if (targetLevel == 1) {
                            if (isAccum){
                                target.setComment(StringUtils.commentBuild(target.getComment(),
                                        messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_ACCUM_INVALID, new String[]{channelType},locale)));
                            }else {
                                target.setComment(StringUtils.commentBuild(target.getComment(),
                                        messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_TARGET_INVALID, new String[]{channelType},locale)));
                            }
                        } else {
                            if (isAccum){
                                target.setComment(StringUtils.commentBuild(target.getComment(),
                                        messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_ACCUM_MONTH_INVALID, new String[]{channelType, month},locale)));
                            }else {
                                target.setComment(StringUtils.commentBuild(target.getComment(),
                                        messageSource.getMessage(MessageKey.PLAN_CHANNEL_IMPORT_TARGET_MONTH_INVALID, new String[]{channelType, month},locale)));
                            }
                        }
                        ex.printStackTrace();
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public List<PlanChannelTargetResponse> searchToResponse (PlanChannelSearchRequest request) {
        List<PlanChannelTargetResponse> listRes = new LinkedList<>();
        try {
            List<PlanChannelTarget> targets = mapPlanChannelTargetRepository
                    .searchPlanChannelTarget(
                            request.getBrCode(),
                            request.getBcCode(),
                            request.getPlanType(),
                            request.getTargetLevel(),
                            request.getStaffCode(),
                            request.getChannelId(),
                            request.getPlanDate());

            Map<String, PlanChannelTargetResponse> map = new HashMap<>();
            Integer targetLevel = request.getTargetLevel() == null ? 1 : request.getTargetLevel();
            List<ChannelWithGroupDTO> channelTypes = channelTypeService.getListChannelCanPlanToDevelop();
            PlanChannelTargetResponse summary = new PlanChannelTargetResponse();
            String channelTypeNameFormat = "%s (%s) - %s";
            String channelTypeNameFormat4Sort = "%s (%s) - (%s) %s";
            for (ChannelWithGroupDTO c : channelTypes) {
                summary.getChannelTargetMap().put(String.format(channelTypeNameFormat, c.getChannelName(), c.getObjectType(), messageSource.getMessage(MessageKey.PLAN_CHANNEL_DEVELOP_NEW, null,locale)), null);
                summary.getChannelTargetMap().put(String.format(channelTypeNameFormat, c.getChannelName(), c.getObjectType(), messageSource.getMessage(MessageKey.PLAN_CHANNEL_ACCUMULATED, null,locale)), null);
            }
            for (PlanChannelTarget t : targets) {
                Optional<ChannelWithGroupDTO> channelTypeOpt = channelTypes.stream().filter(c -> t.getChannelId().longValue() == c.getChannelTypeId().longValue()).findFirst();
                String channelTypeMapping = "TEMP";
                if (channelTypeOpt.isPresent()) {
                    channelTypeMapping = channelTypeOpt.get().getObjectType();
                }
                String channelTypeName = String.format(channelTypeNameFormat, channelTypeOpt.get().getChannelName(), channelTypeOpt.get().getObjectType(), messageSource.getMessage(MessageKey.PLAN_CHANNEL_DEVELOP_NEW, null,locale));
                String channelTypeName4Sort = String.format(channelTypeNameFormat4Sort, channelTypeOpt.get().getChannelName(), channelTypeOpt.get().getObjectType(), "1",messageSource.getMessage(MessageKey.PLAN_CHANNEL_DEVELOP_NEW, null,locale));
                if (t.getTargetType() == Constants.TARGET_TYPE_ACCUMULATED) {
                    channelTypeName = String.format(channelTypeNameFormat, channelTypeOpt.get().getChannelName(), channelTypeOpt.get().getObjectType(),messageSource.getMessage(MessageKey.PLAN_CHANNEL_ACCUMULATED, null,locale));
                    channelTypeName4Sort = String.format(channelTypeNameFormat4Sort, channelTypeOpt.get().getChannelName(), channelTypeOpt.get().getObjectType(), "2", messageSource.getMessage(MessageKey.PLAN_CHANNEL_ACCUMULATED, null,locale));
                }
                ChannelTarget sum = summary.getChannelTargetMap().get(channelTypeName);

                if (sum == null) {
                    sum = new ChannelTarget();
                    sum.setChannelCode(channelTypeName);
                    sum.setChannelTypeName4Sort(channelTypeName4Sort);
                    sum.setChannelId(t.getChannelId());
                    sum.setChannelKey(channelTypeMapping);
                    sum.setTargetType(t.getTargetType());
                }
                PlanChannelTargetResponse res = null;
                if (targetLevel == 1) {
                    res = map.get(t.getBrCode());
                    if (res == null) {
                        res = new PlanChannelTargetResponse();
                        for (ChannelWithGroupDTO c : channelTypes) {
                            res.getChannelTargetMap().put(String.format(channelTypeNameFormat, c.getChannelName(), c.getObjectType(), messageSource.getMessage(MessageKey.PLAN_CHANNEL_DEVELOP_NEW, null,locale)), null);
                            res.getChannelTargetMap().put(String.format(channelTypeNameFormat, c.getChannelName(), c.getObjectType(), messageSource.getMessage(MessageKey.PLAN_CHANNEL_ACCUMULATED, null,locale)), null);
                        }
                        res.setBrCode(t.getBrCode());
                        map.put(t.getBrCode(), res);
                    }
                    summary.setBrCode(TOTAL);
                } else if (targetLevel == 2) {
                    res = map.get(t.getBcCode());
                    if (res == null) {
                        res = new PlanChannelTargetResponse();
                        res.setBrCode(t.getBrCode());
                        res.setBcCode(t.getBcCode());
                        map.put(t.getBcCode(), res);
                    }
                    summary.setBrCode(t.getBrCode());
                    summary.setBcCode(TOTAL);
                } else {
                    res = map.get(t.getStaffCode());
                    if (res == null) {
                        res = new PlanChannelTargetResponse();
                        res.setBrCode(t.getBrCode());
                        res.setBcCode(t.getBcCode());
                        res.setStaffCode(t.getStaffCode());
                        res.setStaffId(t.getStaffId());
                        map.put(t.getStaffCode(), res);
                    }
                    summary.setBrCode(t.getBrCode());
                    summary.setBcCode(t.getBcCode());
                    summary.setStaffCode(TOTAL);
                }
                ChannelTarget channelTarget = new ChannelTarget();
                channelTarget.setTargetId(t.getId());
                channelTarget.setChannelId(t.getChannelId());
                channelTarget.setChannelCode(channelTypeName);
                channelTarget.setChannelTypeName4Sort(channelTypeName4Sort);
                channelTarget.setChannelKey(channelTypeMapping);
                channelTarget.setTargetType(t.getTargetType());
                channelTarget.setParentId(t.getParentId());
                channelTarget.setTarget(buildTargets(t));
                channelTarget.setResult(buildResults(t));
                channelTarget.setPlan(buildPlans(t));
                res.getChannelTargetMap().put(channelTypeName, channelTarget);
                res.setChildStatus(t.getChildStatus());
                AmountByMonth sumTarget = sum.getTarget();
                if (sumTarget == null) {
                    sumTarget = new AmountByMonth();
                }
                buildSummaryTarget(t, sumTarget);
                AmountByMonth sumResult = sum.getResult();
                if (sumResult == null) {
                    sumResult = new AmountByMonth();
                }
                AmountByMonth sumPlan = sum.getPlan();
                if (sumPlan == null) {
                    sumPlan = new AmountByMonth();
                }
                buildSummaryResult(t, sumResult);
                sum.setTarget(sumTarget);
                sum.setResult(sumResult);
                buildSummaryPlan(t, sumPlan);
                sum.setPlan(sumPlan);
                summary.getChannelTargetMap().put(channelTypeName, sum);
            }
            if (map.isEmpty()) {
                return new ArrayList<>();
            }


            for (Map.Entry<String, PlanChannelTargetResponse> entry : map.entrySet()) {
                PlanChannelTargetResponse res = entry.getValue();
                res.buildListChannel();
                res.setChannelTargetMap(new LinkedHashMap<>());
                if (CollectionUtils.isNotEmpty(res.getChannels())) {
                    listRes.add(res);
                }
            }
            if (request.getTargetLevel().equals(1)) {
                listRes = listRes.stream().sorted((o1, o2) -> o1.getBrCode().compareToIgnoreCase(o2.getBrCode())).collect(Collectors.toList());
            } else if (request.getTargetLevel().equals(2)) {
                listRes = listRes.stream().sorted((o1, o2) -> o1.getBcCode().compareToIgnoreCase(o2.getBcCode())).collect(Collectors.toList());
            } else if (request.getTargetLevel().equals(3)) {
                listRes = listRes.stream().sorted((o1, o2) -> o1.getStaffCode().compareToIgnoreCase(o2.getStaffCode())).collect(Collectors.toList());
            }
            summary.buildListChannel();
            summary.setChannelTargetMap(new LinkedHashMap<>());
            if (CollectionUtils.isNotEmpty(summary.getChannels())) {
                listRes.add(summary);
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        return listRes;
    }
    private static void buildSummaryTarget(PlanChannelTarget t, AmountByMonth sumTarget) {
        sumTarget.setT1(NumberUtils.plusInt(sumTarget.getT1(), t.getT1Target()));
        sumTarget.setT2(NumberUtils.plusInt(sumTarget.getT2(), t.getT2Target()));
        sumTarget.setT3(NumberUtils.plusInt(sumTarget.getT3(), t.getT3Target()));
        sumTarget.setT4(NumberUtils.plusInt(sumTarget.getT4(), t.getT4Target()));
        sumTarget.setT5(NumberUtils.plusInt(sumTarget.getT5(), t.getT5Target()));
        sumTarget.setT6(NumberUtils.plusInt(sumTarget.getT6(), t.getT6Target()));
        sumTarget.setT7(NumberUtils.plusInt(sumTarget.getT7(), t.getT7Target()));
        sumTarget.setT8(NumberUtils.plusInt(sumTarget.getT8(), t.getT8Target()));
        sumTarget.setT9(NumberUtils.plusInt(sumTarget.getT9(), t.getT9Target()));
        sumTarget.setT10(NumberUtils.plusInt(sumTarget.getT10(), t.getT10Target()));
        sumTarget.setT11(NumberUtils.plusInt(sumTarget.getT11(), t.getT11Target()));
        sumTarget.setT12(NumberUtils.plusInt(sumTarget.getT12(), t.getT12Target()));
        sumTarget.setTotal(NumberUtils.plusInt(sumTarget.getTotal(), t.getTotalTarget()));
    }

    private static void buildSummaryResult(PlanChannelTarget t, AmountByMonth sumResult) {
        sumResult.setT1(NumberUtils.plusInt(sumResult.getT1(), t.getT1Result()));
        sumResult.setT2(NumberUtils.plusInt(sumResult.getT2(), t.getT2Result()));
        sumResult.setT3(NumberUtils.plusInt(sumResult.getT3(), t.getT3Result()));
        sumResult.setT4(NumberUtils.plusInt(sumResult.getT4(), t.getT4Result()));
        sumResult.setT5(NumberUtils.plusInt(sumResult.getT5(), t.getT5Result()));
        sumResult.setT6(NumberUtils.plusInt(sumResult.getT6(), t.getT6Result()));
        sumResult.setT7(NumberUtils.plusInt(sumResult.getT7(), t.getT7Result()));
        sumResult.setT8(NumberUtils.plusInt(sumResult.getT8(), t.getT8Result()));
        sumResult.setT9(NumberUtils.plusInt(sumResult.getT9(), t.getT9Result()));
        sumResult.setT10(NumberUtils.plusInt(sumResult.getT10(), t.getT10Result()));
        sumResult.setT11(NumberUtils.plusInt(sumResult.getT11(), t.getT11Result()));
        sumResult.setT12(NumberUtils.plusInt(sumResult.getT12(), t.getT12Result()));
        sumResult.setTotal(NumberUtils.plusInt(sumResult.getTotal(), t.getTotalResult()));
    }

    private static void buildSummaryPlan(PlanChannelTarget t, AmountByMonth sumPlan) {
        sumPlan.setT1(NumberUtils.plusInt(sumPlan.getT1(), t.getT1Plan()));
        sumPlan.setT2(NumberUtils.plusInt(sumPlan.getT2(), t.getT2Plan()));
        sumPlan.setT3(NumberUtils.plusInt(sumPlan.getT3(), t.getT3Plan()));
        sumPlan.setT4(NumberUtils.plusInt(sumPlan.getT4(), t.getT4Plan()));
        sumPlan.setT5(NumberUtils.plusInt(sumPlan.getT5(), t.getT5Plan()));
        sumPlan.setT6(NumberUtils.plusInt(sumPlan.getT6(), t.getT6Plan()));
        sumPlan.setT7(NumberUtils.plusInt(sumPlan.getT7(), t.getT7Plan()));
        sumPlan.setT8(NumberUtils.plusInt(sumPlan.getT8(), t.getT8Plan()));
        sumPlan.setT9(NumberUtils.plusInt(sumPlan.getT9(), t.getT9Plan()));
        sumPlan.setT10(NumberUtils.plusInt(sumPlan.getT10(), t.getT10Plan()));
        sumPlan.setT11(NumberUtils.plusInt(sumPlan.getT11(), t.getT11Plan()));
        sumPlan.setT12(NumberUtils.plusInt(sumPlan.getT12(), t.getT12Plan()));
        sumPlan.setTotal(NumberUtils.plusInt(sumPlan.getTotal(), t.getTotalPlan()));
    }


    public AmountByMonth buildTargets(PlanChannelTarget t) {
        return AmountByMonth.builder().t1(t.getT1Target()).t2(t.getT2Target()).t3(t.getT3Target()).t4(t.getT4Target())
                .t5(t.getT5Target()).t6(t.getT6Target()).t7(t.getT7Target()).t8(t.getT8Target()).t9(t.getT9Target())
                .t10(t.getT10Target()).t11(t.getT11Target()).t12(t.getT12Target()).total(t.getTotalTarget()).build();
    }
    public AmountByMonth buildResults(PlanChannelTarget t) {
        return AmountByMonth.builder().t1(t.getT1Result()).t2(t.getT2Result()).t3(t.getT3Result()).t4(t.getT4Result())
                .t5(t.getT5Result()).t6(t.getT6Result()).t7(t.getT7Result()).t8(t.getT8Result()).t9(t.getT9Result())
                .t10(t.getT10Result()).t11(t.getT11Result()).t12(t.getT12Result()).total(t.getTotalResult()).build();
    }
    public AmountByMonth buildPlans(PlanChannelTarget t) {
        return AmountByMonth.builder().t1(t.getT1Plan()).t2(t.getT2Plan()).t3(t.getT3Plan()).t4(t.getT4Plan())
                .t5(t.getT5Plan()).t6(t.getT6Plan()).t7(t.getT7Plan()).t8(t.getT8Plan()).t9(t.getT9Plan())
                .t10(t.getT10Plan()).t11(t.getT11Plan()).t12(t.getT12Plan()).total(t.getTotalPlan()).build();
    }

    public List<MapPlanChannelTarget> findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(String brCode, Long channelId, Integer targetLevel, Date planDate){
        try{
            List<MapPlanChannelTarget> results = mapPlanChannelTargetRepository.findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(brCode, channelId, targetLevel, planDate);
            return results;
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    public MapPlanChannelTarget findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(String brCode, Long channelId, Integer targetLevel, Date planDate,Integer targetType){
        try{
            List<MapPlanChannelTarget> results = mapPlanChannelTargetRepository.findTargetByBrCodeAndChannelIdAndTargetLevelAndPlanDate(brCode, channelId, targetLevel, targetType, planDate);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(results)) {
                return (MapPlanChannelTarget) results.get(0);
            }
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    public List<MapPlanChannelTarget> findTargetByBcCodeAndChannelIdAndTargetLevelAndPlanDate(String bcCode, Long channelId, Integer targetLevel, Date planDate){
        try {
            List<MapPlanChannelTarget> results = mapPlanChannelTargetRepository.findTargetByBcCodeAndChannelIdAndTargetLevelAndPlanDate(bcCode, channelId, targetLevel, planDate);
            return results;
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return null;
    }
    public MapPlanChannelTarget findTargetByBcCodeAndChannelIdAndTargetLevelAndPlanDate(String bcCode, Long channelId, Integer targetLevel, Date planDate,Integer targetType){
        try {
            List<MapPlanChannelTarget> results = mapPlanChannelTargetRepository.findTargetByBcCodeAndChannelIdAndTargetLevelAndPlanDate(bcCode, channelId, targetLevel,targetType, planDate);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(results)) {
                return results.get(0);
            }
        }catch (Exception e){
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    @Transactional
    public String setStatus(PlanChannelSearchRequest searchRequest) {
        String result = "";
        try {
            Date planDateYear = DateUtils.parseDate(searchRequest.getPlanDate(), "YYYY");
            List<MapPlanChannelTarget> targetsBrLeve1 = findTargetByBrCodeAndTargetLevelAndPlanDate(searchRequest.getBrCode(), 1, planDateYear);
            List<MapPlanChannelTarget> targetsBrLeve2 = findTargetByBrCodeAndTargetLevelAndPlanDate(searchRequest.getBrCode(), 2, planDateYear);
            targetsBrLeve1.forEach(l1 -> {
                l1.setChildStatus(searchRequest.getStatus());
                mapPlanChannelTargetRepository.save(l1);
            });
            targetsBrLeve2.forEach(l2 -> {
                l2.setStatus(searchRequest.getStatus());
                mapPlanChannelTargetRepository.save(l2);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "error";
        }
        return result;
    }

    public List<MapPlanChannelTarget> findTargetByBrCodeAndTargetLevelAndPlanDate(String brCode, Integer i, Date planDateYear) {
        try {
            List<MapPlanChannelTarget> results = mapPlanChannelTargetRepository.findTargetByBrCodeAndTargetLevelAndPlanDate(brCode, i, planDateYear);
            return results;
        }catch (Exception ex) {
            loggerFactory.error(ex.getMessage(), ex);
        }
        return null;
    }

    public List<MapPlanChannelTarget> findTargetByBrCodeAndTargetLevelAndPlanDate(String brCode, Integer i, Date planDateYear,Integer targetType) {
        try {
            List<MapPlanChannelTarget> results = mapPlanChannelTargetRepository.findTargetByBrCodeAndTargetLevelAndPlanDate(brCode, i,targetType, planDateYear);
            return results;
        }catch (Exception ex) {
            loggerFactory.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Transactional
    public void setStatusPlan(PlanChannelSearchRequest request){
        if (request.getId() != null) {
            try {
                MapPlanChannel plan = findPlanById(request.getId());
                boolean isBr = org.apache.commons.lang3.StringUtils.isNotEmpty(request.getBrCode());
                Date currentDate = new Date();
                plan.setStatus(request.getStatus());
                plan.setUpdatedDatetime(currentDate);
                plan.setChannelComment(request.getComment());
                if (isBr) {
                    StringBuilder sb = new StringBuilder(plan.getApprovedUser());
                    sb.append(", ").append(request.getStaffCode()).append("(Sale)");
                    plan.setApprovedUser(sb.toString());
                } else {
                    plan.setApprovedUser(request.getStaffCode());
                }
                plan.setApprovedDatetime(currentDate);

                if (isBr) {
                    MapPlanChannelTarget target = findTargetById(request.getTargetId());
                    Calendar cal = Calendar.getInstance();
                    int currentMonth = cal.get(Calendar.MONTH);
                    switch (currentMonth) {
                        case 1:
                            target.setT1Plan(NumberUtils.plusInt(target.getT1Plan(), 1));
                            break;
                        case 2:
                            target.setT2Plan(NumberUtils.plusInt(target.getT2Plan(), 1));
                            break;
                        case 3:
                            target.setT3Plan(NumberUtils.plusInt(target.getT3Plan(), 1));
                            break;
                        case 4:
                            target.setT4Plan(NumberUtils.plusInt(target.getT4Plan(), 1));
                            break;
                        case 5:
                            target.setT5Plan(NumberUtils.plusInt(target.getT5Plan(), 1));
                            break;
                        case 6:
                            target.setT6Plan(NumberUtils.plusInt(target.getT6Plan(), 1));
                            break;
                        case 7:
                            target.setT7Plan(NumberUtils.plusInt(target.getT7Plan(), 1));
                            break;
                        case 8:
                            target.setT8Plan(NumberUtils.plusInt(target.getT8Plan(), 1));
                            break;
                        case 9:
                            target.setT9Plan(NumberUtils.plusInt(target.getT9Plan(), 1));
                            break;
                        case 10:
                            target.setT10Plan(NumberUtils.plusInt(target.getT10Plan(), 1));
                            break;
                        case 11:
                            target.setT11Plan(NumberUtils.plusInt(target.getT11Plan(), 1));
                            break;
                        case 12:
                            target.setT12Plan(NumberUtils.plusInt(target.getT12Plan(), 1));
                            break;
                    }
                    target.summaryTotalPlan();
                    target.setUpdatedDate(currentDate);
                    target.setUpdateUser(request.getStaffCode());

                    mapPlanChannelRepository.save(plan);
                    mapPlanChannelTargetRepository.save(target);
                } else {
                    mapPlanChannelRepository.save(plan);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MapPlanChannel findPlanById(Long id) {
        try {
            MapPlanChannel planChannel = mapPlanChannelRepository.getById(id);
            return planChannel;
        }catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }
        return null;
    }

    public MapPlanChannelTarget findTargetById(Long id) {
        try {
            MapPlanChannelTarget mapPlanChannelTarget = mapPlanChannelTargetRepository.getById(id);
            return mapPlanChannelTarget;
        }catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }
        return null;
    }

    public List<StaffDto> listPointsNearChannelPlaned(PlanChannelSearchRequest request) {
        try {
            List<StaffDto> results = new LinkedList<>();
            List<AppParamsSM> appParams = appParamService.getParamsSMByTypeAndCode("QLKD_MAP_PLAN_CHANNEL_TYPE", "CHANNEL_DISTANCE_SHOW");
            Double distance = 0.3;
            if (CollectionUtils.isNotEmpty(appParams)) {
                String value = appParams.get(0).getValue();
                try {
                    distance = Double.valueOf(value);
                } catch (Exception ex) {
                    distance = 0.3;
                }
            }
            if (Constant.CHANNEL_TYPE_ID_DF == request.getChannelTypeId()) {
                List<StaffDto> lst = staffRepository.getListDFsByDistance(request.getX(),request.getY(),distance);
                if (CollectionUtils.isNotEmpty(lst)) {
                    results.addAll(lst);
                }
            } else {
                List<StaffDto> lst = staffRepository.getChannelByDistance(request.getX(),request.getY(),distance,request.getChannelTypeId());
                if (CollectionUtils.isNotEmpty(lst)) {
                    results.addAll(lst);
                }
            }
            List<StaffDto> lst = staffRepository.getListChannelPlanApprovedByDistance(request.getX(),request.getY(),distance,request.getChannelTypeId());
            if (CollectionUtils.isNotEmpty(lst)) {
                results.addAll(lst);
            }

            StaffDto selected = new StaffDto();
            selected.setLatitude(request.getX());
            selected.setLongitude(request.getY());
            selected.setObjectType(String.valueOf(3));
            selected.setChannelObjectType("1");
            results.add(selected);

            StaffDto deployed = new StaffDto();
            MapPlanChannel planed = findPlanById(request.getId());
            if (planed != null && org.apache.commons.lang3.StringUtils.isNotEmpty(planed.getChannelCodeDeployed())) {
                String code = planed.getChannelCodeDeployed().trim();
                if (Constants.CHANNEL_TYPE_ID_POS == planed.getChannelTypeId()) {
                    Staff pdv = staffRepository.getByStaffCode(code);
                    deployed.setObjectType(String.valueOf(4));
                    deployed.setChannelObjectType("2");
                    if (pdv != null && pdv.getX() != null && pdv.getY() != null) {
                        deployed.setLatitude(pdv.getX());
                        deployed.setLongitude(pdv.getY());
                    } else if (pdv == null) {
                        deployed.setNote("El canal no se encuentra en la lista de PDV"); // Channel is not found in PDV list
                    } else {
                        deployed.setNote("La latitud y la longitud no se encuentran"); // Latitude and longitude is not found
                    }
                } else {
                    Shop df = shopRepository.getShopByShopCode(code);
                    deployed.setObjectType(String.valueOf(4));
                    deployed.setChannelObjectType("1");
                    if (df != null && df.getX() != null && df.getY() != null) {
                        deployed.setLatitude(df.getX());
                        deployed.setLongitude(df.getY());
                    } else if (df == null) {
                        deployed.setNote("El canal no se encuentra en la lista de compras"); // Channel is not found in shop list
                    } else {
                        deployed.setNote("La latitud y la longitud no se encuentran"); // Latitude and longitude is not found
                    }
                }
                results.add(deployed);
            }
            results.forEach(r -> {
                if (r.getObjectType().equals("1")) {
                    r.setIconUrl(String.format("assets/channels/1-%s-red.png", request.getChannelTypeId()));
                } else if (r.getObjectType().equals("2")) {
                    r.setIconUrl(String.format("assets/channels/1-%s.png", request.getChannelTypeId()));
                } else if (r.getObjectType().equals("3")) {
                    r.setIconUrl(String.format("assets/channels/1-%s-green.png", request.getChannelTypeId()));
                } else {
                    r.setIconUrl(String.format("assets/channels/1-%s-violet.png", request.getChannelTypeId()));
                }
            });
            return results;
        }catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        return null;
    }

    public MapPlanChannelSearchListResponse searchPlanChannelDevelop(PlanChannelSearchRequest request) {
       try {
           MapPlanChannelSearchListResponse response = new MapPlanChannelSearchListResponse();
           List<MapPlanChannelDevelop> channelDevelops = mapPlanChannelRepository.SearchMapPlanDevelop(request.getBrCode(),request.getBcCode(),request.getStaffCode(),request.getChannelTypeId(),request.getFromDate(),request.getToDate(),request.getStatus());
           if (request.isPaging()) {
               int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
               int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 1;
               Long countResults = (long) channelDevelops.size();
               int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
               if (currentPage < 1) {
                   currentPage = 1;
               } else if (currentPage > lastPageNumber) {
                   currentPage = lastPageNumber;
               }
               response.setTotalRecord(countResults);
               response.setTotalPage(lastPageNumber);
               response.setCurrentPage(currentPage);
               int startIndex = (currentPage - 1) * pageSize;
               if (startIndex < 0) {
                   startIndex = 0;
               }
               int endIndex = Math.min(startIndex + pageSize, channelDevelops.size());
               response.setChannelDevelops(channelDevelops.subList(startIndex, endIndex));
           }
           return response;
       }catch (Exception e) {
           loggerFactory.error(e.getMessage(), e);
       }
       return null;
    }

    public<T> List<T> convertToExcelPlanChannel(File excelFile, Class<T> bean) throws IOException {
        List<T> result = new ArrayList<>();
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().headerCount(2).headerMapper(headerMapper).addListDelimiter(";").build();
        List<T> rows =  Poiji.fromExcel(excelFile, bean,options);
        result.addAll(rows);
        return result;
    }

    public String buildImportResultFile(List<PlanChannelSale> targets, int level) {
        String templateFolder = this.templateFolder;
//        String templateFolder = "C:/Users/ADMIN/Desktop/excel_check_list";
        String fileName = "import_sale_plan_target_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
//        String fileNameFull = "C:/Users/ADMIN/Desktop/excel_result" + File.separator + fileName;
        String fileTemplate = "Template_import_channel_target_sale_result.xls";
        if (level == 2) {
            fileTemplate = "Template_import_channel_target_br_result.xls";
        } else if (level == 3) {
            fileTemplate = "Template_import_channel_target_bc_result.xls";
        }
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", targets);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String searchPlan2Export(PlanChannelSearchRequest request) {
//        List<PlanChannelTargetResponse> results = searchToResponse(session, request);
        List<TargetChannelDto> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("brCode", request.getBrCode());
        params.put("bcCode", request.getBcCode());
        params.put("staffCode", request.getStaffCode());
        params.put("channelTypeId", request.getChannelId());
        params.put("targetLevel", request.getTargetLevel());
        params.put("year", request.getPlanDate());
        String templateFolder = this.templateFolder;
        String fileName = "plan_channel_target_search_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "Template_channel_target_sale_search_v2.xls";

        if(request.getTargetLevel() == 1) {
            list = mapPlanChannelTargetRepository.searchExportTargetChannelLevelSale(request.getBrCode(), request.getBcCode(), request.getStaffCode(), request.getChannelTypeId(), request.getTargetLevel(), request.getPlanDate());
        }else if (request.getTargetLevel() == 2 || request.getTargetLevel() == 3) {
            fileTemplate = "Template_channel_target_br_search_v2.xls";
            if (request.getTargetLevel() == 3) {
                fileTemplate = "Template_channel_target_bc_search_v2.xls";
            }
            list = mapPlanChannelTargetRepository.searchExportTargetChannelLevelBCAccum(request.getBrCode(), request.getBcCode(), request.getStaffCode(), request.getChannelTypeId(), request.getTargetLevel(), request.getPlanDate());
        }

        File templateFile = new File(templateFolder, fileTemplate); // đường dẫn file mẫu
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", list);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            loggerFactory.error("An error occurred while searching for review results", e);
        }
        return null;
    }

    public String exportChannelDevelopReport(PlanChannelSearchRequest request) {
        String templateFolder = this.templateFolder;
        String fileName = "sale_control_report_" + Calendar.getInstance().getTimeInMillis() + ".xlsx";
        String fileNameFull =this.fileNameFullFolder + File.separator + fileName;

        File templateFile = new File(templateFolder, "Channel_Develop.jrxml");
        Map<String, Object> params = new HashMap<>();
        params.put("p_year", request.getPlanDate());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(request.getBrCode())) {
            params.put("p_br", " AND tblOrg.BR_CODE ='" + request.getBrCode() + "'");
        } else {
            params.put("p_br", " ");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(request.getBcCode())) {
            params.put("p_bc", " AND tblOrg.BC_CODE ='" + request.getBcCode() + "'");
        } else {
            params.put("p_bc", " ");
        }
        if (request.getChannelTypeId() != null) {
            params.put("p_channel_type_id", " AND tblOrg.CHANNEL_ID =" + request.getChannelTypeId());
        } else {
            params.put("p_channel_type_id", " ");
        }
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(templateFile.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource.getConnection());
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setDetectCellType(true); // Detect cell types (date and etc.)
            configuration.setWhitePageBackground(false); // No white background!
            configuration.setFontSizeFixEnabled(false);

            // No spaces between rows and columns
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setRemoveEmptySpaceBetweenColumns(true);

            JasperReportExporter instance = new JasperReportExporter();
            byte[] fileInBytes = instance.exportToXlsx(jasperPrint, "Channel Develop");
            FileUtils.writeByteArrayToFile(new File(fileNameFull), fileInBytes);

            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}

