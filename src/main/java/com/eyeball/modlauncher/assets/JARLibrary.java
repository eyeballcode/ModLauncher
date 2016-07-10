package com.eyeball.modlauncher.assets;

import com.eyeball.modlauncher.file.FileHelper;
import com.eyeball.modlauncher.util.DownloadUtil;
import org.json.JSONObject;

import java.io.File;

public class JARLibrary extends Library {

    JSONObject library;

    public JARLibrary(JSONObject library) {
        super(library);
        this.library = library;
    }

    public void download() {
        JSONObject downloadInfo = library.getJSONObject("downloads").getJSONObject("artifact");
        File output = new File(new File(FileHelper.getMCDir(), "libraries"), downloadInfo.getString("path"));
        output.getParentFile().mkdirs();
        DownloadUtil.hashedDownload(downloadInfo.getString("sha1"), downloadInfo.getString("url"), output, 5);
    }


    public String toClassPath() {
        JSONObject downloadInfo = library.getJSONObject("downloads").getJSONObject("artifact");
        File output = new File(new File(FileHelper.getMCDir(), "libraries"), downloadInfo.getString("path"));
        return output.getAbsolutePath();
    }
}
