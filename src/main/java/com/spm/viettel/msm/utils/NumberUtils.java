package com.spm.viettel.msm.utils;

public class NumberUtils {
    public static int zeroIfNull(Integer obj) {
        if (obj == null) return 0;
        return obj;
    }

    public static int plusInt(Integer obj1, Integer obj2) {
        if (obj1 == null && obj2 == null) return 0;
        if (obj1 == null && obj2 != null) return obj2;
        if (obj1 != null && obj2 == null) return obj1;
        return obj1 + obj2;
    }
}