package com.modlauncher.api.minecraft;

import org.json.JSONObject;

public class MCMod {

    private String name;
    private JSONObject data;

    public MCMod(JSONObject data, String name) {
        this.data = data;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isRequiredOnClient() {
        return data.getBoolean("clientRequired");
    }

    public boolean isRequiredOnServer() {
        return data.getBoolean("serverRequired");
    }

    public String getVersion() {
        return data.getString("version");
    }

    public String getURL() {
        return data.getString("url");
    }

    public String getJARSHA1() {
        return data.getString("sha1");
    }

}
