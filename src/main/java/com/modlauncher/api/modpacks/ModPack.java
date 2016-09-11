package com.modlauncher.api.modpacks;

import com.modlauncher.api.forge.ForgeVersion;
import com.modlauncher.api.minecraft.MCModDownloader;
import com.modlauncher.api.minecraft.MCModSet;
import com.modlauncher.api.minecraft.MCVersion;
import org.json.JSONObject;

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

}
