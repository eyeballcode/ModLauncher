package com.modlauncher.api.util;

import lib.mc.util.Utils;

public class OSHelper {

    public static String getOSNativeLibExt() {
        switch (Utils.OSUtils.getOS()) {
            case WINDOWS: return "dll";
            case MACOSX: return "dynlib";
            default: return "so";
        }
    }
}
