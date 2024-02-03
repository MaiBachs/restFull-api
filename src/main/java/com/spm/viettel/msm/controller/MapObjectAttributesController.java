package com.spm.viettel.msm.controller;

import com.spm.viettel.msm.dto.MapObjectAttributeDto;
import com.spm.viettel.msm.dto.StaffDto;
import com.spm.viettel.msm.dto.request.MapObjectAttributeRequest;
import com.spm.viettel.msm.dto.response.MapObjectAttrReponse;
import com.spm.viettel.msm.factory.ResponseFactory;
import com.spm.viettel.msm.service.*;
import com.spm.viettel.msm.utils.Constants;
import com.spm.viettel.msm.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/map-object-attribute")
public class MapObjectAttributesController extends BaseController{

    private final Logger loggerFactory = LoggerFactory.getLogger(MapObjectAttributesController.class);
    private final HttpServletRequest request;
    private final ResponseFactory responseFactory;
    private final AuthenticateService authenticateService;
    private final MapObjectInfoService mapObjectInfoService;
    private final MapObjectInfoImageService mapObjectInfoImageService;
    private final ChannelTypeService channelTypeService;
    private final AppParamService appParamService;
    private final StaffProfileService staffProfileService;
    private final StaffService staffService;

    @Autowired
    @Qualifier("SMSessionFactory")
    private SessionFactory smSessionFactory;

    @Autowired
    @Qualifier("anypaySessionFactory")
    private SessionFactory anypaySessionFactory;

    @Autowired
    public MapObjectAttributesController(HttpServletRequest request, ResponseFactory responseFactory, AuthenticateService authenticateService, MapObjectInfoService mapObjectInfoService, MapObjectInfoImageService mapObjectInfoImageService, ChannelTypeService channelTypeService, AppParamService appParamService, StaffProfileService staffProfileService, StaffService staffService) {
        this.request = request;
        this.responseFactory = responseFactory;
        this.authenticateService = authenticateService;
        this.mapObjectInfoService = mapObjectInfoService;
        this.mapObjectInfoImageService = mapObjectInfoImageService;
        this.channelTypeService = channelTypeService;
        this.appParamService = appParamService;
        this.staffProfileService = staffProfileService;
        this.staffService = staffService;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @PostMapping("/")
    public ResponseEntity<?> searchObjectAttributes(@RequestBody MapObjectAttributeRequest request) throws Exception {
        MapObjectAttrReponse result = new MapObjectAttrReponse();
        if (request.getMapObjectId() != null && request.getMapObjectId() > 0) {
            Session sessionSM = smSessionFactory.openSession();
            Session sessionANY = anypaySessionFactory.openSession();

            if (StringUtils.isEmpty(request.getFromDate())) {
                Date from = DateUtil.addDay(new Date(), -31);
                request.setFromDate(DateUtil.convertDateTimeToString(from, Constants.DD_MM_YYYY));
            }
            Date toDateTime = new Date();
            if (StringUtils.isNotEmpty(request.getToDate())) {
                toDateTime = DateUtil.convertStringToTime(request.getToDate(), Constants.DD_MM_YYYY);
            }
            toDateTime = DateUtil.addDay(toDateTime, 1);
            request.setToDate(DateUtil.convertDateTimeToString(toDateTime, Constants.DD_MM_YYYY));
            try {
                result.setMapObjectAttributes(new ArrayList<>());
                if (request.getObjectType() != null && request.getObjectType() == 2) {
                    result.setMapObjectAttributes(mapObjectInfoService.getAttributesByObjectId(sessionSM, request.getMapObjectId()));
                    result.setMapObjectInfoImages(mapObjectInfoImageService.getImagesBase64(sessionSM, request.getMapObjectId()));
                } else if (request.getObjectType() != null && request.getObjectType() == 1) {
                    List<String> list = appParamService.getListChannelAllowPlanVisit();
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
//                        for (String s : list) {
//                            if (s.equalsIgnoreCase(channelTypeId.toString())) {
//                                MapObjectAttribute plan = new MapObjectAttribute();
//                                plan.setNameES("Planes asignados");
//                                plan.setNameEN("Plans");
//                                plan.setNameVN("Kế hoạch");
//                                plan.setType(1);
//                                plan.setValue(MapObjectInfoBiz.getPlan4Object(sessionSM, getPvdCode(), getChannelObjectType(), getFromDate(), getToDate()));
////                                plan.setValue("01/02/2020;02/02/2020;03/02/2020;05/02/2020;27/02/2020;28/02/2020;01/03/2020;02/03/2020");
////                                if (StringUtils.isNotEmpty(plan.getValue())) {
//                                    mapObjectAttributes.add(plan);
////                                }
//
//                                MapObjectAttribute checkedIn = new MapObjectAttribute();
//                                checkedIn.setNameES("Registro de Check IN");
//                                checkedIn.setNameEN("Check in");
//                                checkedIn.setNameVN("Chăm sóc");
//                                checkedIn.setType(2);
////                            checkedIn.setCommentVN("Dữ liệu đã được tổng hợp từ ngày hôm trước");
////                            checkedIn.setCommentEN("Data has been collected from the previous day");
////                            checkedIn.setCommentES("Se han recopilado datos del día anterior");
//                                checkedIn.setValue(MapObjectInfoBiz.getCheckedIn4Object(sessionSM, getPvdCode(), getFromDate(), toDate));
////                                checkedIn.setValue("23/01/2020;25/01/2020;30/01/2020;01/02/2020;02/02/2020;03/02/2020;28/02/2020;01/03/2020;02/03/2020");
////                                if (StringUtils.isNotEmpty(checkedIn.getValue())) {
//                                    mapObjectAttributes.add(checkedIn);
////                                }
//                            }
//                        }
                    }
                    result.setMapObjectInfoImages(staffProfileService.getStaffImages(sessionSM, request.getMapObjectId()));
                    StaffDto staffDto = new StaffDto();
                    staffDto.setId(request.getMapObjectId());
                    List<StaffDto> staffBeanList = Arrays.asList(staffDto);
                    if (request.getChannelObjectType() == 2){
                        staffService.setAnypay4Staff(sessionANY, staffBeanList);
                        result.setActiveNumber(staffService.getTotalActive4Staff(sessionSM,request.getMapObjectId()));
                    }else {
                        staffService.setAnypay4Shop(sessionANY, staffBeanList);
                        result.setActiveNumber(staffService.getTotalActive4Shop(sessionSM,request.getMapObjectId()));
                    }
                    result.setAnypay(staffBeanList.get(0).getAnypay());

//                    TODO: lay danh sach promoter
                    result.setPromoterList(staffService.getListPromoter(sessionSM,request.getMapObjectId(), Constants.CHANNEL_TYPE_PROMOTOR));
                    result.setAfList(staffService.getListPromoter(sessionSM,request.getMapObjectId(), Constants.CHANNEL_TYPE_AF));
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(result.getPromoterList())) {
                        result.getPromoterList().forEach(p -> p.setChannelObjectType("2"));
                        result.setPromoterList(staffService.setAnypay4Staff(sessionANY, result.getPromoterList()));
                    }
                    if (CollectionUtils.isNotEmpty(result.getAfList())) {
                        result.getAfList().forEach(p -> p.setChannelObjectType("2"));
                        result.setAfList(staffService.setAnypay4Staff(sessionANY, result.getAfList()));
                    }
//                    promoterList = StaffBiz.setActiveNumber( sessionSM , sessionsetActiveNumber, promoterList);

                } else if (request.getObjectType() != null && request.getObjectType() == 3) {
                    List<MapObjectAttributeDto> attributes = new ArrayList<MapObjectAttributeDto>();
                    MapObjectAttributeDto totalRegisters = new MapObjectAttributeDto();
                    totalRegisters.setNameES("Registro total");
                    totalRegisters.setNameEN("Total register");
                    totalRegisters.setNameVN("Total register");
                    totalRegisters.setType(3);
                    totalRegisters.setValue(mapObjectInfoService.getTotalRegisterOfBTS(sessionSM,request.getMapObjectId()));
                    totalRegisters.setCommentEN(mapObjectInfoService.get3G4GOfBTS(sessionSM,request.getMapObjectId())); //1005464L
                    attributes.add(totalRegisters);
                    MapObjectAttributeDto population = new MapObjectAttributeDto();
                    population.setNameES("Población");
                    population.setNameEN("Population");
                    population.setNameVN("Dân số");
                    population.setValue(MapObjectInfoService.getPopulationOfBTS(sessionSM,request.getMapObjectId()));
                    attributes.add(population);
                    result.setMapObjectAttributes(attributes);
                }

            } catch (Exception e) {
                loggerFactory.error(e.getMessage());
            }finally {
                smSessionFactory.close();
                anypaySessionFactory.close();
            }
        }
        return responseFactory.success(result);
    }
}
