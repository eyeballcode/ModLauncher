package com.modlauncher.api.minecraft;

import com.modlauncher.api.FileUtil;
import lib.mc.assets.AssetSet;
import lib.mc.library.LibraryObject;
import lib.mc.library.LibrarySet;
import lib.mc.util.Handler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class MCVersion {

    String version;

    public MCVersion(String version) {
        this.version = version;
    }

    public void download() throws IOException {
        MCVersionCache.getCache(version);
        JSONObject assetCache = MCVersionCache.assetsCache.get(version);
        JSONArray libraries = MCVersionCache.libraryCache.get(version);
        AssetSet assetSet = new AssetSet(assetCache);
        File assetFolder = new File(FileUtil.mcLauncherFolder, "assets");
        File assetObjects = new File(assetFolder, "objects");
        assetObjects.mkdirs();
        assetSet.download(assetObjects);
        File librariesFolder = new File(FileUtil.mcLauncherFolder, "libraries");
        librariesFolder.mkdirs();
        LibrarySet librarySet = new LibrarySet(libraries);
        librarySet.downloadAll(librariesFolder, new Handler<LibraryObject>() {
            @Override
            public void handle(LibraryObject object) {
                System.out.println("Download " + object.parseName().getLibraryName());
            }
        });
    }
}
