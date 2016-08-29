package com.modlauncher.modpack;

import org.json.JSONObject;

public class ModPackListItem {

    private JSONObject data;
    private String name;

    public ModPackListItem(JSONObject data, String name) {
        this.data = data;
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public JSONObject getData() {
        return data;
    }
}
