package com.modlauncher;

import com.modlauncher.api.modpacks.ModPack;
import com.modlauncher.api.modpacks.ModPackLookup;

import java.io.IOException;

public class ModLauncher {

    public static void main(String[] args) throws IOException {
        ModPack modPack = ModPackLookup.lookupModpackByName("EyePack");
        System.out.println(modPack.getDescription());
        modPack.getForgeVersion().download();
        modPack.downloadMods();
        modPack.setupFolders();
        modPack.setupMods();
    }

}
