package com.spm.viettel.msm.dto;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelCellRange;
import com.spm.viettel.msm.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanChannelSale implements Serializable {
    @ExcelCellName("#")
    private int rowIndex;

    @Column(name = "br_code")
    @ExcelCellName(value = Constants.BRANCH_CODE, mandatory = false)
    private String brCode;

    @Column(name = "bc_code")
    @ExcelCellName(value = Constants.BC_CODE, mandatory = false)
    private String bcCode;

    @Column(name = "staff_code")
    @ExcelCellName(value = Constants.STAFF_CODE, mandatory = false)
    private String staffCode;

    @ExcelCellRange(value = Constants.TARGET, mandatory = false)
    private ChannelImportTarget target;

    @ExcelCellRange(value = Constants.ACCUM, mandatory = false)
    private ChannelImportTarget accum;

    @ExcelCellRange(value = Constants.TARGET_JAN, mandatory = false)
    private ChannelImportTarget t1;
    @ExcelCellRange(value = Constants.TARGET_FEB, mandatory = false)
    private ChannelImportTarget t2;
    @ExcelCellRange(value = Constants.TARGET_MAR, mandatory = false)
    private ChannelImportTarget t3;
    @ExcelCellRange(value = Constants.TARGET_APR, mandatory = false)
    private ChannelImportTarget t4;
    @ExcelCellRange(value = Constants.TARGET_MAY, mandatory = false)
    private ChannelImportTarget t5;
    @ExcelCellRange(value = Constants.TARGET_JUN, mandatory = false)
    private ChannelImportTarget t6;
    @ExcelCellRange(value = Constants.TARGET_JUL, mandatory = false)
    private ChannelImportTarget t7;
    @ExcelCellRange(value = Constants.TARGET_AUG, mandatory = false)
    private ChannelImportTarget t8;
    @ExcelCellRange(value = Constants.TARGET_SEP, mandatory = false)
    private ChannelImportTarget t9;
    @ExcelCellRange(value = Constants.TARGET_OCT, mandatory = false)
    private ChannelImportTarget t10;
    @ExcelCellRange(value = Constants.TARGET_NOV, mandatory = false)
    private ChannelImportTarget t11;
    @ExcelCellRange(value = Constants.TARGET_DEC, mandatory = false)
    private ChannelImportTarget t12;

    private String comment;

    public boolean isNoData() {
        if (!target.isNoData()) return false;
        if (!t1.isNoData()) return false;
        if (!t2.isNoData()) return false;
        if (!t3.isNoData()) return false;
        if (!t4.isNoData()) return false;
        if (!t5.isNoData()) return false;
        if (!t6.isNoData()) return false;
        if (!t7.isNoData()) return false;
        if (!t8.isNoData()) return false;
        if (!t9.isNoData()) return false;
        if (!t10.isNoData()) return false;
        if (!t11.isNoData()) return false;
        if (!t12.isNoData()) return false;
        return true;
    }
}