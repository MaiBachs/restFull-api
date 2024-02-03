package com.spm.viettel.msm.dto;

import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;

@Data
public class MapSaleBtsConfigDTO {
    private Long id;
    private Long salePolicyId;
    private String salePolicyName;
    private String btsCode;
    private Long status;
    private Date startDate;
    private Date endDate;
    private String brCode;
    private Date createdDate;
    private Date updateDate;
    private String createdUser;
    private String updateUser;
    private String sStartDate;
    private String sEndDate;
    private String statusString;
    private String sCreatedDate;
    private String sUpdateDate;
    private boolean isPaging = true;
    private int pageSize = 10;
    private Integer currentPage;

}
