package com.spm.viettel.msm.dto;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

@Data
public class ChannelImportTarget {
    @ExcelCellName(value = "C1", mandatory = false)
    private String c1;
    @ExcelCellName(value = "C2", mandatory = false)
    private String c2;
    @ExcelCellName(value = "C3", mandatory = false)
    private String c3;
    @ExcelCellName(value = "C4", mandatory = false)
    private String c4;
    @ExcelCellName(value = "C5", mandatory = false)
    private String c5;
    @ExcelCellName(value = "C6", mandatory = false)
    private String c6;
    @ExcelCellName(value = "C7", mandatory = false)
    private String c7;
    @ExcelCellName(value = "C8", mandatory = false)
    private String c8;
    @ExcelCellName(value = "C9", mandatory = false)
    private String c9;
    @ExcelCellName(value = "C10", mandatory = false)
    private String c10;
    @ExcelCellName(value = "C11", mandatory = false)
    private String c11;
    @ExcelCellName(value = "C12", mandatory = false)
    private String c12;
    @ExcelCellName(value = "C13", mandatory = false)
    private String c13;
    @ExcelCellName(value = "C14", mandatory = false)
    private String c14;
    @ExcelCellName(value = "C15", mandatory = false)
    private String c15;
    @ExcelCellName(value = "C16", mandatory = false)
    private String c16;
    @ExcelCellName(value = "C17", mandatory = false)
    private String c17;
    @ExcelCellName(value = "C18", mandatory = false)
    private String c18;
    @ExcelCellName(value = "C19", mandatory = false)
    private String c19;
    @ExcelCellName(value = "C20", mandatory = false)
    private String c20;

    public boolean isNoData() {
        try {
            for (int i = 1; i <= 20; i++) {
                String value = BeanUtils.getProperty(this, String.format("c%s", i));
                if (StringUtils.isNotEmpty(value)) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}