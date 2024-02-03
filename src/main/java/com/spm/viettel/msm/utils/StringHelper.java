package com.spm.viettel.msm.utils;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringHelper {

    private StringHelper() {
    }

    public static String underscoreToUppercase(final String string) {
        if (!Strings.isNullOrEmpty(string)) {
            String upper = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
            return upper.isEmpty() ? "" : Character.toLowerCase(upper.charAt(0)) + upper.substring(1);
        } else {
            return "";
        }
    }

    public static String uppercaseToUnderscore(final String string) {
        return !Strings.isNullOrEmpty(string) ? CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string) : "";
    }

    public static String removeAccent(final String str, final boolean isRemoveBlankSpace, final boolean isToLowerCase) {
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        if (isRemoveBlankSpace) {
            temp = temp.replace(" ", "");
        }

        if (isToLowerCase) {
            temp = temp.toLowerCase().replace("đ", "d");
        } else {
            temp = temp.replace("đ", "d").replace("Đ", "D");
        }

        return temp;
    }
    public static String likeBuild(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return "%" + str + "%";
        } else {
            return null;
        }
    }
}
