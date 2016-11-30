package com.modlauncher.api.modpacks;

import com.modlauncher.api.FileUtil;
import com.modlauncher.api.forge.ForgeVersion;
import com.modlauncher.api.minecraft.MCMod;
import com.modlauncher.api.minecraft.MCModDownloader;
import com.modlauncher.api.minecraft.MCModSet;
import com.modlauncher.api.minecraft.MCVersion;
import lib.mc.util.Downloader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

public class ModPack {

    private JSONObject modpackData;
    private String name, dataURL;

    public ModPack(JSONObject modpackData, String name, String dataURL) {
        this.modpackData = modpackData;
        this.name = name;
        this.dataURL = dataURL;
    }

    public String getLatestVersion() {
        return modpackData.getString("latestVersion");
    }

    public Set<String> getVersions() {
        return modpackData.getJSONObject("versions").keySet();
    }

    public String getName() {
        return name;
    }

    public ForgeVersion getForgeVersion(String versionID) {
        if (modpackData.getJSONObject("versions").getJSONObject(versionID).getString("forgeVersion").equals("none")) {
            return null;
        }
        return new ForgeVersion(modpackData.getJSONObject("versions").getJSONObject(versionID).getString("forgeVersion"));
    }

    public MCModSet getModList(String version) {
        JSONObject modList = modpackData.getJSONObject("versions").getJSONObject(version).getJSONObject("modlist");
        JSONObject extraMods = modpackData.getJSONObject("versions").getJSONObject(version).getJSONObject("specialMods");
        return new MCModSet(modList, extraMods);
    }

    public void downloadMods() throws IOException {
        MCModDownloader.download(getModList());
    }

    public MCModSet getModList() {
        return getModList(getLatestVersion());
    }


    public MCVersion getMCVersion(String versionID) {
        return new MCVersion(modpackData.getJSONObject("versions").getJSONObject(versionID).getString("minecraftVersion"));
    }

    public MCVersion getMCVersion() {
        return getMCVersion(getLatestVersion());
    }

    public ForgeVersion getForgeVersion() {
        return getForgeVersion(modpackData.getString("latestVersion"));
    }

    public String getDescription() {
        return modpackData.getString("description");
    }

    public void setupFolders(String version) {
        File modpackFolder = new File(FileUtil.mcLauncherFolder, "modpack");
        File myFolder = new File(new File(modpackFolder, getName()), version);
        File modFolder = new File(myFolder, "mods");
        File configFolder = new File(myFolder, "config");
        myFolder.mkdirs();
        modFolder.mkdir();
        configFolder.mkdir();
    }

    public void setupFolders() {
        setupFolders(getLatestVersion());
    }

    public void setupMods(String version) throws IOException {
        File modCache = new File(FileUtil.mcLauncherFolder, "mod-cache");
        File modpackFolder = new File(FileUtil.mcLauncherFolder, "modpack");
        File myFolder = new File(new File(modpackFolder, getName()), version);
        File modFolder = new File(myFolder, "mods");

        FileUtil.delete(modFolder);
        modFolder.mkdir();

        for (MCMod mod : getModList(version).getMods()) {
            String fileName = mod.getFilename() + "-" + mod.getVersion() + ".jar";
            File modJar = new File(new File(new File(modCache, mod.getFilename()), mod.getVersion()), fileName);
            modJar.getParentFile().mkdirs();
            File dest = new File(new File(modFolder, mod.getDownloadLoc()), fileName);
            dest.getParentFile().mkdirs();
            FileUtil.copyFile(modJar, dest);
        }
    }

    public void setupMods() throws IOException {
        setupMods(getLatestVersion());
    }

    public String getConfigURL() {
        return getConfigURL(getLatestVersion());
    }

    private String getConfigURL(String version) {
        return modpackData.getJSONObject("versions").getJSONObject(version).getString("configIndex");
    }


    public String getConfigSHA1() {
        return getConfigSHA1(getLatestVersion());
    }

    private String getConfigSHA1(String version) {
        return modpackData.getJSONObject("versions").getJSONObject(version).getString("configIndexSHA1");
    }

    public void grabConfigs(String version) throws IOException {
        File configDataCache = new File(FileUtil.mcLauncherFolder, "config-cache");
        configDataCache.mkdirs();
        URL dataURL = new URL(this.dataURL);
        File myHostFolder = new File(configDataCache, dataURL.getHost() + "-" + dataURL.getFile().replaceAll("\\..+", "").replaceAll("\\" + File.separator, ""));
        myHostFolder.mkdirs();
        File myConfigCache = new File(myHostFolder, name + "-" + version + "-config.json");
        Downloader.sha1Download(new URL(getConfigURL(version)), myConfigCache, getConfigSHA1(version), 5);

        JSONObject configData = new JSONObject(new JSONTokener(new FileInputStream(myConfigCache)));
        JSONObject files = configData.getJSONObject("files");
        JSONArray folders = configData.getJSONArray("folders");

        File modpackFolder = new File(FileUtil.mcLauncherFolder, "modpack");
        File myFolder = new File(new File(modpackFolder, getName()), version);
        File configFolder = new File(myFolder, "config");

        for (Object folderName : folders) {
            new File(configFolder, (String) folderName).mkdirs();
        }

        for (String filePath : files.keySet()) {
            File file = new File(configFolder, filePath.replaceAll("/", File.separator));
            JSONObject data = files.getJSONObject(filePath);
            Downloader.sha1Download(new URL(data.getString("url")), file, data.getString("sha1"), 5);
            System.out.println("Grab config " + filePath);
        }
    }

    public void grabConfigs() throws IOException {
        grabConfigs(getLatestVersion());
    }
}
