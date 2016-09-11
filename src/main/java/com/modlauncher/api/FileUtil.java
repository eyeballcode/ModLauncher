package com.modlauncher.api;

import lib.mc.util.Utils;

import java.io.File;

public class FileUtil {

    public static File mcLauncherFolder;

    static {
        String homeDir = System.getProperty("user.home");
        switch (Utils.OSUtils.getOS()) {
            case WINDOWS:
                mcLauncherFolder = new File(new File(new File(homeDir, "APPDATA"), "roaming"), "MCLauncher");
                break;
            case MACOSX:
                mcLauncherFolder = new File(new File(new File(homeDir, "Libraries"), "Application Support"), "MCLauncher");
                break;
            default:
                mcLauncherFolder = new File(homeDir, ".MCLauncher");
        }
        if (!mcLauncherFolder.exists())
            mcLauncherFolder.mkdirs();
    }

    public static void delete(File file) {
        if (!file.exists()) return; // LOL!
        if (file.isFile()) file.delete();
        else {
            if (file.listFiles() != null)
                for (File sub : file.listFiles()) {
                    delete(sub);
                }
            file.delete();
        }
    }

}
