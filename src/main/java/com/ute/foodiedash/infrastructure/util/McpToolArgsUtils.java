package com.ute.foodiedash.infrastructure.util;

import java.math.BigDecimal;
import java.util.Map;

public final class McpToolArgsUtils {

    private McpToolArgsUtils() {
    }

    public static String getString(Map<String, Object> args, String key) {
        Object value = args.get(key);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    public static Double getDouble(Map<String, Object> args, String key) {
        Object value = args.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(value));
    }

    public static BigDecimal getBigDecimal(Map<String, Object> args, String key) {
        Object value = args.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return new BigDecimal(String.valueOf(value));
    }
}
