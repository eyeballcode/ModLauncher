package com.eyeball.modlauncher;

import com.eyeball.modlauncher.build.BuildInfo;
import com.eyeball.modlauncher.file.FileHelper;
import com.eyeball.modlauncher.info.OSType;
import com.eyeball.modlauncher.login.LoginHelper;
import com.eyeball.modlauncher.modpack.LoadInfo;
import com.eyeball.modlauncher.modpack.ModPackHelper;

public class ModLauncher {

    public static void main(String[] args) {
        System.out.println("ModLauncher running Build " + BuildInfo.VERSION + " started on " + OSType.getOS() + ".");
        FileHelper.initFilesAsNeeded();
        FileHelper.downloadFiles();
        LoginHelper.loginIfNeeded();
        LoadInfo loadInfo = ModPackHelper.requestVersion();
        ModPackHelper.launch(loadInfo);

    }

}
