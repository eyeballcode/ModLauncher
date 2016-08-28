package com.modlauncher.util;

import lib.mc.util.Utils;

import java.io.File;

public class FileUtil {

    public static File getMCLauncherDir() {
        switch (Utils.OSUtils.getOS()) {
            case WINDOWS:
                return new File(System.getProperty("user.home") + "\\AppData\\Roaming\\MCLauncher");
            case MACOSX:
                return new File(System.getProperty("user.home") + "/Library/Application Support/MCLauncher");
            case LINUX:
            default:
                return new File(System.getProperty("user.home") + "/.MCLauncher");
        }
    }
}
