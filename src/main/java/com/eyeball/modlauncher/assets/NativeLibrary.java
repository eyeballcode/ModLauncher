package com.eyeball.modlauncher.assets;

import com.eyeball.modlauncher.file.FileHelper;
import com.eyeball.modlauncher.info.OSType;
import com.eyeball.modlauncher.util.DownloadUtil;
import com.eyeball.modlauncher.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NativeLibrary extends Library {

    JSONObject library;
    String mcVersion;

    public NativeLibrary(JSONObject library, String mcVer) {
        super(library);
        this.library = library;
        mcVersion = mcVer;
    }

    boolean canUse() {
        if (library.has("rules")) {
            for (Object _ : library.getJSONArray("rules")) {
                JSONObject rule = (JSONObject) _;
                if (rule.getString("action").equals("disallow")) {
                    String osname = rule.getJSONObject("os").getString("name");
                    if (OSType.getOS().toString().toLowerCase().equals(osname)) {
                        return false;
                    }
                } else {
                    if (rule.has("os")) {
                        String osname = rule.getJSONObject("os").getString("name");
                        if (!OSType.getOS().toString().toLowerCase().equals(osname)) return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    void download() {
        if (canUse()) {
            String osname = OSType.getOS().toString().toLowerCase();
            String nativeName = library.getJSONObject("natives").getString(osname);
            nativeName = nativeName.replaceAll("\\$\\{arch\\}", System.getProperty("sun.arch.data.model"));
            JSONObject downloadInfo = library.getJSONObject("downloads").getJSONObject("classifiers").getJSONObject(nativeName);
            File output = new File(new File(FileHelper.getMCDir(), "libraries"), downloadInfo.getString("path"));
            output.getParentFile().mkdirs();
            DownloadUtil.hashedDownload(downloadInfo.getString("sha1"), downloadInfo.getString("url"), output, 5);
            if (library.has("extract")) {
                File extractDir = new File(new File(new File(FileHelper.getMCDir(), "versions"), mcVersion), "natives");
                extractDir.mkdirs();
                JSONObject extractRules = library.getJSONObject("extract");
                if (extractRules.has("exclude")) {
                    JSONArray exclude = extractRules.getJSONArray("exclude");
                    ArrayList<String> excludes = new ArrayList<>();
                    for (Object _ : exclude) {
                        excludes.add((String) _);
                    }
                    try {
                        Utils.extractZip(extractDir, output);
                        for (String excludeName : excludes) {
                            FileHelper.delete(new File(extractDir, excludeName));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public String toClassPath() {
        if (!canUse()) return null;
        String nativeName = library.getJSONObject("natives").getString(OSType.getOS().toString().toLowerCase());
        JSONObject downloadInfo = library.getJSONObject("downloads").getJSONObject("classifiers").getJSONObject(nativeName);
        File output = new File(new File(FileHelper.getMCDir(), "libraries"), downloadInfo.getString("path"));
        return output.getAbsolutePath();
    }

}
