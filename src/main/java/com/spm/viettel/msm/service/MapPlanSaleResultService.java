package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.PlanSaleResultDto;
import com.spm.viettel.msm.dto.request.PlanSaleSearchRequest;
import com.spm.viettel.msm.repository.sm.MapPlanSaleResultRepository;
import com.spm.viettel.msm.repository.sm.entity.MapPlanSale;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Calendar;
import java.util.List;

@Service
public class MapPlanSaleResultService {
    @Autowired
    private MapPlanSaleResultRepository mapPlanSaleResultRepository;

    @Value("${TEMPLATE_PATH}")
    private String templateFolder;
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    public Page<PlanSaleResultDto> searchResult(PlanSaleSearchRequest request){
        PageRequest pageRequest = PageRequest.of(request.getCurrentPage() > 0 ? request.getCurrentPage() - 1 : 0, request.getPageSize());
        return mapPlanSaleResultRepository.searchResult(request.getBrCode()
                , request.getBcCode()
                , request.getStaffCode()
                , request.getIsdn()
                , request.getFromDate()
                , request.getToDate()
                , request.getMapPlanSaleId(),
                pageRequest);
    }

    public List<PlanSaleResultDto> searchResultNoPage(PlanSaleSearchRequest request){
        return mapPlanSaleResultRepository.searchResult(request.getBrCode()
                , request.getBcCode()
                , request.getStaffCode()
                , request.getIsdn()
                , request.getFromDate()
                , request.getToDate()
                , request.getMapPlanSaleId());
    }

    public String downloadSearchResult(PlanSaleSearchRequest request){
        List<PlanSaleResultDto> planSaleResultDtos = searchResultNoPage(request);
        String fileName = "plan_sale_target_search_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "plan_sale_result.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", planSaleResultDtos);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNameFull;
    }

    public String downloadSearchResultDetail(PlanSaleSearchRequest request){
        List<MapPlanSale> mapPlanSales = mapPlanSaleResultRepository.downloadSearchResultDetail(request.getBrCode()
                , request.getBcCode()
                , request.getStaffCode()
                , request.getStatus()
                , request.getFromDate()
                , request.getToDate());
        String fileName = "plan_sale_result_" + Calendar.getInstance().getTimeInMillis() + ".xls";
        String fileNameFull = fileNameFullFolder + File.separator + fileName;
        String fileTemplate = "template_sale_plan_result.xls";
        File templateFile = new File(templateFolder, fileTemplate);
        try {
            InputStream is = new FileInputStream(templateFile);
            File fileResult = new File(fileNameFull);
            OutputStream os = new FileOutputStream(fileResult);
            Context context = new Context();
            context.putVar("staffs", mapPlanSales);
            JxlsHelper.getInstance().processTemplate(is, os, context);
            return fileNameFull;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNameFull;
    }
}
