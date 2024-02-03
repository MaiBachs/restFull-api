/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spm.viettel.msm.dto;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelSheet;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author luan
 */
@Data
@ExcelSheet("Visit plan")
public class VisitPlanBean implements Serializable{
    private Long id;
    private String shopCode;
    @ExcelCellName("User")
    private String userCode;
    private String posCode;
    private Long userId;
    private Long channelId;
    @ExcelCellName("Channel")
    private String channelCode;
    private String channelRank;
    private Long bcId;
    private String bcCode;
    @ExcelCellName("Branch Code")
    private String branchCode;
    private Long branchId;
    private Date planDate;
    private Date createdDate;
    private Date checkedIn;
    private String createdDateString;
    private String checkedInString;
    @ExcelCellName("Date")
    private String planDateString;
    private String visited;
    private Integer rowIndex;
    private Integer visitTimeNeed;
    private Integer realTimeVisited;
    private Integer numberTimePlanVisit;
    private Integer isDetail;
    private Integer checkListResultStatus;
    private String checkListResultStatusString;
    private String checkListResultApproveUser;
    private String checkListResultComment;
    private Integer visitTimeQualified;
    private Integer channelToCheckListStatus;
    private String channelToCheckListStatusString;

    public void convertStatus(){
        if (getCheckedIn() != null) {
            setVisited("Sí");
            setCheckedInString(DateFormatUtils.format(getCheckedIn(), "dd/MM/yyyy HH:mm:ss"));
        } else {
            setVisited("No");
        }
        if (getCreatedDate() != null) {
            setCreatedDateString(DateFormatUtils.format(getCreatedDate(), "dd/MM/yyyy HH:mm:ss"));
        }
        setPlanDateString(DateFormatUtils.format(getPlanDate(), "dd/MM/yyyy"));

    }

}
