package com.modlauncher.api.forge;

import com.modlauncher.api.FileUtil;
import com.modlauncher.api.minecraft.MCVersion;
import lib.mc.library.LibraryObject;
import lib.mc.library.LibrarySet;
import lib.mc.util.Downloader;
import lib.mc.util.Handler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ForgeVersion {

    private String forgeVersion;

    public ForgeVersion(String forgeVersion) {
        this.forgeVersion = forgeVersion;
    }

    public String getForgeVersion() {
        return forgeVersion;
    }

    public void download() throws IOException {
        String installerDownloadURL = "http://files.minecraftforge.net/maven/net/minecraftforge/forge/" + forgeVersion + "/forge-" + forgeVersion + "-installer.jar";
        String versionJarURL = "http://files.minecraftforge.net/maven/net/minecraftforge/forge/" + forgeVersion + "/forge-" + forgeVersion + "-universal.jar";
        File librariesFolder = new File(FileUtil.mcLauncherFolder, "libraries");
        File forgeVersionCache = new File(FileUtil.mcLauncherFolder, "forge-version-cache");
        File tmpFolder = new File(FileUtil.mcLauncherFolder, "tmp");
        File forgeVersionCacheFile = new File(forgeVersionCache, forgeVersion);
        librariesFolder.mkdirs();
        forgeVersionCache.mkdirs();
        tmpFolder.mkdirs();
        JSONObject forgeData;
        if (!forgeVersionCacheFile.exists()) {
            Downloader.download(new URL(installerDownloadURL), new File(tmpFolder, forgeVersion + ".jar"));
            JarFile file = new JarFile(new File(tmpFolder, forgeVersion + ".jar"));
            ZipEntry entry = file.getEntry("install_profile.json");
            InputStream inputStream = file.getInputStream(entry);
            FileOutputStream outputStream = new FileOutputStream(forgeVersionCacheFile);
            byte[] data = new byte[(int) entry.getSize()];
            inputStream.read(data);
            outputStream.write(data);
            outputStream.close();
            inputStream.close();
        }
        FileUtil.delete(tmpFolder);
        forgeData = new JSONObject(new JSONTokener(new FileInputStream(forgeVersionCacheFile)));
        File versionFolder = new File(FileUtil.mcLauncherFolder, "versions");
        versionFolder.mkdirs();
        File versionJARFolder = new File(versionFolder, forgeVersion);
        versionJARFolder.mkdirs();
        File versionJAR = new File(versionJARFolder, forgeVersion + ".jar");
        Downloader.download(new URL(versionJarURL), versionJAR);
        MCVersion mcVersion = new MCVersion(forgeVersion.split("-")[0]);
        mcVersion.download();
        JSONArray libraries = forgeData.getJSONObject("versionInfo").getJSONArray("libraries");
        LibrarySet librarySet = new LibrarySet(libraries);
        File libraryFolder = new File(FileUtil.mcLauncherFolder, "libraries");
        ArrayList<LibraryObject> toDrop = new ArrayList<>();
        for (LibraryObject object : librarySet.getLibraries()) {
            if (object.getRawName().equals("net.minecraftforge:forge:" + forgeVersion))
                toDrop.add(object);
        }
        for (LibraryObject object : toDrop)
            librarySet.drop(object);
        librarySet.downloadAll(librariesFolder, new Handler() {
            @Override
            public void download(LibraryObject object) {
                System.out.println("Download " + object.parseName().getLibraryName() + " (Forge)");
            }
        });
    }
}
