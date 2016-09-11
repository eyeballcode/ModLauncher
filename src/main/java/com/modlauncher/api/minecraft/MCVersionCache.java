package com.modlauncher.api.minecraft;

import com.modlauncher.api.FileUtil;
import lib.mc.util.Downloader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class MCVersionCache {

    public static HashMap<String, JSONObject> assetsCache = new HashMap<>();
    public static HashMap<String, JSONArray> libraryCache = new HashMap<>();
    public static JSONObject versionList;

    public static void getCache(String version) throws IOException {
        if (versionList == null) loadCache();
        File mcCacheFolder = new File(FileUtil.mcLauncherFolder, "minecraft-version-cache");
        mcCacheFolder.mkdirs();
        File mcCacheFile = new File(mcCacheFolder, version + ".json");
        String versionURL = null;
        for (Object ele_ : versionList.getJSONArray("versions")) {
            JSONObject versionJSON = (JSONObject) ele_;
            if (versionJSON.getString("id").equals(version)) {
                versionURL = versionJSON.getString("url");

            }
        }
        if (versionURL != null) {
            Downloader.download(new URL(versionURL), mcCacheFile);
        } else {
            throw new IOException("No such version");
        }
        JSONObject data = new JSONObject(new JSONTokener(new FileInputStream(mcCacheFile)));
        String assetsURL = data.getJSONObject("assetIndex").getString("url"),
                assetsURLSHA = data.getJSONObject("assetIndex").getString("sha1");
        File assetsFile = new File(mcCacheFolder, version + "-assets.json");
        Downloader.sha1Download(new URL(assetsURL), assetsFile, assetsURLSHA, 5);

        JSONObject assetsCache = new JSONObject(new JSONTokener(new FileInputStream(assetsFile)));
        JSONArray libraryCache = data.getJSONArray("libraries");
        MCVersionCache.assetsCache.put(version, assetsCache);
        MCVersionCache.libraryCache.put(version, libraryCache);
    }

    private static void loadCache() throws IOException {
        File mcCacheFolder = new File(FileUtil.mcLauncherFolder, "minecraft-version-cache");
        mcCacheFolder.mkdirs();
        File cacheFile = new File(mcCacheFolder, "cache.json");
        if (!cacheFile.exists()) {
            Downloader.download(new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json"), cacheFile);
        }
        versionList = new JSONObject(new JSONTokener(new FileInputStream(cacheFile)));
    }

}
