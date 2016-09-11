package com.modlauncher.api.minecraft;

import com.modlauncher.api.FileUtil;
import lib.mc.util.Downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MCModDownloader {

    public static void download(MCModSet modset) throws IOException {
        File modCache = new File(FileUtil.mcLauncherFolder, "mod-cache");
        modCache.mkdirs();

        for (MCMod mod : modset.getMods()) {
            File folder = new File(new File(modCache, mod.getName()), mod.getVersion());
            folder.mkdirs();
            File jarDest = new File(folder, mod.getName() + "-" + mod.getVersion() + ".jar");
            Downloader.sha1Download(new URL(mod.getURL()), jarDest, mod.getJARSHA1(), 5);
            System.out.println("Download mod " + mod.getName());
        }
    }

}
