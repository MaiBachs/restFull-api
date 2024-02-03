package com.spm.viettel.msm.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmountByMonth implements Serializable {
    private Integer t1;
    private Integer t2;
    private Integer t3;
    private Integer t4;
    private Integer t5;
    private Integer t6;
    private Integer t7;
    private Integer t8;
    private Integer t9;
    private Integer t10;
    private Integer t11;
    private Integer t12;
    private Integer total;

    public boolean hasData() {
        if (t1 != null && t1 > 0) return true;
        if (t2 != null && t2 > 0) return true;
        if (t3 != null && t3 > 0) return true;
        if (t4 != null && t4 > 0) return true;
        if (t5 != null && t5 > 0) return true;
        if (t6 != null && t6 > 0) return true;
        if (t7 != null && t7 > 0) return true;
        if (t8 != null && t8 > 0) return true;
        if (t9 != null && t9 > 0) return true;
        if (t10 != null && t10 > 0) return true;
        if (t11 != null && t11 > 0) return true;
        if (t12 != null && t12 > 0) return true;
        if (total != null && total > 0) return true;
        return false;
    }
}
