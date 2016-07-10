package com.eyeball.modlauncher.assets;

import org.json.JSONObject;

public abstract class Library {

    public Library(JSONObject library) {
    }

    abstract void download();

}
