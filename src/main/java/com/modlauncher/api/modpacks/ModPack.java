package com.modlauncher.api.modpacks;

import com.modlauncher.api.FileUtil;
import com.modlauncher.api.forge.ForgeVersion;
import com.modlauncher.api.minecraft.MCMod;
import com.modlauncher.api.minecraft.MCModDownloader;
import com.modlauncher.api.minecraft.MCModSet;
import com.modlauncher.api.minecraft.MCVersion;
import lib.mc.util.ChecksumUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ModPack {

    private JSONObject modpackData;
    private String name;

    public ModPack(JSONObject modpackData, String name) {
        this.modpackData = modpackData;
        this.name = name;
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
        return new MCModSet(modList);
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

    public ForgeVersion getForgeVersion() {
        return getForgeVersion(modpackData.getString("latestVersion"));
    }

    public String getDescription() {
        return modpackData.getString("description");
    }

    public void setupFolders() {
        File modpackFolder = new File(FileUtil.mcLauncherFolder, "modpack");
        File myFolder = new File(modpackFolder, getName());
        File modFolder = new File(myFolder, "mods");
        myFolder.mkdirs();
        modFolder.mkdir();
    }

    public void setupMods(String version) throws IOException {
        File modCache = new File(FileUtil.mcLauncherFolder, "mod-cache");
        File modpackFolder = new File(FileUtil.mcLauncherFolder, "modpack");
        File myFolder = new File(modpackFolder, getName());
        File modFolder = new File(myFolder, "mods");

        for (MCMod mod : getModList(version).getMods()) {
            String fileName = mod.getFilename() + "-" + mod.getVersion() + ".jar";
            File modJar = new File(new File(new File(modCache, mod.getFilename()), mod.getVersion()), fileName);
            File dest = new File(modFolder, fileName);
            if (dest.exists()) {
                if (ChecksumUtils.calcSHA1Sum(dest).equals(mod.getJARSHA1())) return;
                dest.delete();
            }
            FileUtil.copyFile(modJar, dest);
        }
    }


    public void setupMods() throws IOException {
        setupMods(getLatestVersion());
    }
}
