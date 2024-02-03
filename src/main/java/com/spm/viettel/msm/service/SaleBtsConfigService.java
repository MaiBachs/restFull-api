package com.spm.viettel.msm.service;

import com.spm.viettel.msm.controller.StockController;
import com.spm.viettel.msm.dto.*;
import com.spm.viettel.msm.dto.request.MapSearchRequest;
import com.spm.viettel.msm.dto.request.PlanSaleBtsConfigSearchRequest;
import com.spm.viettel.msm.dto.response.SearchSaleBtsResponse;
import com.spm.viettel.msm.repository.sm.MapHistoryRepository;
import com.spm.viettel.msm.repository.sm.MapSaleBtsConfigRepository;
import com.spm.viettel.msm.repository.sm.entity.MapHistory;
import com.spm.viettel.msm.repository.sm.entity.MapSaleBtsConfig;
import com.spm.viettel.msm.repository.sm.entity.MapSalePolicy;
import com.spm.viettel.msm.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

@Transactional(rollbackFor = { SQLException.class })
@Service
public class SaleBtsConfigService extends BaseService{
    private final Logger loggerFactory = LoggerFactory.getLogger(SaleBtsConfigService.class);
    @Value("${TEMPLATE_PATH}")
    private String templateFolder;

    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;
    @Autowired
    private MapSaleBtsConfigRepository mapSaleBtsConfigRepository;
    @Autowired
    private MapHistoryRepository mapHistoryRepository;

    public SearchSaleBtsResponse searchBtsCofig(PlanSaleBtsConfigSearchRequest request) {
        SearchSaleBtsResponse searchSaleBtsResponse = new SearchSaleBtsResponse();
        List<MapSaleBtsConfigDTO> mapSaleBtsConfigDTOList = mapSaleBtsConfigRepository.searchListBtsConfig(
                request.getBrCode(),
                request.getSalePolicyId(),
                request.getBtsCode().trim(),
                request.getStatus(),
                request.getCreatedDate(),
                request.getUpdateDate(),
                request.getCreatedUser(),
                request.getUpdateUser(),
                request.getStartDate(),
                request.getEndDate());
        if (request.isPaging()) {
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : Constants.PAGE_SIZE;
            int currentPage = request.getCurrentPage() > 0 ? request.getCurrentPage() : 1;
            Long countResults = (long) mapSaleBtsConfigDTOList.size();
            int lastPageNumber = (int) (Math.ceil(countResults / Double.valueOf(pageSize)));
            if (currentPage < 1) {
                currentPage = 1;
            } else if (currentPage > lastPageNumber) {
                currentPage = lastPageNumber;
            }
            searchSaleBtsResponse.setTotalRecord(countResults);
            searchSaleBtsResponse.setTotalPage(lastPageNumber);
            searchSaleBtsResponse.setCurrentPage(currentPage);
            int startIndex = (currentPage -1 ) * pageSize;
            if(startIndex < 0){
                startIndex = 0;
            }
            int endIndex = Math.min(startIndex + pageSize, mapSaleBtsConfigDTOList.size());
            searchSaleBtsResponse.setMapSaleBtsConfigDTOS(mapSaleBtsConfigDTOList.subList(startIndex, endIndex));
        }
        return searchSaleBtsResponse;
    }

    public MapSaleBtsConfig createNewMapSaleBtsConfig(PlanSaleBtsConfigSearchRequest searchReq, String btsCode, String brCode, MapSalePolicy salePolicy){
        MapSaleBtsConfig saleBtsConfig = new MapSaleBtsConfig();
        saleBtsConfig.setBrCode(brCode);
        saleBtsConfig.setBtsCode(btsCode);
        saleBtsConfig.setSalePolicyId(salePolicy.getId());
        saleBtsConfig.setStatus((int) Constants.ACTIVE);
        saleBtsConfig.setCreatedDate(new Date());
        if (StringUtils.isNotEmpty(searchReq.getStartDate())) {
            try {
                saleBtsConfig.setStartDate(
                        DateUtils.parseDate(searchReq.getStartDate(), Constants.DD_MM_YYYY));
            } catch (Exception ex) {
                loggerFactory.error(ex.getMessage());
            }
        } else{
            saleBtsConfig.setStartDate(new Date());
        }
        if (StringUtils.isNotEmpty(searchReq.getEndDate())) {
            try {
                saleBtsConfig.setEndDate(
                        DateUtils.parseDate(searchReq.getEndDate(), Constants.DD_MM_YYYY));
                if (saleBtsConfig.getEndDate().before(saleBtsConfig.getStartDate())) {
                }
            } catch (Exception ex) {
                loggerFactory.error(ex.getMessage());
            }
        }
        return saleBtsConfig;
    }

    public List<BTS> findListBtsByCode(String btsCode, String brCode) {
        return mapSaleBtsConfigRepository.findListBtsByCode("%" + StringUtils.trim(btsCode).toUpperCase() + "%", brCode);
    }

    public List<StaffDto> listBTSByShop(Session sessionSM,Map<String, Object> hashMap) {
        try {
            Map<String, Object> params = new HashMap<>();

//
//            StringBuilder sb = new StringBuilder();
//            sb.append("SELECT DISTINCT bts.bts_id id, bts.bts_code code, bts.bts_name name, 1 channelTypeId ");
//            sb.append(", bts.lats x, bts.longs y, bts.address, shop.shop_code shopCode");
//            sb.append(", staff.staff_id staffOwnerId, staff.name staffOwnerName");
//            sb.append(", staff.staff_code staffOwnerCode, staff.tel staffOwnerTel");
//            sb.append(", ").append(3).append(" objectType ");
//            sb.append(" FROM sm.bts_management bts LEFT JOIN sm.shop shop ON (bts.shop_id=shop.shop_id) ");
//                sb.append("  LEFT JOIN sm.staff staff ON (bts.staff_id=staff.staff_id) ");
//            sb.append(" WHERE bts.status=1 ");
//            sb.append("  and bts.lats is not null ");
//            sb.append("  and bts.longs is not null ");
//            sb.append(" AND bts.shop_id IN ( ");
//            sb.append(" SELECT stree.shop_id FROM sm.tbl_shop_tree stree");
//            sb.append(" WHERE stree.root_id = ").append(hashMap.get("shopId")).append(") ");
//            if (hashMap.get("staffId") != null) {
//                sb.append(" AND bts.staff_id = ").append(hashMap.get("staffId"));
//            }
//            if (StringUtils.validObject(hashMap.get("province")) && !"-1".equals(hashMap.get("province"))) {
//                sb.append(" and bts.province = '").append(hashMap.get("province")).append("'");
//            }
//
//            if (StringUtils.validObject(hashMap.get("district")) && !"-1".equals(hashMap.get("district"))) {
//                sb.append(" and bts.district = '").append(hashMap.get("district")).append("'");
//            }
//
//            if (StringUtils.validObject(hashMap.get("precinct")) && !"-1".equals(hashMap.get("precinct"))) {
//                sb.append(" and bts.precinct = '").append(hashMap.get("precinct")).append("'");
//            }
//
//            if (hashMap.get("channelCode") != null) {
//                sb.append(" AND UPPER(bts.bts_code) LIKE '").append(hashMap.get("channelCode")).append("'");
//            }
//
//            sb.append(" ORDER BY bts.bts_name");
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("shopId"))) {
                params.put("shopId", hashMap.get("shopId"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("staffId"))) {
                params.put("staffId", hashMap.get("staffId"));
//                query.setParameter("staffId", params.get("staffId"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("province")) && !"-1".equals(hashMap.get("province"))) {
                params.put("province", hashMap.get("province"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("district")) && !"-1".equals(hashMap.get("district"))) {
                params.put("district", hashMap.get("district"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("precinct")) && !"-1".equals(hashMap.get("precinct"))) {
                params.put("precinct", hashMap.get("precinct"));
            }
            if (hashMap.get("channelCode") != null) {
                params.put("channelCode", hashMap.get("channelCode"));
            }
            if (hashMap.get("btsRegisterFrom") != null) {
                params.put("btsRegisterFrom", hashMap.get("btsRegisterFrom"));
            }
            if (hashMap.get("btsRegisterTo") != null) {
                params.put("btsRegisterTo", hashMap.get("btsRegisterTo"));
            }
            if (hashMap.get("btsCreateFromDate") != null) {
                params.put("btsCreateFromDate", hashMap.get("btsCreateFromDate"));
            }
            if (hashMap.get("btsCreateToDate") != null) {
                params.put("btsCreateToDate", hashMap.get("btsCreateToDate"));
            }
            SQLQuery query = getSMSessionFactory(getClass(),"getBTS.sftl",params,sessionSM);
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("shopId"))) {
                query.setParameter("shopId", params.get("shopId"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("province")) && !"-1".equals(hashMap.get("province"))) {
                query.setParameter("province", params.get("province"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("district")) && !"-1".equals(hashMap.get("district"))) {
                query.setParameter("district", params.get("district"));
            }
            if (com.spm.viettel.msm.utils.StringUtils.validObject(hashMap.get("precinct")) && !"-1".equals(hashMap.get("precinct"))) {
                query.setParameter("precinct", params.get("precinct"));
            }
            if (hashMap.get("channelCode") != null) {
                query.setParameter("channelCode", params.get("channelCode"));
            }
            if (hashMap.get("btsRegisterFrom") != null) {
                query.setParameter("btsRegisterFrom", params.get("btsRegisterFrom"));
            }
            if (hashMap.get("btsRegisterTo") != null) {
                query.setParameter("btsRegisterTo", params.get("btsRegisterTo"));
            }
            if (hashMap.get("btsCreateFromDate") != null) {
                query.setParameter("btsCreateFromDate", params.get("btsCreateFromDate"));
            }
            if (hashMap.get("btsCreateToDate") != null) {
                query.setParameter("btsCreateToDate", params.get("btsCreateToDate"));
            }
            query.setResultTransformer(Transformers.aliasToBean(StaffDto.class));
            query.addScalar("id", LongType.INSTANCE);
            query.addScalar("code", StringType.INSTANCE);
            query.addScalar("name", StringType.INSTANCE);
            query.addScalar("channelTypeId", LongType.INSTANCE);
            query.addScalar("address", StringType.INSTANCE);
            query.addScalar("shopCode", StringType.INSTANCE);
            query.addScalar("x", DoubleType.INSTANCE).addScalar("y", DoubleType.INSTANCE);
            query.addScalar("staffOwnerId", LongType.INSTANCE);
            query.addScalar("staffOwnerName", StringType.INSTANCE);
            query.addScalar("staffOwnerCode", StringType.INSTANCE);
            query.addScalar("staffOwnerTel", StringType.INSTANCE);
            query.addScalar("objectType", StringType.INSTANCE);
            query.addScalar("numRegister", LongType.INSTANCE);
            query.addScalar("createDate", StringType.INSTANCE);
            List<StaffDto> lst = query.getResultList();
            return lst;
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }

        return new ArrayList<>();
    }



    public MapSaleBtsConfig findByBrCodeAndBtsCodeAndSalePolicyId(String brCode, String btsCode, Long salePolicyId){
        return mapSaleBtsConfigRepository.findByBrCodeAndBtsCodeAndSalePolicyId(brCode, btsCode, salePolicyId);
    }

    public MapSaleBtsConfig save(MapSaleBtsConfig mapSaleBtsConfig){
        return mapSaleBtsConfigRepository.save(mapSaleBtsConfig);
    }

    public boolean hasSaleConfigAvailable(String brCode, String btsCode) {
        BigDecimal count = mapSaleBtsConfigRepository.hasSaleConfigAvailable(brCode, btsCode);
        if (count.intValue() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void settingPolicyNormalForBTS(MapSaleBtsConfig currentConfig) {
        MapSaleBtsConfig btsNormal;
        String brCode = currentConfig.getBrCode();
        String btsCode = currentConfig.getBtsCode();
        Date currentDate = new Date();
        if (currentConfig.getSalePolicyId() == Constants.SALE_POLICY_FOR_BTS_NORMAL && currentConfig.getStatus() == Constants.ACTIVE_INT) {
            PlanSaleBtsConfigSearchRequest request = new PlanSaleBtsConfigSearchRequest();
            request.setBtsCode(btsCode);
            request.setBrCode(brCode);
            request.setStatus(Constants.ACTIVE_INT);
            request.setPaging(false);
            List<MapSaleBtsConfigDTO> saleBtsConfigs = searchBtsCofig(request).getMapSaleBtsConfigDTOS();
            saleBtsConfigs.forEach(s -> {
                if (s.getStartDate().compareTo(currentDate) < 0
                        && (s.getEndDate() == null || s.getEndDate().compareTo(currentDate) > 0)
                        && s.getSalePolicyId() != Constants.SALE_POLICY_FOR_BTS_NORMAL) {
                    MapSaleBtsConfig activeConfig = mapSaleBtsConfigRepository.findById(s.getId()).orElse(null);
                    activeConfig.setStatus((int) Constants.INACTIVE);
                    mapSaleBtsConfigRepository.save(activeConfig);
                }
            });
        } else {
            btsNormal = findByBrCodeAndBtsCodeAndSalePolicyId(brCode, btsCode, Constants.SALE_POLICY_FOR_BTS_NORMAL);
            if (btsNormal == null) {
                btsNormal = new MapSaleBtsConfig();
                btsNormal.setBrCode(brCode);
                btsNormal.setBtsCode(btsCode);
                btsNormal.setSalePolicyId(Constants.SALE_POLICY_FOR_BTS_NORMAL);
                btsNormal.setStartDate(new Date());
            }
            if (hasSaleConfigAvailable(brCode, btsCode)) {
                btsNormal.setStatus((int) Constants.INACTIVE);
            } else {
                btsNormal.setStatus(Constants.ACTIVE_INT);
            }
            try {
                mapSaleBtsConfigRepository.save(btsNormal);
            } catch (Exception e) {
                loggerFactory.error(e.getMessage());
            }
        }
    }

    public MapSaleBtsConfig findById(Long id){
        return mapSaleBtsConfigRepository.findById(id).orElse(null);
    }

    public void saveHistoryBts(MapSaleBtsConfig btsOld, MapSaleBtsConfig btsNew) {
        Javers javers = JaversBuilder.javers()
                .withListCompareAlgorithm(LEVENSHTEIN_DISTANCE)
                .build();
        Diff diff = javers.compare(btsOld, btsNew);

        ArrayList<Change> list = new ArrayList<>();
        list.addAll(diff.getChanges());
        ArrayList<String> listChanged = new ArrayList<>();
        listChanged.add(diff.changesSummary());
        for (int i = 0; i < list.size(); i++) {
            ValueChange change = diff.getChangesByType(ValueChange.class).get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            if (change.getPropertyName().equals("startDate")) {
                listChanged.add("'" + change.getPropertyName() + "'" + " changed: " + sdf.format(change.getLeft()) + " -> " + sdf.format(change.getRight()));
            } else if (change.getPropertyName().equals("endDate") && change.getRight() == null) {
                listChanged.add("'" + change.getPropertyName() + "'" + " changed: " + sdf.format(change.getLeft()) + " -> null");
            } else if (change.getPropertyName().equals("endDate") && change.getLeft() == null) {
                listChanged.add("'" + change.getPropertyName() + "'" + " changed: " +  " null -> " + sdf.format(change.getRight()));
            } else {
                listChanged.add("'" + change.getPropertyName() + "'" + " changed: " + change.getLeft() + " -> " + change.getRight());
            }
        }
        MapHistory mapHistory;
        mapHistory = new MapHistory();
        mapHistory.setTableName("MAP_SALE_BTS_CONFIG");
        mapHistory.setDiff(listChanged.toString());
        mapHistory.setCreatedDate(new Date());
        mapHistory.setCreatedUser(btsOld.getCreatedUser());
        mapHistory.setObjectId(btsOld.getId());

        try {
            mapHistoryRepository.save(mapHistory);
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
    }

    public String buildImportResultFile(List<SaleBtsConfigDto> btsConfigs) {
        String templateFolder = this.templateFolder;
        String fileName = "import_bts_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "Template_import_bts_result.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", btsConfigs);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String buildSearchBTSResultFile(PlanSaleBtsConfigSearchRequest request) {
        List<MapSaleBtsConfigDTO> mapSaleBtsConfigDTOList = mapSaleBtsConfigRepository.searchListBtsConfig(
                request.getBrCode(),
                request.getSalePolicyId(),
                request.getBtsCode(),
                request.getStatus(),
                request.getCreatedDate(),
                request.getUpdateDate(),
                request.getCreatedUser(),
                request.getUpdateUser(),
                request.getStartDate(),
                request.getEndDate());
        String templateFolder = this.templateFolder;
        String fileName = "Template_search_bts_export.xls";
        String fileNameFull = this.fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "Template_search_bts_export.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", mapSaleBtsConfigDTOList);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  List<SaleBtsSummaryDto> listSaleBtsSummary(Map<String, Object> hashMap) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("btsCodeList", hashMap.get("btsCodeList"));
            if (hashMap.get("btsSalePolicyId") != null) {
                params.put("btsSalePolicyId", hashMap.get("btsSalePolicyId"));
            }
            if (hashMap.get("btsSaleFrom") != null) {
                params.put("btsSaleFrom", hashMap.get("btsSaleFrom"));
            }
            if (hashMap.get("btsSaleTo") != null) {
                params.put("btsSaleTo", hashMap.get("btsSaleTo"));
            }
            List<SaleBtsSummaryDto> lst = mapSaleBtsConfigRepository.getListSaleBtsSummary();
            return lst;
        } catch (Exception e) {
            loggerFactory.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}
