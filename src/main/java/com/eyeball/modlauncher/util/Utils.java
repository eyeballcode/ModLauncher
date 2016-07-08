package com.eyeball.modlauncher.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Utils {
    public static String noise() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
}
