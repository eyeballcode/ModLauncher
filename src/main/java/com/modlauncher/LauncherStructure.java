package com.modlauncher;

import com.modlauncher.logging.LauncherLogger;
import com.modlauncher.modpack.ModPack;
import com.modlauncher.util.FileUtil;
import lib.mc.util.Downloader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class LauncherStructure {

    public static final String VERSION = "1.0.0";
    public static final HashMap<String, ModPack> modpackCache = new HashMap<>();

    public static boolean downloadAssets() {
        File indexFile = new File(FileUtil.getMCLauncherDir(), "index.json");
        try {
            JSONObject versionIndexData;
            URL indexJSONURL = new URL("https://eyeballcode.github.io/index.json");
            versionIndexData = new JSONObject(Downloader.wget(indexJSONURL));
            if (versionIndexData.toString().getBytes().length != indexFile.length()) {
                FileOutputStream outputStream = new FileOutputStream(indexFile);
                outputStream.write(versionIndexData.toString().getBytes());
                outputStream.close();
            }
            LauncherLogger.logger.log("Downloaded index.json...");
            downloadAssets(versionIndexData);
            return true;
        } catch (IOException e) {
            try {
                downloadAssets(new JSONObject(new JSONTokener(new FileInputStream(indexFile))));
                return true;
            } catch (IOException ignored) {
            }
            LauncherLogger.logger.error("Could not load launcher files, therefore I cannot continue startup! Please check your internet connection.");
            return false;
        }
    }

    private static void downloadAssets(JSONObject versionIndexData) throws IOException {
        JSONObject versionData = versionIndexData.getJSONObject("versions").getJSONObject(VERSION);
        JSONObject files = versionData.getJSONObject("gamefiles");
        for (String filePath : files.keySet()) {
            JSONObject fileData = files.getJSONObject(filePath);
            downloadAsset(new URL(fileData.getString("url")), filePath, fileData.getString("sha1"));
        }
    }


    public static void downloadAsset(URL url, String output, String sha) throws IOException {
        File file = new File(FileUtil.getMCLauncherDir(), output);
        file.getParentFile().mkdirs();
        int tries = Downloader.sha1Download(url, file, sha, 5);
        if (tries > 0)
            LauncherLogger.logger.log("Took " + tries + " tries to download " + url.getFile());
        else
            LauncherLogger.logger.log("Skipped " + url.getFile() + " as SHA1 sum matched");
    }


}
