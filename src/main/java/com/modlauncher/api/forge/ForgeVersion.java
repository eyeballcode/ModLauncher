package com.modlauncher.api.forge;

import com.modlauncher.api.FileUtil;
import lib.mc.library.DefaultMCLibraryObject;
import lib.mc.library.LibraryObjectInfo;
import lib.mc.util.Downloader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;
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
        String forgeVersionMFURL = "http://eyeballcode.github.io/Forge-Libraries/forge-libraries.json";

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
        File versionFolder = new File(FileUtil.mcLauncherFolder, "versions");
        versionFolder.mkdirs();
        File versionJARFolder = new File(versionFolder, forgeVersion);
        versionJARFolder.mkdirs();
        File versionJAR = new File(versionJARFolder, forgeVersion + ".jar");
        Downloader.download(new URL(versionJarURL), versionJAR);

        File forgeVersionData = new File(FileUtil.mcLauncherFolder, "forge-libraries.json");
        Downloader.download(new URL(forgeVersionMFURL), forgeVersionData);

        JSONObject forgeVersionDataJSON = new JSONObject(new JSONTokener(new FileInputStream(forgeVersionData))).getJSONObject(forgeVersion);
        for (String libFileName : forgeVersionDataJSON.keySet()) {
            JSONObject libData = forgeVersionDataJSON.getJSONObject(libFileName);
            String libRawName = libData.getString("name");
            String sha1 = libData.getString("sha1");
            String url = libData.getString("url");
            LibraryObjectInfo info = new DefaultMCLibraryObject(libRawName, sha1).parseName();
            File libFolder = new File(new File(new File(librariesFolder, info.getPackageName().replaceAll("\\.", File.separator)), info.getLibraryName()), info.getVersion());
            libFolder.mkdirs();
            File libraryFile = new File(libFolder, libFileName);
            Downloader.sha1Download(new URL(url), libraryFile, sha1, 5);
            System.out.println("Download " + info.getLibraryName() + " (Forge)");
        }
    }
}
