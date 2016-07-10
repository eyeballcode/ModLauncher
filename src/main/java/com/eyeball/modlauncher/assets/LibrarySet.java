package com.eyeball.modlauncher.assets;

import com.eyeball.modlauncher.file.FileHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

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

    public String classpathFormat() {
        StringBuilder builder = new StringBuilder();
        for (Object _ : librariesList) {
            JSONObject library = (JSONObject) _;
            if (!library.has("natives")) {
                JARLibrary lib = new JARLibrary(library);
                builder.append(lib.toClassPath()).append(":");
            }
        }
        String nativesPath = new File(new File(new File(FileHelper.getMCDir(), "versions"), versionManifest.getString("id")), "natives").getAbsolutePath();
        for (File file : new File(nativesPath).listFiles()) {
            builder.append(file.getAbsolutePath()).append(":");
        }
        return builder.toString().substring(0, builder.length() - 1);
    }
}
