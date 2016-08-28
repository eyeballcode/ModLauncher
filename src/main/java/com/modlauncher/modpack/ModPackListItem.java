package com.modlauncher.modpack;

import org.json.JSONObject;

public class ModPackListItem {

    JSONObject data;
    String name;

    public ModPackListItem(JSONObject data, String name) {
        this.data = data;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public JSONObject getData() {
        return data;
    }
}
