package com.spm.viettel.msm.dto;

import lombok.Data;
import java.util.Date;

@Data
public class BusinessResultOnMapdto {
    private Date lastUpdate;
    private Date preLastDateActive;
    private Long preActiveLastDay;
    private Long preAcumN;
    private Long preAcumN1;
    private Date postLastDateActive;
    private Long postActiveLastDay;
    private Long postAcumN;
    private Long postAcumN1;
    private Long totalAcumN;
    private Long totalAcumN1;
    private Long preActiveNoRcLastDay;
    private Long preActiveRcLastDay;
    private Long preAcumNNoRc;
    private Long preAcumNRc;
    private Long preAcumN1NoRc;
    private Long preAcumN1Rc;
}
