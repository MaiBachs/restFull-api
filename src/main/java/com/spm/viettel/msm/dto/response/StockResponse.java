package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.PrepareDto;
import com.spm.viettel.msm.dto.SaleResultDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class StockResponse {
    private List<SaleResultDto> saleResults = new ArrayList<>();
    private Date lastUpdateDateTime;
    private List<PrepareDto> prepaidDetailList;
}
