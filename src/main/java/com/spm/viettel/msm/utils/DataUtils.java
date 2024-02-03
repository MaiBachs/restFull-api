/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spm.viettel.msm.utils;

import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtils {

    public static boolean checkValueMatchPattern(String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    public static BigDecimal timeIn24hFormatToDecimal(String time) {
        Pattern pattern = Pattern.compile(Constants.TIME_24H_PATTERN);
        if (!checkValueMatchPattern(time, pattern)) {
            return null;
        }
        BigDecimal hours = new BigDecimal(time.substring(0,2));
        BigDecimal minutes = new BigDecimal(time.substring(3,5));
        BigDecimal convertedMinutes = minutes.divide(new BigDecimal(60),1, RoundingMode.UP);
        BigDecimal convertedTime = hours.add(convertedMinutes);
        return convertedTime;
    }

    public static String validateTimeFromAndTimeTo(String timeFrom, String timeTo, MessageSource messageSource, Locale locale) {
        Pattern pattern = Pattern.compile(Constants.TIME_24H_PATTERN);
        String errorMessage = null;
        if (!DataUtils.checkValueMatchPattern(timeFrom, pattern)) {
            errorMessage = messageSource.getMessage(
                    MessageKey.COMMON_TIME_FROM_INVALID,null, locale);
        }
        if (!DataUtils.checkValueMatchPattern(timeTo, pattern)) {
            errorMessage = messageSource.getMessage(
                    MessageKey.COMMON_TIME_TO_INVALID,null, locale);
        }
        if (timeFrom.compareTo(timeTo) >= 0) {
            errorMessage = messageSource.getMessage(
                    MessageKey.COMMON_TIME_FROM_MUST_SMALLER_THAN_TIME_TO,null, locale);
        }
        return errorMessage;
    }
}
