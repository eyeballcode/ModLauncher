package com.modlauncher.api.minecraft;

import org.json.JSONObject;

import java.util.ArrayList;

public class MCModSet {

    ArrayList<MCMod> mods = new ArrayList<>();

    public MCModSet(JSONObject modlist) {
        for (String name : modlist.keySet()) {
            JSONObject mod = modlist.getJSONObject(name);
            mods.add(new MCMod(mod, name));
        }
    }

    public ArrayList<MCMod> getMods() {
        return mods;
    }
}