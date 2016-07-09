package com.eyeball.modlauncher.assets;

import com.eyeball.modlauncher.file.FileHelper;
import com.eyeball.modlauncher.util.DownloadUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class AssetsHelper {

    public static void downloadAssetsForMCVersion(String mcVersion) {
        String url = findMCVersionJSON(mcVersion);
        if (url == null) {
            System.err.println("Could not find MC version " + mcVersion);
            System.err.println("Check with modpack author about this...");
            System.exit(3);
        }
        File versionsCache = new File(new File(FileHelper.getMCLDir(), "versionsCache"), mcVersion + ".json");
        DownloadUtil.downloadFile(url, versionsCache);
        JSONObject versionManifest = new JSONObject(FileHelper.read(versionsCache));

        JSONObject clientLibraryDownloadInfo = versionManifest.getJSONObject("downloads").getJSONObject("client");
        DownloadUtil.hashedDownload(clientLibraryDownloadInfo.getString("sha1"), clientLibraryDownloadInfo.getString("url"), new File(new File(new File(FileHelper.getMCDir(), "versions"), mcVersion), mcVersion + ".jar"), 3);

        String assetsURL = versionManifest.getJSONObject("assetIndex").getString("url"),
                assetsURLHash = versionManifest.getJSONObject("assetIndex").getString("sha1");
        File assetsManifestFile = new File(new File(FileHelper.getMCLDir(), "versionsCache"), mcVersion + "_assets.json");
        DownloadUtil.hashedDownload(assetsURLHash, assetsURL, assetsManifestFile, 5);
        JSONObject assetsManifest = new JSONObject(FileHelper.read(assetsManifestFile));
        downloadAssets(assetsManifest);
        downloadLibraries(versionManifest);
    }

    //[--userProperties, {}, --assetsDir, /home/eyeballcode/.gradle/caches/minecraft/assets, --assetIndex, 1.7.10, --accessToken, {REDACTED}, --version, 1.7.10, --tweakClass, cpw.mods.fml.common.launcher.FMLTweaker, --tweakClass, net.minecraftforge.gradle.tweakers.CoremodTweaker]

    private static void downloadLibraries(JSONObject versionManifest) {
        JSONArray librariesList = versionManifest.getJSONArray("libraries");
        for (Object _ : librariesList) {
            JSONObject library = (JSONObject) _;
            if (library.has("natives")) {
                downloadNative(library);
            } else {
                JSONObject downloadInfo = library.getJSONObject("downloads").getJSONObject("artifact");
                File output = new File(new File(FileHelper.getMCDir(), "libraries"), downloadInfo.getString("path"));
                output.getParentFile().mkdirs();
                DownloadUtil.hashedDownload(downloadInfo.getString("sha1"), downloadInfo.getString("url"), output, 5);
            }
        }
    }

    private static void downloadNative(JSONObject library) {

    }

    private static void downloadAssets(JSONObject assetsManifest) {
        JSONObject objects = assetsManifest.getJSONObject("objects");
        File assetsDir = new File(new File(FileHelper.getMCDir(), "assets"), "objects");
        for (String filePath : objects.keySet()) {
            JSONObject fileDesc = objects.getJSONObject(filePath);
            String hash = fileDesc.getString("hash");
            String url = "http://resources.download.minecraft.net/" + hash.substring(0, 2) + "/" + hash;
            File outputContainer = new File(assetsDir, hash.substring(0, 2));
            outputContainer.mkdirs();
            DownloadUtil.hashedDownload(hash, url, new File(outputContainer, hash), 10);
        }
    }

    private static String findMCVersionJSON(String mcVersion) {
        String versionManifestString = DownloadUtil.wget("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        JSONObject versionManifest = new JSONObject(versionManifestString);
        JSONArray versions = versionManifest.getJSONArray("versions");
        for (Object _ : versions) {
            JSONObject version = (JSONObject) _;
            if (version.getString("id").equals(mcVersion))
                return version.getString("url");
        }
        return null;
    }
}
