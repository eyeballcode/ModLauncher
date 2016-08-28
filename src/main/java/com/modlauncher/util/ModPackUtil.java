package com.modlauncher.util;

import lib.mc.util.ChecksumUtils;
import lib.mc.util.Downloader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class ModPackUtil {
    public static JSONObject cacheModpack(String name) throws IOException {
        JSONObject modpackCache = new JSONObject(new JSONTokener(new FileInputStream(new File(FileUtil.getMCLauncherDir(), "modpacks.json"))));
        if (!modpackCache.getJSONObject("modpacks").has(name)) return null;
        JSONObject modpack = modpackCache.getJSONObject("modpacks").getJSONObject(name);
        File modpackCacheFolder = new File(FileUtil.getMCLauncherDir(), "modpackCache");
        modpackCacheFolder.mkdir();
        File modpackFile = new File(modpackCacheFolder, name.toLowerCase().replace(" ", "-") + ".json");
        if (modpackFile.exists()) {
            if (ChecksumUtils.calcSHA1Sum(modpackFile).equals(modpack.getString("sha1"))) {
                return new JSONObject(new JSONTokener(new FileInputStream(modpackFile)));
            }
        } else {
            Downloader.sha1Download(new URL(modpack.getString("url")), modpackFile, modpack.getString("sha1"), 5);
            return new JSONObject(new JSONTokener(new FileInputStream(modpackFile)));
        }
        return null;
    }
}
