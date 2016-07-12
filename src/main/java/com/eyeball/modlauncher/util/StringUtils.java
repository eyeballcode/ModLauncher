package com.eyeball.modlauncher.util;

public class StringUtils {
    public static boolean isNotNullOrEmpty(String ram) {
        if (ram == null) return false;
        if (ram.trim().equals("")) return false;
        return true;
    }
}
