package com.spm.viettel.msm.utils;

public final class StringUtils {

    /**
     * alphabeUpCaseNumber.
     */
    private static String alphabeUpCaseNumber = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    /**
     * INVOICE_MAX_LENGTH.
     */
    private static final int INVOICE_MAX_LENGTH = 7;
    /**
     * ZERO.
     */
    private static final String ZERO = "0";

    /**
     * Creates a new instance of StringUtils
     */
    private StringUtils() {
    }

    /**
     * method compare two string
     *
     * @param str1 String
     * @param str2 String
     * @return boolean
     */
    public static boolean compareString(String str1, String str2) {
        if (str1 == null) {
            str1 = "";
        }
        if (str2 == null) {
            str2 = "";
        }

        if (str1.equals(str2)) {
            return true;
        }
        return false;
    }

    /**
     * method convert long to string
     *
     * @param lng Long
     * @return String
     * @throws abc Exception
     */
    public static String convertFromLongToString(Long lng) throws Exception {
        try {
            return Long.toString(lng);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /*
     *  @todo: convert from Long array to String array
     */
    public static String[] convertFromLongToString(Long[] arrLong) throws Exception {
        String[] arrResult = new String[arrLong.length];
        try {
            for (int i = 0; i < arrLong.length; i++) {
                arrResult[i] = convertFromLongToString(arrLong[i]);
            }
            return arrResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /*
     *  @todo: convert from String array to Long array
     */
    public static long[] convertFromStringToLong(String[] arrStr) throws Exception {
        long[] arrResult = new long[arrStr.length];
        try {
            for (int i = 0; i < arrStr.length; i++) {
                arrResult[i] = Long.parseLong(arrStr[i]);
            }
            return arrResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /*
     *  @todo: convert from String value to Long value
     */
    public static long convertFromStringToLong(String value) throws Exception {
        try {
            return Long.parseLong(value);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }


    /*
     * Check String that containt only AlphabeUpCase and Number
     * Return True if String was valid, false if String was not valid
     */
    public static boolean checkAlphabeUpCaseNumber(String value) {
        boolean result = true;
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (alphabeUpCaseNumber.indexOf(temp) == -1) {
                result = false;
                return result;
            }
        }
        return result;
    }

    public static String standardInvoiceString(Long input) {
        String temp;
        if (input == null) {
            return "";
        }
        temp = input.toString();
        if (temp.length() <= INVOICE_MAX_LENGTH) {
            int count = INVOICE_MAX_LENGTH - temp.length();
            for (int i = 0; i < count; i++) {
                temp = ZERO + temp;
            }
        }
        return temp;
    }

    public static boolean validString(String temp) {
        if (temp == null || temp.trim().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * Ham tra ve chuoi bao gom toan so tu chuoi dua vao
     *
     * @param stringInput
     * @return string
     */
    public static String getNumericString(String stringInput) {
        try {
            StringBuilder result = new StringBuilder();
            int length = stringInput.length();
            for (int i = 0; i < length; i++) {
                Character c = stringInput.charAt(i);
                if (Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER) {
                    result.append(c);
                }
            }
            return result.toString();
        } catch (Exception e) {
        }
        return "";
    }

    public static boolean checkString(String input) {
        try {
            int j = 0;
            for (int i = input.length() - 1; i >= 0; i--) {
                if (j == 3) {
                    if (input.charAt(i) != ',') {
                        return false;
                    } else {
                        j = -1;
                    }

                } else {
                    if (j > 3) {
                        return false;
                    }
                }
                j++;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String standardizeString(String input) {
        try {
            StringBuilder tmp = new StringBuilder(input);
            StringBuilder result = new StringBuilder("");
            while (tmp.length() > 3) {
                tmp.insert(tmp.length() - 3, ",");
                result.insert(0, tmp.substring(tmp.length() - 4));
                tmp.delete(tmp.length() - 4, tmp.length());
            }
            result.insert(0, tmp);
            return result.toString();
        } catch (Exception e) {
        }
        return input;
    }

    public static String returnValueNull(Object value) {
        if (value == null) {
            return "";
        } else {
            return "" + value;
        }
    }

    public static String returnLevel(String valueLevel) {
        if (valueLevel == null) {
            return "";
        } else {
            Integer level = Integer.parseInt(valueLevel);
            String strLevel = "";
            if (level != 1) {
                for (int i = 0; i < level; i++) {
                    strLevel += "    ";
                }
            }
            return strLevel;
        }
    }

    public static boolean validObject(Object obj) {
        if (obj == null || obj.equals("")) {
            return false;
        }
        return true;
    }

    public static String commentBuild(String s1, String s2) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s1)) {
            return s2;
        } else {
            return s1 + ", " + s2;
        }
    }

    public static void main(String[] args) {
        System.out.println("abd: " + checkString("14"));
    }
}
