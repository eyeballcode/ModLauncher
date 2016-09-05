package com.modlauncher.util;

import com.modlauncher.logging.LauncherLogger;
import com.modlauncher.modpack.ModPack;
import lib.mc.assets.AssetSet;
import lib.mc.except.NoSuchVersionException;
import lib.mc.util.ChecksumUtils;
import lib.mc.util.Downloader;
import lib.mc.versions.GameVersionDownloader;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
        }
        Downloader.sha1Download(new URL(modpack.getString("url")), modpackFile, modpack.getString("sha1"), 5);
        return new JSONObject(new JSONTokener(new FileInputStream(modpackFile)));
    }

    public static void setActiveModpack(ModPack activeModpack) throws IOException {
        File modpackFile = new File(FileUtil.getMCLauncherDir(), "modpacks-data.json");
        FileWriter fw = new FileWriter(modpackFile);
        fw.write(new JSONObject().put("active", activeModpack.getName()).toString().toCharArray());
        fw.close();
    }

    public static void downloadModpack(ModPack activeModpack, JProgressBar progressBar) throws NoSuchVersionException, IOException {
        JSONObject data = activeModpack.getData();
        System.out.println(data);

        File versionCacheFolder =new File(FileUtil.getMCLauncherDir(), "versionCache");
        versionCacheFolder.mkdirs();
        String mcVersion = data.getJSONObject("versions").getJSONObject(data.getString("latestVersion")).getString("minecraftVersion");
        GameVersionDownloader.downloadVersion(GameVersionDownloader.forVersion(mcVersion), new File(versionCacheFolder, mcVersion + ".json"));
        LauncherLogger.logger.log("Downloading assets for " + activeModpack.getName() + " (MC " + mcVersion + ")");
    }

    public static ModPack getActiveModpack() throws IOException {
        File modpackFile = new File(FileUtil.getMCLauncherDir(), "modpacks-data.json");
        if (!modpackFile.exists()) {
            JSONObject modpackCache = new JSONObject(new JSONTokener(new FileInputStream(new File(FileUtil.getMCLauncherDir(), "modpacks.json"))));
            String featured = modpackCache.getString("featuredModpack");
            FileWriter fw = new FileWriter(modpackFile);
            fw.write(new JSONObject().put("active", featured).toString().toCharArray());
            fw.close();
            return new ModPack(featured, cacheModpack(featured));
        }
        JSONObject modpackCache = new JSONObject(new JSONTokener(new FileInputStream(modpackFile)));
        return new ModPack(modpackCache.getString("active"), cacheModpack(modpackCache.getString("active")));
    }
}
