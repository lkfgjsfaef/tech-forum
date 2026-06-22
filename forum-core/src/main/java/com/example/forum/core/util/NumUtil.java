package com.example.forum.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumUtil {
    public static int toInt(Object obj, int defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long toLong(Object obj, long defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static double toDouble(Object obj, double defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static BigDecimal toBigDecimal(Object obj, BigDecimal defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        try {
            return new BigDecimal(obj.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (dividend == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return dividend.divide(divisor, scale, RoundingMode.HALF_UP);
    }

    public static boolean isPositive(Integer num) {
        return num != null && num > 0;
    }

    public static boolean isPositive(Long num) {
        return num != null && num > 0;
    }

    public static boolean upZero(Long num) {
        return num != null && num > 0;
    }

    public static boolean upZero(Integer num) {
        return num != null && num > 0;
    }

    public static Integer nullOrZero(Integer num) {
        return num == null ? 0 : num;
    }

    public static Long nullOrZero(Long num) {
        return num == null ? 0L : num;
    }

    public static boolean nullOrZero(Long num, boolean returnBool) {
        return num == null || num == 0L;
    }

    public static boolean nullOrZero(Integer num, boolean returnBool) {
        return num == null || num == 0;
    }
}
