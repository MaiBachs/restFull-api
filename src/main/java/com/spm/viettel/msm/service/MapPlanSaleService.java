package com.spm.viettel.msm.service;

import com.spm.viettel.msm.dto.MapPlanSaleDto;
import com.spm.viettel.msm.dto.request.PlanSaleSearchRequest;
import com.spm.viettel.msm.repository.sm.MapPlanSaleRepository;
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
public class MapPlanSaleService {
    @Autowired
    private MapPlanSaleRepository mapPlanSaleRepository;

    @Value("${TEMPLATE_PATH}")
    private String templateFolder;
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;

    public Page<MapPlanSaleDto> mapSearchPlan(PlanSaleSearchRequest request){
        PageRequest pageRequest = PageRequest.of(request.getCurrentPage() > 0 ? request.getCurrentPage() - 1 : 0, request.getPageSize());
        return mapPlanSaleRepository.mapSearchPlan(request.getBrCode()
                , request.getBcCode()
                , request.getStaffCode()
                , request.getChannelCode()
                , request.getStatus()
                , request.getFromDate()
                , request.getToDate()
                , pageRequest);
    }
}
