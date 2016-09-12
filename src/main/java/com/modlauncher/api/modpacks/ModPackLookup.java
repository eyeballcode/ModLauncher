package com.modlauncher.api.modpacks;

import org.json.JSONObject;

import java.io.IOException;

public class ModPackLookup {

    public static ModPack lookupModpackByName(String name) throws IOException {
        return lookupModpackByName(name, "https://eyeballcode.github.io/modpacks.json");
    }

    public static ModPack lookupModpackByName(String name, String dataURL) throws IOException {
        return lookupModpackByName(name, dataURL, null);
    }

    public static ModPack lookupModpackByName(String name, String dataURL, String sha1) throws IOException {
        ModPackCache.getIndexCacheIfNeeded(dataURL, sha1);
        ModPackCache.getModpackIfNeeded(name, dataURL);
        JSONObject data = ModPackCache.modpackCache.get(dataURL + name);
        return new ModPack(data, name, dataURL);
    }

}
