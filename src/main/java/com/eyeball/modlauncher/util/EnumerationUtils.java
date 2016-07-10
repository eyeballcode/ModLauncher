package com.eyeball.modlauncher.util;

import java.util.Enumeration;

public class EnumerationUtils {


    public static <T> int count(Enumeration<T> entries) {
        Enumeration<T> enumeration = entries;
        int i = 0;
        while (enumeration.hasMoreElements()) {
            i++;
            enumeration.nextElement();
        }
        return i;
    }
}
