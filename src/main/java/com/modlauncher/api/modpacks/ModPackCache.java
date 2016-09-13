package com.modlauncher.api.modpacks;

import com.modlauncher.api.FileUtil;
import lib.mc.util.ChecksumUtils;
import lib.mc.util.Downloader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

class ModPackCache {
    public static HashMap<String, JSONObject> modpackIndexCache = new HashMap<>();
    public static HashMap<String, JSONObject> modpackCache = new HashMap<>();

    public static void getIndexCacheIfNeeded(String url, String sha1) throws IOException {
        if (modpackIndexCache.containsKey(url)) return;
        File modpackDataCacheFolder = new File(FileUtil.mcLauncherFolder, "modpack-cache");
        URL indexURL = new URL(url);
        File modpackCache = new File(modpackDataCacheFolder, indexURL.getHost() + "-" + indexURL.getFile().replaceAll("\\..+", "").replaceAll("\\" + File.separator, "") + ".json");
        if (!modpackDataCacheFolder.exists())
            modpackDataCacheFolder.mkdir();
        if (modpackCache.exists()) {
            if (sha1 != null) {
                String calculatedSHA = ChecksumUtils.calcSHA1Sum(modpackCache);
                if (calculatedSHA.equals(sha1)) {
                    try {
                        modpackIndexCache.put(url, new JSONObject(new JSONTokener(new FileInputStream(modpackCache))));
                    } catch (JSONException ignored) {
                        modpackCache.delete();
                    }
                }
            }
        } else {
            if (sha1 != null) {
                Downloader.sha1Download(indexURL, modpackCache, sha1, 5);
            } else {
                modpackCache.delete();
                Downloader.download(indexURL, modpackCache);
            }
        }
        JSONObject response = new JSONObject(new JSONTokener(new FileInputStream(modpackCache)));
        modpackIndexCache.put(url, response);
    }

    public static JSONObject getModpackIfNeeded(String name, String dataURL) throws IOException {
        JSONObject index = modpackIndexCache.get(dataURL);
        JSONObject modpacksList = index.getJSONObject("modpacks");
        JSONObject wantedModpackLookupInfo = modpacksList.getJSONObject(name);
        URL location = new URL(wantedModpackLookupInfo.getString("url"));
        URL url = new URL(dataURL);
        File modpackDataCacheFolder = new File(new File(FileUtil.mcLauncherFolder, "modpack-cache"), url.getHost() + "-" + url.getFile().replaceAll("\\..+", "").replaceAll("\\" + File.separator, ""));
        modpackDataCacheFolder.mkdirs();
        File modpackCache = new File(modpackDataCacheFolder, name + ".json");
        Downloader.sha1Download(location, modpackCache, wantedModpackLookupInfo.getString("sha1"), 5);
        JSONObject modpackJSON = new JSONObject(new JSONTokener(new FileInputStream(modpackCache)));
        ModPackCache.modpackCache.put(dataURL + name, modpackJSON);
        return modpackJSON;
    }
}
