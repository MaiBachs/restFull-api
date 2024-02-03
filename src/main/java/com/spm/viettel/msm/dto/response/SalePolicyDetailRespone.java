package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.SaleTimeConfigDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalePolicyDetailRespone {
    private Long id;
    private String name;
    private String content;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private List<SaleTimeConfigDTO> saleTimeConfigDTOS;
}
