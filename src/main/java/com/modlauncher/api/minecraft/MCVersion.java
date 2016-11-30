package com.modlauncher.api.minecraft;

import com.modlauncher.api.FileUtil;
import lib.mc.assets.Asset;
import lib.mc.assets.AssetSet;
import lib.mc.library.LibraryObject;
import lib.mc.library.LibrarySet;
import lib.mc.util.Downloader;
import lib.mc.util.Handler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MCVersion {

    String version;

    public MCVersion(String version) {
        this.version = version;
    }

    public LibrarySet getLibrarySet() throws IOException {
        MCVersionCache.getCache(version);
        JSONArray libraries = MCVersionCache.libraryCache.get(version);
        return new LibrarySet(libraries);
    }

    public void download() throws IOException {
        MCVersionCache.getCache(version);
        JSONObject assetCache = MCVersionCache.assetsCache.get(version);
        AssetSet assetSet = new AssetSet(assetCache);
        File assetFolder = new File(FileUtil.mcLauncherFolder, "assets");
        File assetObjects = new File(assetFolder, "objects");
        assetObjects.mkdirs();
        assetSet.download(assetObjects, new Handler<Asset>() {
            @Override
            public void handle(Asset object) {
                System.out.println("Download " + new File(object.getFilepath()).getName() + " (" + object.getSHA1Sum() + ")");
            }
        });
        File librariesFolder = new File(FileUtil.mcLauncherFolder, "libraries");
        librariesFolder.mkdirs();
        LibrarySet librarySet = getLibrarySet();
        librarySet.downloadAll(librariesFolder, new Handler<LibraryObject>() {
            @Override
            public void handle(LibraryObject object) {
                System.out.println("Download " + object.parseName().getLibraryName());
            }
        });
        File versionJarFolder = new File(new File(FileUtil.mcLauncherFolder, "versions"), version);
        versionJarFolder.mkdirs();
        File versionJar = new File(versionJarFolder, version + ".jar");
        JSONObject downloadData = MCVersionCache.versionDownloadInfo.get(version).getJSONObject("client");
        String url = downloadData.getString("url");
        String sha1 = downloadData.getString("sha1");

        Downloader.sha1Download(new URL(url), versionJar, sha1, 5);
    }

    public String getVersion() {
        return version;
    }
}
