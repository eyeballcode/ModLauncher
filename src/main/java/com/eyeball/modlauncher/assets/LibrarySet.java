package com.eyeball.modlauncher.assets;

import com.eyeball.modlauncher.file.FileHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by eyeballcode on 10/7/16.
 */
public class LibrarySet {

    JSONArray librariesList;
    JSONObject versionManifest;

    public LibrarySet(JSONArray librariesList, JSONObject versionManifest) {
        this.librariesList = librariesList;
        this.versionManifest = versionManifest;
    }

    private static void downloadLibrary(JSONObject library) {
        JARLibrary lib = new JARLibrary(library);
        lib.download();
    }

    private static void downloadNative(JSONObject library, String mcVersion) {
        NativeLibrary nativeLib = new NativeLibrary(library, mcVersion);
        nativeLib.download();
    }

    public void download() {
        for (Object _ : librariesList) {
            JSONObject library = (JSONObject) _;
            if (library.has("natives")) {
                downloadNative(library, versionManifest.getString("id"));
            } else {
                downloadLibrary(library);
            }
        }
    }

    public String classpathFormat(JSONArray drop_) {
        ArrayList<String> drop = new ArrayList<>();
        for (Object _ : drop_) drop.add((String) _);
        System.out.println(drop);
        StringBuilder builder = new StringBuilder();
        for (Object _ : librariesList) {
            JSONObject library = (JSONObject) _;
            if (!library.has("natives")) {
                JARLibrary lib = new JARLibrary(library);
                if (drop.contains(lib.library.getString("name")))
                    continue;
                builder.append(lib.toClassPath()).append(":");
            }
        }
        return builder.toString().substring(0, builder.length() - 1);
    }
}
