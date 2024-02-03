package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.GetChannelCodeDto;
import com.spm.viettel.msm.dto.request.SearchChannelCodeRequest;
import com.spm.viettel.msm.repository.sm.ShopRepository;
import com.spm.viettel.msm.repository.sm.StaffRepository;
import com.spm.viettel.msm.repository.sm.entity.Staff;
import com.spm.viettel.msm.repository.smartphone.StaffSmartPhoneRepository;
import com.spm.viettel.msm.repository.smartphone.entity.StaffSmartPhone;
import com.spm.viettel.msm.utils.Constants;
import freemarker.template.TemplateException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class StaffService extends BaseService{
    private final Logger loggerFactory = LoggerFactory.getLogger(StaffService.class);

    private final StaffRepository staffRepository;

    private final ShopRepository shopRepository;

    private final StaffSmartPhoneRepository staffSmartPhoneRepository;
    private final ChannelTypeService channelTypeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${STAFF_MANAGER_LIST}")
    private String staffManagerList;

    @Autowired
    public StaffService(StaffRepository staffRepository, ShopRepository shopRepository, StaffSmartPhoneRepository staffSmartPhoneRepository, ChannelTypeService channelTypeService, JdbcTemplate jdbcTemplate) {
        this.staffRepository = staffRepository;
        this.shopRepository = shopRepository;
        this.staffSmartPhoneRepository = staffSmartPhoneRepository;
        this.channelTypeService = channelTypeService;
    }

    public List<TemplateUpdateLoationDto> updateLocations(List<TemplateUpdateLoationDto> dataFiles, UserTokenDto userInfo) {
        List<TemplateUpdateLoationDto> dataRes = new ArrayList<>();
        try {
            for (TemplateUpdateLoationDto data : dataFiles) {
                int qttUpdate = staffRepository.updateLocationsForStaff(data.getLongitude(), data.getLatitude(), data.getRadius(), userInfo.getStaffCode(), new Date(), data.getChannelCodeId());
                if (qttUpdate > 0) {
                    dataRes.add(data);
                }
            }
        } catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }
        return dataRes;
    }

    public List<StaffBaseDto> getListEmployeeOfShop(Long shopId, Integer hasChild, String userType, String channelCode, Integer status) {
        return staffRepository.getListEmployeeOfShop(shopId, status, userType,hasChild, channelCode);
    }

    public StaffBaseDto getStaffZonalAgent(Long staffId) {
        if(staffId!=null){
            return staffRepository.getStaffZonalAgent(staffId);
        }
        return null;
    }

    public Staff getByStaffCode(String staffCode) {
        return staffRepository.getByStaffCode(staffCode);
    }

    public Staff searchByUserNameOfSM(String userCode) {
        return staffRepository.getStaffByStaffCode(userCode);
    }

    public StaffSmartPhone searchByUserNameOfSmartPhone(String userCode) {
        try {
            return staffSmartPhoneRepository.getStaffByStaffCode(userCode);
        }catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }
        return null;
    }

    public List<StaffSmartPhone> getStaffAuditorInSmartPhones() {
        try {
            return staffSmartPhoneRepository.getStaffAuditor();
        }catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }
        return null;
    }

//    public List<Staff> getStaffByShopIdAndChannelTypeId (Long shopId,Long channelTypeId){
//        return staffRepository.findStaffByShopIdAndChannelTypeId(shopId,channelTypeId);
//    }

    public List<StaffDto> getStaffByShopIdAndChannelTypeId(GetChannelCodeDto req) {
        List<StaffDto> staffList = new ArrayList<StaffDto>();
        try {
            staffList = staffRepository.findStaffByShopIdAndChannelTypeId(req.getShopId(), req.getChannelTypeId(), req.getOwnerId());
        } catch (Exception e) {
            loggerFactory.error(e.getMessage(), e);
        }
        return staffList;
    }

    public Page<StaffDto> getChannelsForUpdateLocation(SearchChannelCodeRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getCurrentPage() > 0 ? request.getCurrentPage() - 1 : 0, request.getPageSize());
        Page<StaffDto> channelList = null;
        if (request.getChannelTypeId() != null) {
            if (request.getObjectType() == 2) {
                channelList = staffRepository.getListChannelByShopAndChannelTypeAndStaffCodeAndOwner(request.getShopId(), request.getChannelTypeId(), "%" + request.getCode() + "%", request.getOwnerId(), pageRequest);
            } else {
                channelList = shopRepository.getListChannelByShopAndChannelTypeAndCodeAndOwner(request.getShopId(), request.getChannelTypeId(), "%" + request.getCode() + "%", request.getOwnerId(), pageRequest);
            }
        } else {
            channelList = shopRepository.getListChannelByShopAndChannelTypeAndCodeAndOwner(request.getShopId(), request.getChannelTypeId(), "%" + request.getCode() + "%", request.getOwnerId(), pageRequest);
        }
        return channelList;
    }

    public boolean isManager(String posCode){
        if(StringUtils.isNotEmpty(posCode)){
            String[] managerList = staffManagerList.split(";");
            for (String s : managerList) {
                if(s.equalsIgnoreCase(posCode)) return true;
            }
        }
        return false;
    }

    public StaffBaseDto isZonalAgent( Long staffId) {
        StaffBaseDto result = staffRepository.isZonalAgent(staffId);
        if (result != null) {
            return result;
        }
        return null;
    }

    public List<StaffDto> listStaff(Session sessionSM, Session anypaySession,Map<String, Object> params, int type, boolean hasAnypay, boolean hasActiveCount) throws TemplateException, IOException {
        if (params.get("staffId") != null) {
            return listStaffByManager(sessionSM, anypaySession,params, type, hasAnypay, hasActiveCount);
        } else {
            return listStaffByBranchOrBU(sessionSM, anypaySession,params, type, hasAnypay, hasActiveCount);
        }
    }

    public List<StaffDto> listStaffByManager(Session sessionSM, Session anypaySession,Map<String, Object> params, int type, boolean hasAnypay, boolean hasActiveCount) throws TemplateException, IOException {
        List<ChannelWithGroupDTO> channelTypes = channelTypeService.getListChannelWithGroup();
        List<Long> channelTypeIdsObject1 = new ArrayList<>();
        List<Long> channelTypeIdsObject2 = new ArrayList<>();
        channelTypeExtract(channelTypes, channelTypeIdsObject1, channelTypeIdsObject2);

        /**
         * B2 lay thong tin cac staff tu DB smartphone. *
         */
        List<StaffDto> listStaffs = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (!channelTypeIdsObject1.isEmpty()) {
            sb.append("SELECT DISTINCT sf.staff_id AS id, sf.staff_code AS code, sf.name AS name ");
            sb.append(", sf.channel_type_id AS channelTypeId, sf.address AS address");
            sb.append(", sf.x AS x, sf.y AS y");
            sb.append(", sf.tel AS tel, 2 AS channelObjectType ");
            sb.append(", sf.CHILD_CHANNEL AS rank ");
            sb.append(", man.staff_id AS staffOwnerId, man.name AS staffOwnerName");
            sb.append(", man.staff_code AS staffOwnerCode, man.tel AS staffOwnerTel");
            sb.append(", aa.isdn AS isdnAgent");
            sb.append(", ").append(type).append("AS type ");
            sb.append(", ").append(1).append("AS objectType ");
            sb.append(", shp.shop_code AS shopCode");
            sb.append(", 1 AS allowVisitPlan");
            if (hasActiveCount) {
                sb.append(", NVL(viewacumulate.total_acumulate,0) AS totalAcumulate");
            } else {
                sb.append(", 0 AS totalAcumulate  ");
            }
            sb.append(" FROM staff sf LEFT JOIN  staff man ON (sf.staff_owner_id=man.staff_id) "); //, channel_type cte
            sb.append(" LEFT JOIN account_agent aa ON (sf.staff_id = aa.owner_id AND aa.OWNER_TYPE = 2)");
            sb.append(" LEFT JOIN shop shp ON (sf.shop_id = shp.shop_id)");
            if (hasActiveCount) {
                sb.append(getTotalActiveSQL(true));
            }
            sb.append(" WHERE sf.status=1 ");
            sb.append("  and sf.x is not null ");
            sb.append("  and sf.y is not null ");

            List lstParam = new ArrayList();

            if (params.get("staffId") != null) {
                sb.append(" AND man.staff_id = :staffId");
                lstParam.add(params.get("staffId"));
            }
            if (params.get("shopId") != null) {
                sb.append(" AND sf.shop_id IN (SELECT stree.shop_id FROM sm.tbl_shop_tree stree WHERE stree.root_id = :shopId )");
                lstParam.add(params.get("shopId"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("province")) && !"-1".equals(params.get("province"))) {
                sb.append(" and sf.province = :province ");
                lstParam.add(params.get("province"));
            }

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("district")) && !"-1".equals(params.get("district"))) {
                sb.append(" and sf.district  = :district ");
                lstParam.add(params.get("district"));
            }

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("precinct")) && !"-1".equals(params.get("precinct"))) {
                sb.append(" and sf.precinct  = :precinct ");
                lstParam.add(params.get("precinct"));
            }
            if (params.get("channelCode") != null) {
                sb.append(" AND UPPER(sf.staff_code) LIKE :channelCode");
                lstParam.add(params.get("channelCode"));
            }
//            if (params.get("ranksValue") != null) {
//                sb.append(" AND sf.CHILD_CHANNEL = ?");
//                lstParam.add(params.get("ranksValue"));
//            }
            sb.append(" AND  sf.channel_type_id IN ( :channelTypeIdsObject1 ) ");
            sb.append(" AND sf.status=1 ");
            sb.append(Constants._AND_ROWNUM__20_);
            sb.append(" ORDER BY sf.staff_code,sf.name");

            NativeQuery query = sessionSM.createSQLQuery(sb.toString());
            query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
            query.addScalar("id", LongType.INSTANCE);
            query.addScalar("type", LongType.INSTANCE);
            query.addScalar("code", StringType.INSTANCE);
            query.addScalar("name", StringType.INSTANCE);
            query.addScalar("channelTypeId", LongType.INSTANCE);
            query.addScalar("address", StringType.INSTANCE);
            query.addScalar("x", DoubleType.INSTANCE).addScalar("y", DoubleType.INSTANCE);
            query.addScalar("tel", StringType.INSTANCE);
            query.addScalar("channelObjectType", StringType.INSTANCE);
            query.addScalar("rank", StringType.INSTANCE);
            query.addScalar("staffOwnerId", LongType.INSTANCE);
            query.addScalar("staffOwnerName", StringType.INSTANCE);
            query.addScalar("staffOwnerCode", StringType.INSTANCE);
            query.addScalar("staffOwnerTel", StringType.INSTANCE);
            query.addScalar("isdnAgent", StringType.INSTANCE);
            query.addScalar("objectType", StringType.INSTANCE);
            query.addScalar("shopCode", StringType.INSTANCE);
            query.addScalar("allowVisitPlan", IntegerType.INSTANCE);
            query.addScalar("totalAcumulate", IntegerType.INSTANCE);
            for (int i = 0; i < lstParam.size(); i++) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue().equals(lstParam.get(i))) {
                        query.setParameter(entry.getKey(), lstParam.get(i));
                        break;
                    }
                }
            }
            query.setParameter("channelTypeIdsObject1", channelTypeIdsObject1);
            List<StaffDto> list = query.getResultList();

            /**
             * B3 lay thong tin Anypay cho cac staff tu DB Anypay. *
             */
            if (list != null && !list.isEmpty() && hasAnypay) {
                list = setAnypay4Staff(anypaySession, list);
            }
//            /**
//             * B4 lay thong tin Planed *
//             */
            List<List<String>> staffCodes = new ArrayList<>();
            List<List<Long>> staffIds = getListIdOfStaffs(list, staffCodes);
            Map<String, StaffDto> StaffDtoMap = new HashMap<>();
            for (StaffDto s : list) {
                StaffDtoMap.put(s.getCode().toUpperCase(), s);
            }
            setPlanedStatus4PVD(sessionSM, StaffDtoMap, staffIds, com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("fromDate")), com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("toDate")), "2");

//            /**
//             * B5 lay thong tin CheckedIn *
//             */
            setCheckedInStatus4PVD(sessionSM, StaffDtoMap, staffCodes, com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("fromDate")), com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("toDate")));
            listStaffs.addAll(list);
        }
        if (!channelTypeIdsObject2.isEmpty()) {
            sb = new StringBuilder();
            /**
             * B2 lay thong tin cac object tu staff. voi cac channel co
             * ObjectType = 1*
             */
            sb.append("SELECT DISTINCT sp.shop_id id, sp.shop_code code, sp.name name ");
            sb.append(", sp.channel_type_id channelTypeId, sp.address address");
            sb.append(", sp.x x, sp.y y ");
            sb.append(", sp.tel tel, 1 channelObjectType ");
            sb.append(", '-1' rank ");
            sb.append(", man.staff_id staffOwnerId, man.name staffOwnerName");
            sb.append(", man.staff_code staffOwnerCode, man.tel staffOwnerTel");
            sb.append(", aa.isdn isdnAgent");
            sb.append(", ").append(type).append(" type ");
            sb.append(", ").append(1).append(" objectType ");
            sb.append(", shp.shop_code shopCode");
            sb.append(", 1 AS allowVisitPlan");
            if (hasActiveCount) {
                sb.append(", ").append(getTotalActiveSQL(false));
            } else {
                sb.append(", 0 AS totalAcumulate ");
            }
            sb.append(" FROM shop sp LEFT JOIN staff man ON (sp.staff_owner_id=man.staff_id) ");
            sb.append(" LEFT JOIN account_agent aa ON (sp.shop_id = aa.owner_id AND aa.OWNER_TYPE = 1)");
            sb.append(" LEFT JOIN shop shp ON (sp.parent_shop_id = shp.shop_id)");
            if (hasActiveCount) {
                sb.append(getTotalActiveSQL(false));
            }
            sb.append(" WHERE 1=1 ");
            sb.append("  and sp.x is not null ");
            sb.append("  and sp.y is not null ");

            List lstParam = new ArrayList();

            if (params.get("staffId") != null) {
                sb.append(" AND man.staff_id = :staffId");
                lstParam.add(params.get("staffId"));
            }
            if (params.get("shopId") != null) {
                sb.append(" AND sp.shop_id IN (SELECT stree.shop_id FROM sm.tbl_shop_tree stree WHERE stree.root_id = :shopId )");
                lstParam.add(params.get("shopId"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("province")) && !"-1".equals(params.get("province"))) {
                sb.append(" and sp.province = :province ");
                lstParam.add(params.get("province"));
            }

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("district")) && !"-1".equals(params.get("district"))) {
                sb.append(" and sp.district  = :district ");
                lstParam.add(params.get("district"));
            }

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("precinct")) && !"-1".equals(params.get("precinct"))) {
                sb.append(" and sp.precinct  = :precinct ");
                lstParam.add(params.get("precinct"));
            }
            if (params.get("channelCode") != null) {
                sb.append(" AND UPPER(sp.shop_code) LIKE :channelCode");
                lstParam.add(params.get("channelCode"));
            }
            sb.append(" AND  sp.channel_type_id IN ( :channelTypeIdsObject2 ) ");
            sb.append(" AND sp.status=1 ");
            sb.append(Constants._AND_ROWNUM__20_);
            sb.append(" ORDER BY sp.shop_code, sp.name");

            NativeQuery query = sessionSM.createSQLQuery(sb.toString());
            query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
            query.addScalar("id", LongType.INSTANCE);
            query.addScalar("type", LongType.INSTANCE);
            query.addScalar("code", StringType.INSTANCE);
            query.addScalar("name", StringType.INSTANCE);
            query.addScalar("channelTypeId", LongType.INSTANCE);
            query.addScalar("address", StringType.INSTANCE);
            query.addScalar("x", DoubleType.INSTANCE).addScalar("y", DoubleType.INSTANCE);
            query.addScalar("tel", StringType.INSTANCE);
            query.addScalar("channelObjectType", StringType.INSTANCE);
            query.addScalar("rank", StringType.INSTANCE);
            query.addScalar("staffOwnerId", LongType.INSTANCE);
            query.addScalar("staffOwnerName", StringType.INSTANCE);
            query.addScalar("staffOwnerCode", StringType.INSTANCE);
            query.addScalar("staffOwnerTel", StringType.INSTANCE);
            query.addScalar("isdnAgent", StringType.INSTANCE);
            query.addScalar("objectType", StringType.INSTANCE);
            query.addScalar("shopCode", StringType.INSTANCE);
            query.addScalar("allowVisitPlan", IntegerType.INSTANCE);
            query.addScalar("totalAcumulate", IntegerType.INSTANCE);

            query.setParameterList("channelTypeIdsObject2", channelTypeIdsObject2);
//            System.out.println("====channelTypeIdsObject2: "+ org.apache.commons.lang3.StringUtils.join(channelTypeIdsObject2, ','));
            for (int i = 0; i < lstParam.size(); i++) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue().equals(lstParam.get(i))) {
                        query.setParameter(entry.getKey(), lstParam.get(i));
                        break;
                    }
                }
            }
            List<StaffDto> list2 = query.list();

            String userType = params.get("userType") == null? null: (String) params.get("userType");
            if (org.apache.commons.lang3.StringUtils.isEmpty(userType) || Constants.POS_CODE_AF.equalsIgnoreCase(userType)){
                Long shopId = (Long) params.get("shopId");
                String channelCode = params.get("channelCode")==null? null: (String) params.get("channelCode");
//                List<StaffDto> afStaffs = getListEmployeesOfShop(sessionSM, shopId, true, userType, channelCode, 1);
//                List<Long> afStaffIds = afStaffs.stream().map(s -> s.getId()).collect(Collectors.toList());
                List<StaffDto> dfs = getListDFs(sessionSM, userType, channelCode, shopId, null);
                if (CollectionUtils.isEmpty(list2)){
                    list2 = new ArrayList<>();
                }
                list2.addAll(dfs);
            }

//            /**
//             * B3 lay thong tin Anypay cho cac staff tu DB Anypay. *
//             */
            if (list2 != null && !list2.isEmpty() && hasAnypay) {
                list2 = setAnypay4Shop(anypaySession, list2);
            }
//
//            /**
//             * B4 lay thong tin Planed *
//             */
            List<List<String>> staffCodes = new ArrayList<>();
            List<List<Long>> staffIds = getListIdOfStaffs(list2, staffCodes);
            Map<String, StaffDto> staffBeanMap = new HashMap<>();
            for (StaffDto s : list2) {
                staffBeanMap.put(s.getCode().toUpperCase(), s);
            }
            setPlanedStatus4PVD(sessionSM, staffBeanMap, staffIds, com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("fromDate")), com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("toDate")), "1");

//            /**
//             * B5 lay thong tin CheckedIn *
//             */
            setCheckedInStatus4PVD(sessionSM, staffBeanMap, staffCodes, com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("fromDate")), com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("toDate")));

            listStaffs.addAll(list2);
        }
//
        loggerFactory.debug("Total staff: " + listStaffs.size());
        return listStaffs;
    }

    public List<StaffDto> listStaffByBranchOrBU(Session sessionSM, Session anypaySession,Map<String, Object> params, int type, boolean hasAnypay, boolean hasActiveCount) throws TemplateException, IOException {
        List<ChannelWithGroupDTO> channelTypes = channelTypeService.getListChannelWithGroup();
        List<Long> channelTypeIdsObject1 = new ArrayList<>();
        List<Long> channelTypeIdsObject2 = new ArrayList<>();
        channelTypeExtract(channelTypes, channelTypeIdsObject1, channelTypeIdsObject2);
        /**
         * B1 lay thong tin cac object tu staff. voi cac channel co ObjectType =
         * 2*
         */
        List<StaffDto> listStaffs = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (!channelTypeIdsObject1.isEmpty()) {
            List lstParam = new ArrayList();
            sb.append("SELECT DISTINCT sf.staff_id id, sf.staff_code code, sf.name name ");
            sb.append(", sf.channel_type_id channelTypeId, sf.address address");
            sb.append(", sf.x x, sf.y y ");
            sb.append(", sf.tel tel, 2 channelObjectType ");
            sb.append(", sf.CHILD_CHANNEL rank ");
            sb.append(", man.staff_id staffOwnerId, man.name staffOwnerName");
            sb.append(", man.staff_code staffOwnerCode, man.tel staffOwnerTel");
            sb.append(", aa.isdn isdnAgent"); //cte.name as channelName,
            sb.append(", ").append(type).append(" type ");
            sb.append(", ").append(1).append(" objectType ");
            sb.append(", shp.shop_code shopCode");
            sb.append(", 1 AS allowVisitPlan");
            if (hasActiveCount) {
                sb.append(", NVL(viewacumulate.total_acumulate,0) AS totalAcumulate");
            } else {
                sb.append(", 0 AS totalAcumulate    ");
            }
            sb.append(" FROM staff sf LEFT JOIN  staff man ON (sf.staff_owner_id=man.staff_id) "); //, channel_type cte
            sb.append(" LEFT JOIN account_agent aa ON (sf.staff_id = aa.owner_id AND aa.OWNER_TYPE = 2)");
            sb.append(" LEFT JOIN shop shp ON (sf.shop_id = shp.shop_id)");
            if (hasActiveCount) {
                sb.append(getTotalActiveSQL(true));
            }
            sb.append(" WHERE 1=1 ");
            sb.append(" AND sf.shop_id IN ( ");
            sb.append(" SELECT stree.shop_id FROM sm.tbl_shop_tree stree");
            sb.append(" WHERE stree.root_id = ").append(params.get("shopId")).append(")");
            sb.append("  and sf.x is not null ");
            sb.append("  and sf.y is not null ");
            sb.append("  and sf.status=1 ");

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("province")) && !"-1".equals(params.get("province"))) {
                sb.append(" and sf.province = :province ");
                lstParam.add(params.get("province"));
            }

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("district")) && !"-1".equals(params.get("district"))) {
                sb.append(" and sf.district  = :district ");
                lstParam.add(params.get("district"));
            }

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("precinct")) && !"-1".equals(params.get("precinct"))) {
                sb.append(" and sf.precinct  = :precinct ");
                lstParam.add(params.get("precinct"));
            }
            if (params.get("channelCode") != null) {
                sb.append(" AND UPPER(sf.staff_code) LIKE :channelCode");
                lstParam.add(params.get("channelCode"));
            }

            if (params.get("userType") != null) {
                sb.append(" AND man.POS_CODE = :userType");
                lstParam.add(params.get("userType"));
            }

//            if (params.get("ranksValue") != null) {
//                sb.append(" AND sf.CHILD_CHANNEL = ?");
//                lstParam.add(params.get("ranksValue"));
//            }

            sb.append(" AND  sf.channel_type_id IN ( :channelTypeIdsObject1 ) ");
            sb.append(Constants._AND_ROWNUM__20_);
            sb.append(" ORDER BY sf.staff_code,sf.name");

            SQLQuery query = sessionSM.createSQLQuery(sb.toString());
            query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
            query.addScalar("id", LongType.INSTANCE);
            query.addScalar("type", LongType.INSTANCE);
            query.addScalar("code", StringType.INSTANCE);
            query.addScalar("name", StringType.INSTANCE);
            query.addScalar("channelTypeId", LongType.INSTANCE);
            query.addScalar("address", StringType.INSTANCE);
            query.addScalar("x", DoubleType.INSTANCE).addScalar("y", DoubleType.INSTANCE);
            query.addScalar("tel", StringType.INSTANCE);
            query.addScalar("channelObjectType", StringType.INSTANCE);
            query.addScalar("rank", StringType.INSTANCE);
            query.addScalar("staffOwnerId", LongType.INSTANCE);
            query.addScalar("staffOwnerName", StringType.INSTANCE);
            query.addScalar("staffOwnerCode", StringType.INSTANCE);
            query.addScalar("staffOwnerTel", StringType.INSTANCE);
            query.addScalar("isdnAgent", StringType.INSTANCE);
            query.addScalar("objectType", StringType.INSTANCE);
            query.addScalar("shopCode", StringType.INSTANCE);
            query.addScalar("allowVisitPlan", IntegerType.INSTANCE);
            query.addScalar("totalAcumulate", IntegerType.INSTANCE);
            for (int i = 0; i < lstParam.size(); i++) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue().equals(lstParam.get(i))) {
                        query.setParameter(entry.getKey(), lstParam.get(i));
                        break;
                    }
                }
            }
            query.setParameterList("channelTypeIdsObject1", channelTypeIdsObject1);
            List<StaffDto> list = query.list();

            /**
             * B3 lay thong tin Anypay cho cac staff tu DB Anypay. *
             */
            if (list != null && !list.isEmpty() && hasAnypay) {
                list = setAnypay4Staff(anypaySession, list);
            }
            /**
             * B4 lay thong tin Planed *
             */
            List<List<String>> staffCodes = new ArrayList<>();
            List<List<Long>> staffIds = getListIdOfStaffs(list, staffCodes);
            Map<String, StaffDto> StaffDtoMap = new HashMap<>();
            for (StaffDto s : list) {
                StaffDtoMap.put(s.getCode().toUpperCase(), s);
            }
            setPlanedStatus4PVD(sessionSM, StaffDtoMap, staffIds, com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("fromDate")), com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("toDate")), "2");

            /**
             * B5 lay thong tin CheckedIn *
             */
            setCheckedInStatus4PVD(sessionSM, StaffDtoMap, staffCodes, com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("fromDate")), com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("toDate")));

            listStaffs.addAll(list);
        }

        if (!channelTypeIdsObject2.isEmpty()) {
            sb = new StringBuilder();
            List lstParam = new ArrayList();
            /**
             * B2 lay thong tin cac object tu staff. voi cac channel co
             * ObjectType = 1*
             */
            sb.append("SELECT DISTINCT sp.shop_id id, sp.shop_code code, sp.name name ");
            sb.append(", sp.channel_type_id channelTypeId, sp.address address");
            sb.append(", sp.x x, sp.y y ");
            sb.append(", sp.tel tel, 1 channelObjectType ");
            sb.append(", '-1' rank ");
            sb.append(", man.staff_id staffOwnerId, man.name staffOwnerName");
            sb.append(", man.staff_code staffOwnerCode, man.tel staffOwnerTel");
            sb.append(", aa.isdn isdnAgent");
            sb.append(", ").append(type).append(" type ");
            sb.append(", ").append(1).append(" objectType ");
            sb.append(", shp.shop_code shopCode");
            sb.append(", 1 AS allowVisitPlan");
            if (hasActiveCount) {
                sb.append(", ").append(getTotalActiveSQL(false));
            } else {
                sb.append(", 0 AS totalAcumulate ");
            }
            sb.append(" FROM shop sp LEFT JOIN staff man ON (sp.staff_owner_id=man.staff_id) ");
            sb.append(" LEFT JOIN account_agent aa ON (sp.shop_id = aa.owner_id AND aa.OWNER_TYPE = 1)");
            sb.append(" LEFT JOIN shop shp ON (sp.parent_shop_id = shp.shop_id)");
            sb.append(" WHERE 1=1 ");
            sb.append(" AND sp.parent_shop_id IN ( ");
            sb.append(" SELECT stree.shop_id FROM sm.tbl_shop_tree stree");
            sb.append(" WHERE stree.root_id = ").append(params.get("shopId")).append(")");
            sb.append("  and sp.x is not null ");
            sb.append("  and sp.y is not null ");
            sb.append("  and sp.status=1 ");

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("province")) && !"-1".equals(params.get("province"))) {
                sb.append(" and sp.province = :province ");
                lstParam.add(params.get("province"));
            }

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("district")) && !"-1".equals(params.get("district"))) {
                sb.append(" and sp.district  = :district ");
                lstParam.add(params.get("district"));
            }

            if (com.spm.viettel.msm.utils.StringUtils.validObject(params.get("precinct")) && !"-1".equals(params.get("precinct"))) {
                sb.append(" and sp.precinct  = :precinct ");
                lstParam.add(params.get("precinct"));
            }
            if (params.get("channelCode") != null) {
                sb.append(" AND UPPER(sp.shop_code) LIKE :shop_code");
                lstParam.add(params.get("channelCode"));
            }
            if (params.get("userType") != null) {
                sb.append(" AND man.POS_CODE = :userType");
                lstParam.add(params.get("userType"));
            }
            sb.append(" AND  sp.channel_type_id IN ( :channelTypeIdsObject2 ) ");
            sb.append(Constants._AND_ROWNUM__20_);
            sb.append(" ORDER BY sp.shop_code, sp.name");

            SQLQuery query = sessionSM.createSQLQuery(sb.toString());
            query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
            query.addScalar("id", LongType.INSTANCE);
            query.addScalar("type", LongType.INSTANCE);
            query.addScalar("code", StringType.INSTANCE);
            query.addScalar("name", StringType.INSTANCE);
            query.addScalar("channelTypeId", LongType.INSTANCE);
            query.addScalar("address", StringType.INSTANCE);
            query.addScalar("x", DoubleType.INSTANCE).addScalar("y", DoubleType.INSTANCE);
            query.addScalar("tel", StringType.INSTANCE);
            query.addScalar("channelObjectType", StringType.INSTANCE);
            query.addScalar("rank", StringType.INSTANCE);
            query.addScalar("staffOwnerId", LongType.INSTANCE);
            query.addScalar("staffOwnerName", StringType.INSTANCE);
            query.addScalar("staffOwnerCode", StringType.INSTANCE);
            query.addScalar("staffOwnerTel", StringType.INSTANCE);
            query.addScalar("isdnAgent", StringType.INSTANCE);
            query.addScalar("objectType", StringType.INSTANCE);
            query.addScalar("shopCode", StringType.INSTANCE);
            query.addScalar("allowVisitPlan", IntegerType.INSTANCE);
            query.addScalar("totalAcumulate", IntegerType.INSTANCE);

            query.setParameterList("channelTypeIdsObject2", channelTypeIdsObject2);
//            System.out.println("====channelTypeIdsObject2: "+ org.apache.commons.lang3.StringUtils.join(channelTypeIdsObject2, ','));
            for (int i = 0; i < lstParam.size(); i++) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue().equals(lstParam.get(i))) {
                        query.setParameter(entry.getKey(), lstParam.get(i));
                        break;
                    }
                }
            }
            List<StaffDto> list2 = query.list();

            String userType = params.get("userType") == null? null: (String) params.get("userType");
            if (org.apache.commons.lang3.StringUtils.isEmpty(userType) || Constants.POS_CODE_AF.equalsIgnoreCase(userType)){
                Long shopId = (Long) params.get("shopId");
                String channelCode = params.get("channelCode")==null? null: (String) params.get("channelCode");
//                List<StaffDto> afStaffs = getListEmployeesOfShop(sessionSM, shopId, true, userType, channelCode, 1);
//                List<Long> afStaffIds = afStaffs.stream().map(s -> s.getId()).collect(Collectors.toList());
                List<StaffDto> dfs = getListDFs(sessionSM, userType, channelCode, shopId, null);
                if (CollectionUtils.isEmpty(list2)){
                    list2 = new ArrayList<>();
                }
                list2.addAll(dfs);
            }

            /**
             * B3 lay thong tin Anypay cho cac staff tu DB Anypay. *
             */
            if (list2 != null && !list2.isEmpty() && hasAnypay) {
                list2 = setAnypay4Shop(anypaySession, list2);
            }
            /**
             * B4 lay thong tin Planed *
             */
            List<List<String>> staffCodes = new ArrayList<>();
            List<List<Long>> staffIds = getListIdOfStaffs(list2, staffCodes);
            Map<String, StaffDto> StaffDtoMap = new HashMap<>();
            for (StaffDto s : list2) {
                StaffDtoMap.put(s.getCode().toUpperCase(), s);
            }
            setPlanedStatus4PVD(sessionSM, StaffDtoMap, staffIds, com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("fromDate")), com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("toDate")), "1");

            /**
             * B5 lay thong tin CheckedIn *
             */
            setCheckedInStatus4PVD(sessionSM, StaffDtoMap, staffCodes, com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("fromDate")), com.spm.viettel.msm.utils.StringUtils.returnValueNull(params.get("toDate")));

            listStaffs.addAll(list2);

        }

        loggerFactory.debug("Total staff: " + listStaffs.size());
        return listStaffs;

    }

    public List<StaffDto> setAnypay4Shop(Session anypaySession, List<StaffDto> listStaffs) {
        List<List<Long>> staffIds = getListIdOfStaffs(listStaffs, null);

        //B1. Get Staff Agent
        Map<Long, Long> staffAgent = new HashMap<Long, Long>();
        Map<Long, Double> agentAnypay = new HashMap<Long, Double>();
        for (List<Long> sIDs : staffIds) {
            StringBuilder sbAgent = new StringBuilder();
            sbAgent.append("SELECT DISTINCT agent_id, owner_id AS staffId from anypay.sm_account_agent WHERE OWNER_TYPE ='1' AND owner_id IN ( :staffId ) ");
            SQLQuery queryAgent = anypaySession.createSQLQuery(sbAgent.toString());
            queryAgent.setParameterList("staffId", sIDs);
            List<Object[]> agents = queryAgent.list();
            setAnypay4Object(anypaySession, listStaffs, staffAgent, agentAnypay, agents);
        }
        return listStaffs;
    }

    public List<StaffDto> getListDFs(Session sessionSM, String userType, String channelCode, Long shopId, Long staffId) throws TemplateException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("userType", userType);
        params.put("channelCode", channelCode);
        params.put("shopId", shopId);
        params.put("staffId", staffId);
        List<StaffDto> list = new ArrayList<>();
        NativeQuery query = getSMSessionFactory(getClass(),"getListDFsOnMap.sftl",params,sessionSM);
        query.setParameter("shopId", shopId);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(userType)) {
            query.setParameter("userType", userType);
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(channelCode)) {
            query.setParameter("channelCode", channelCode.toUpperCase());
        }
        if (staffId != null){
            query.setParameter("staffId", staffId);
        }
        query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("type", LongType.INSTANCE);
        query.addScalar("code", StringType.INSTANCE);
        query.addScalar("name", StringType.INSTANCE);
        query.addScalar("channelTypeId", LongType.INSTANCE);
        query.addScalar("address", StringType.INSTANCE);
        query.addScalar("x", DoubleType.INSTANCE).addScalar("y", DoubleType.INSTANCE);
        query.addScalar("tel", StringType.INSTANCE);
        query.addScalar("channelObjectType", StringType.INSTANCE);
        query.addScalar("rank", StringType.INSTANCE);
        query.addScalar("staffOwnerId", LongType.INSTANCE);
        query.addScalar("staffOwnerName", StringType.INSTANCE);
        query.addScalar("staffOwnerCode", StringType.INSTANCE);
        query.addScalar("staffOwnerTel", StringType.INSTANCE);
        query.addScalar("isdnAgent", StringType.INSTANCE);
        query.addScalar("objectType", StringType.INSTANCE);
        query.addScalar("shopCode", StringType.INSTANCE);
        query.addScalar("allowVisitPlan", IntegerType.INSTANCE);
        query.addScalar("totalAcumulate", IntegerType.INSTANCE);
        list = query.list();
        return list;
    }
    private void channelTypeExtract(List<ChannelWithGroupDTO> channelTypes, List<Long> channelTypeIdsObject1, List<Long> channelTypeIdsObject2) {
        for (ChannelWithGroupDTO c : channelTypes) {
            if (c.getType() == 1 && "2".equalsIgnoreCase(c.getObjectType())) {
                // channel_type có object_type=2 thì query trong staff
                channelTypeIdsObject1.add(c.getChannelTypeId());
            } else if (c.getType() == 1 && "1".equalsIgnoreCase(c.getObjectType()) && Constants.CHANNEL_TYPE_DF != c.getChannelTypeId()) {
                // channel_type có object_type=1 thì query trong shop
                channelTypeIdsObject2.add(c.getChannelTypeId());
            }
        }
    }

    private String getTotalActiveSQL(boolean isStaff) {
        StringBuilder sb = new StringBuilder();
        if (isStaff) {
            sb.append(" LEFT JOIN (SELECT channelId, SUM (total) AS total_acumulate");
            sb.append("   FROM (SELECT 'Prepaid' AS servicetype, active_staff_id AS channelId, COUNT (*) AS total");
            sb.append("      FROM sm.collect_sub");
            sb.append("      WHERE start_datetime >= TRUNC (SYSDATE, 'mm') AND start_datetime < TRUNC (SYSDATE) + 1 AND service_type = 2");
            sb.append("      GROUP BY active_staff_id");
            sb.append("      UNION ALL ");
            sb.append("      SELECT 'Postpaid' AS servicetype, (SELECT STAFF_ID FROM STAFF WHERE STAFF_CODE = cmposdata.STAFF_CODE AND ROWNUM = 1) channelId, COUNT (*) AS total");
            sb.append("      FROM CM_POS.RP_SUB_DEV_DATA cmposdata");
            sb.append("      WHERE cmposdata.STA_DATETIME >= TRUNC  (SYSDATE,'mm') AND cmposdata.STA_DATETIME < TRUNC (SYSDATE) + 1");
            sb.append("      GROUP BY cmposdata.STAFF_CODE)");
            sb.append("   GROUP BY channelId) viewacumulate ON (sf.STAFF_ID=viewacumulate.channelId)");
        } else {
            sb.append("   (SELECT NVL(SUM(total_acumulate), 0)");
            sb.append("      FROM (SELECT channelId, SUM (total) AS total_acumulate FROM");
            sb.append("      (SELECT 'Prepaid' AS servicetype, active_shop_id AS channelId, COUNT (*) AS total FROM sm.collect_sub");
            sb.append("      WHERE start_datetime >= TRUNC (SYSDATE, 'mm') AND start_datetime < TRUNC (SYSDATE) + 1 AND service_type = 2");
            sb.append("      GROUP BY active_shop_id");
            sb.append("      UNION ALL ");
            sb.append("      SELECT 'Postpaid' AS servicetype, smstaff.SHOP_ID AS channelId, COUNT (*) AS total");
            sb.append("      FROM SM.STAFF smstaff LEFT JOIN CM_POS.RP_SUB_DEV_DATA cmposdata ON (smstaff.STAFF_CODE = cmposdata.STAFF_CODE)");
            sb.append("      WHERE cmposdata.STA_DATETIME >= TRUNC  (SYSDATE,'mm') AND cmposdata.STA_DATETIME < TRUNC (SYSDATE) + 1");
            sb.append("      GROUP BY smstaff.SHOP_ID)");
            sb.append("   GROUP BY channelId) activationCaculate");
            sb.append("   WHERE activationCaculate.channelId IN (SELECT stree.shop_id FROM sm.tbl_shop_tree stree WHERE stree.root_id = sp.shop_id)) AS totalAcumulate");
        }
        return sb.toString();
    }

    public void setCheckedInStatus4PVD(Session sessionSM, Map<String, StaffDto> StaffDtoMap, List<List<String>> staffCodes, String fromDate, String toDate) throws TemplateException, IOException {
        Session smSession = null;
        for (List<String> codes : staffCodes) {
            if (CollectionUtils.isNotEmpty(codes)) {
                NativeQuery query =  getSMSessionFactory(getClass(),"getCheckedIn4Channel.sftl",null,sessionSM);
                query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
                query.addScalar("code", StringType.INSTANCE);
                query.addScalar("checkedDate", StringType.INSTANCE);
                query.setParameterList("staffCodes", codes);
                query.setParameter("fromDate", fromDate);
                query.setParameter("toDate", toDate);
                List<StaffDto> pvdCodesPlaned = query.getResultList();
                for (StaffDto pvd : pvdCodesPlaned) {
                    StaffDto StaffDto = StaffDtoMap.get(pvd.getCode());
                    if (StaffDto != null) {
                        StaffDto.setCheckedDate(pvd.getCheckedDate());
                    }
                }
            }
        }
    }

    public void setPlanedStatus4PVD(Session sessionSM, Map<String, StaffDto> StaffDtoMap, List<List<Long>> staffIds, String fromDate, String toDate, String type) {
        for (List<Long> sIDs : staffIds) {
            if (sIDs != null && !sIDs.isEmpty()) {
                StringBuilder sbAgent = new StringBuilder();
                sbAgent.append("SELECT DISTINCT UPPER(CHANNEL_TO_CODE) AS code,  LISTAGG(TO_CHAR(DATE_PLAN, 'dd/MM/yyyy'), ', ') WITHIN GROUP (ORDER BY DATE_PLAN) AS planedDate ");
                sbAgent.append("from visit_plan_map  ");
                sbAgent.append("where OBJECT_TYPE=").append(type);
                sbAgent.append(" AND CHANNEL_TO_ID IN (:staffId)");
                sbAgent.append(" AND DATE_PLAN >= TO_DATE(:fromDate, 'dd/MM/yyyy') ");
                sbAgent.append(" AND DATE_PLAN < TO_DATE(:toDate, 'dd/MM/yyyy')");
                sbAgent.append(" GROUP BY CHANNEL_TO_CODE");
                SQLQuery query = sessionSM.createSQLQuery(sbAgent.toString());
                query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
                query.addScalar("code", StringType.INSTANCE);
                query.addScalar("planedDate", StringType.INSTANCE);
                query.setParameterList("staffId", sIDs);
                query.setParameter("fromDate", fromDate);
                query.setParameter("toDate", toDate);
                List<StaffDto> pvdCodesPlaned = query.list();
                for (StaffDto pvdCode : pvdCodesPlaned) {
                    StaffDto StaffDto = StaffDtoMap.get(pvdCode.getCode());
                    if (StaffDto != null) {
                        StaffDto.setPlanedDate(pvdCode.getPlanedDate());
                        StaffDto.setPlan(true);
                    }
                }
            }
        }
    }

    public List<StaffDto> setAnypay4Staff(Session anypaySession, List<StaffDto> listStaffs) {
        List<List<Long>> staffIds = getListIdOfStaffs(listStaffs, null);

        //B1. Get Staff Agent
        Map<Long, Long> staffAgent = new HashMap<Long, Long>();
        Map<Long, Double> agentAnypay = new HashMap<Long, Double>();
        for (List<Long> sIDs : staffIds) {
            StringBuilder sbAgent = new StringBuilder();
            sbAgent.append("SELECT DISTINCT agent_id, owner_id AS staffId from anypay.sm_account_agent WHERE OWNER_TYPE ='2' AND owner_id IN ( :staffId ) ");
            SQLQuery queryAgent = anypaySession.createSQLQuery(sbAgent.toString());
            queryAgent.setParameterList("staffId", sIDs);
            List<Object[]> agents = queryAgent.list();
            setAnypay4Object(anypaySession, listStaffs, staffAgent, agentAnypay, agents);
        }
        return listStaffs;
    }

    private void setAnypay4Object(Session anypaySession, List<StaffDto> listStaffs, Map<Long, Long> staffAgent, Map<Long, Double> agentAnypay, List<Object[]> agents) {
        if (agents != null && agents.size() > 0) {
            List<Long> agentIds = new ArrayList<Long>();
            for (Object[] agent : agents) {
                try {
                    Long agentId = Long.valueOf(agent[0].toString());
                    agentIds.add(agentId);
                    staffAgent.put(Long.valueOf(agent[1].toString()), agentId);
                } catch (Exception es) {
                }
            }
            if (agentIds.size() > 0) {
                StringBuilder sbAnypay = new StringBuilder();
                sbAnypay.append("select agent_id, (avail_balance/10000) balance  from agent_account where agent_id IN (:agentIds)  and status = 1");
                Query sqlAnypay = anypaySession.createQuery(sbAnypay.toString());
                sqlAnypay.setParameter("agentIds", agentIds);
                List<Object[]> anypays = sqlAnypay.getResultList();
                if (anypays != null && anypays.size() > 0) {
                    for (Object[] any : anypays) {
                        BigDecimal bd = (BigDecimal) any[1];
                        agentAnypay.put(Long.valueOf(any[0].toString()), bd.doubleValue());
                    }
                }
            }

        }
        //set Anypay for object
        for (StaffDto s : listStaffs) {
            Long agentId = staffAgent.get(s.getId());
            if (agentId != null) {
                Double anypay = agentAnypay.get(agentId);
                if (anypay == null) {
                    anypay = 0.0d;
                }
                s.setAnypay(anypay);
            }
        }
    }

    private List<List<Long>> getListIdOfStaffs(List<StaffDto> listStaffs, List<List<String>> listCodes) {
        List<List<Long>> staffIds = new ArrayList<>();
        List<Long> temp = new ArrayList<>();
        List<String> codesTemp = new ArrayList<>();
        for (int i = 0; i < listStaffs.size(); i++) {
            if (i > 0 && i % Constants.MAX_ROW_PARAM == 0) {
                staffIds.add(temp);
                temp = new ArrayList<>();
                if (listCodes != null) {
                    listCodes.add(codesTemp);
                    codesTemp = new ArrayList<>();
                }
            }
            temp.add(listStaffs.get(i).getId());
            if (listCodes != null) {
                codesTemp.add(listStaffs.get(i).getCode().toUpperCase());
            }
        }
        if (temp.size() < Constants.MAX_ROW_PARAM) {
            staffIds.add(temp);
            if (listCodes != null) {
                listCodes.add(codesTemp);
            }
        }
        return staffIds;
    }

    public List<StaffDto> listMapOtherObject(Session sessionSM, Map<String, Object> hashMap) {
        List<StaffDto> lst;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT sf.id id, sf.code, sf.name name ");
        sb.append(", sf.OBJECT_TYPE_ID channelTypeId, sf.address address");
        sb.append(", sf.LATITUDE x, sf.LONGITUDE y");
        sb.append(", sf.PHONE_NUMBER tel ");
        sb.append(", sf.OBJECT_TYPE type ");
        sb.append(", UPPER(shp.shop_code) shopCode ");
        sb.append(", image.IMAGE_PATH imgPath ");
        sb.append(", otype.NAME channelName ");
        sb.append(", man.staff_id staffOwnerId, man.name staffOwnerName");
        sb.append(", man.staff_code staffOwnerCode, man.tel staffOwnerTel");
//        sb.append(", CONCAT(CONCAT(aa.name,' - '),aa.tel) as isdn");
        sb.append(", ").append(2).append(" objectType ");
        sb.append(" FROM MAP_OBJECT_INFO sf LEFT JOIN  staff man ON (sf.staff_owner_id=man.staff_id) "); //, channel_type cte
//        sb.append(" LEFT JOIN staff aa ON (sf.REPRESENTATIVE = aa.staff_id)");
        sb.append(" LEFT JOIN shop shp ON (sf.shop_id = shp.shop_id)");
        sb.append(" LEFT JOIN MAP_OBJECT_INFO_IMAGE image ON (sf.id = image.MAP_OBJECT_INFO_ID AND image.TYPE=1)");

        StringBuilder sbWhere = new StringBuilder();
        sbWhere.append(" WHERE 1=1 ");
        sbWhere.append("  and sf.LATITUDE is not null ");
        sbWhere.append("  and sf.LONGITUDE is not null ");
        sbWhere.append(" AND sf.shop_id IN ( ");
        sbWhere.append(" SELECT stree.shop_id FROM sm.tbl_shop_tree stree");
        sbWhere.append(" WHERE stree.root_id = ").append(hashMap.get("shopId")).append(")");
        sbWhere.append(" AND sf.status=1 ");

        List lstParam = buildParameters(hashMap, sbWhere);

        StringBuilder sqlBuilder = new StringBuilder(sb);
        sqlBuilder.append("LEFT JOIN OTHER_OBJECT_TYPE otype ON (sf.OBJECT_TYPE_ID = otype.OTHER_CHANNEL_TYPE_ID) ");
        sqlBuilder.append(sbWhere);
        sqlBuilder.append(" AND sf.OBJECT_TYPE=2 ");

        lst = excOtherMapObjectQuery(sessionSM, lstParam, sqlBuilder,hashMap);

        sqlBuilder = new StringBuilder(sb);
        sqlBuilder.append(" LEFT JOIN CHANNEL_TYPE otype ON (sf.OBJECT_TYPE_ID = otype.CHANNEL_TYPE_ID)");
        sqlBuilder.append(sbWhere);
        sqlBuilder.append(" AND sf.OBJECT_TYPE=1 ");

        lst.addAll(excOtherMapObjectQuery(sessionSM, lstParam, sqlBuilder,hashMap));

        return lst;
    }

    private List<StaffDto> excOtherMapObjectQuery(Session sessionSM, List lstParam, StringBuilder sqlBuilder,Map<String, Object> hashMap) {
        List<StaffDto> lst;
        SQLQuery query = sessionSM.createSQLQuery(sqlBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("code", StringType.INSTANCE);
        query.addScalar("name", StringType.INSTANCE);
        query.addScalar("shopCode", StringType.INSTANCE);
        query.addScalar("channelTypeId", LongType.INSTANCE);
        query.addScalar("address", StringType.INSTANCE);
        query.addScalar("x", DoubleType.INSTANCE).addScalar("y", DoubleType.INSTANCE);
        query.addScalar("tel", StringType.INSTANCE);
        query.addScalar("type", LongType.INSTANCE);
        query.addScalar("imgPath", StringType.INSTANCE);
        query.addScalar("channelName", StringType.INSTANCE);
        query.addScalar("staffOwnerId", LongType.INSTANCE);
        query.addScalar("staffOwnerName", StringType.INSTANCE);
        query.addScalar("staffOwnerCode", StringType.INSTANCE);
        query.addScalar("staffOwnerTel", StringType.INSTANCE);
//        query.addScalar("isdn", StringType.INSTANCE);
        query.addScalar("objectType", StringType.INSTANCE);
        for (int i = 0; i < lstParam.size(); i++) {
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                if (entry.getValue().equals(lstParam.get(i))) {
                    query.setParameter(entry.getKey(), lstParam.get(i));
                    break;
                }
            }
        }
        lst = query.list();
        return lst;
    }

    private List buildParameters(Map<String, Object> hashMap, StringBuilder sb) {
        List lstParam = new ArrayList();

        if (hashMap.get("staffId") != null) {
            sb.append(" AND man.staff_id = :staffId");
            lstParam.add(hashMap.get("staffId"));
        }

        if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("province")) && !"-1".equals(hashMap.get("province"))) {
            sb.append(" and sf.province = :province");
            lstParam.add(hashMap.get("province"));
        }

        if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("district")) && !"-1".equals(hashMap.get("district"))) {
            sb.append(" and sf.district  = :district ");
            lstParam.add(hashMap.get("district"));
        }

        if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("precinct")) && !"-1".equals(hashMap.get("precinct"))) {
            sb.append(" and sf.precinct  = :precinct ");
            lstParam.add(hashMap.get("precinct"));
        }

        if (hashMap.get("channelCode") != null) {
            sb.append(" AND UPPER(sf.code) LIKE :channelCode");
            lstParam.add(hashMap.get("channelCode"));
        }

        return lstParam;
    }

    public Integer getTotalActive4Staff(Session sessionSM, Long channelId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUM (total) AS total_acumulate FROM (SELECT  COUNT (*) AS total FROM sm.collect_sub ");
        sb.append(" WHERE start_datetime >= TRUNC (SYSDATE, 'mm') AND start_datetime < TRUNC (SYSDATE) + 1 AND service_type = 2");
        sb.append(" AND active_staff_id = ").append(channelId).append(" UNION ALL ");
        sb.append("SELECT COUNT (*) AS total  FROM CM_POS.RP_SUB_DEV_DATA cmposdata, STAFF st");
        sb.append(" WHERE cmposdata.STA_DATETIME >= TRUNC  (SYSDATE,'mm') AND cmposdata.STA_DATETIME < TRUNC (SYSDATE) + 1");
        sb.append(" AND st.STAFF_CODE = cmposdata.STAFF_CODE AND st.STAFF_ID = ").append(channelId).append(" ) ");

        SQLQuery sql = sessionSM.createSQLQuery(sb.toString());
        BigDecimal res = (BigDecimal) sql.uniqueResult();
        return res.intValue();
    }

    public Integer getTotalActive4Shop(Session sessionSM, Long channelId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT NVL(SUM(TOTAL), 0) FROM (   SELECT COUNT (*) AS total FROM sm.collect_sub ");
        sb.append(" WHERE start_datetime >= TRUNC (SYSDATE, 'mm') AND start_datetime < TRUNC (SYSDATE) + 1 AND service_type = 2");
        sb.append("  AND active_shop_id IN (SELECT stree.shop_id FROM sm.tbl_shop_tree stree WHERE stree.root_id = ").append(channelId).append(" ) UNION ALL ");
        sb.append("SELECT COUNT (*) AS total  FROM SM.STAFF smstaff LEFT JOIN CM_POS.RP_SUB_DEV_DATA cmposdata ON (smstaff.STAFF_CODE = cmposdata.STAFF_CODE) ");
        sb.append(" WHERE cmposdata.STA_DATETIME >= TRUNC  (SYSDATE,'mm') AND cmposdata.STA_DATETIME < TRUNC (SYSDATE) + 1");
        sb.append("  AND smstaff.SHOP_ID IN (SELECT stree.shop_id FROM sm.tbl_shop_tree stree WHERE stree.root_id = ").append(channelId).append(" )) ");

        SQLQuery sql = sessionSM.createSQLQuery(sb.toString());
        BigDecimal res = (BigDecimal) sql.uniqueResult();
        return res.intValue();
    }

    public List<StaffDto> getListPromoter(Session sessionSM, Long ownerId, int channelType) throws TemplateException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", ownerId);
        params.put("channelType", channelType);
        List<StaffDto> list = new ArrayList<>();
        NativeQuery query = getSMSessionFactory(getClass(),"getListPromoter.sftl",params,sessionSM);
        query.setParameter("channelId",ownerId);
        query.setParameter("channelType",channelType);
        query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("code", StringType.INSTANCE);
        query.addScalar("name", StringType.INSTANCE);
        query.addScalar("rank", StringType.INSTANCE);
        query.addScalar("address", StringType.INSTANCE);
        query.addScalar("staffOwnerId", LongType.INSTANCE);
        query.addScalar("staffOwnerCode", StringType.INSTANCE);
        query.addScalar("staffOwnerName", StringType.INSTANCE);
        query.addScalar("staffOwnerTel", StringType.INSTANCE);
        query.addScalar("isdnAgent", StringType.INSTANCE);
        query.addScalar("totalAcumulate", IntegerType.INSTANCE);
        list = query.list();
        return list;
    }
}
