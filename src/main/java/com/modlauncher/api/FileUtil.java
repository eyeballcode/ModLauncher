package com.modlauncher.api;

import lib.mc.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

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

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

}
