package com.eyeball.modlauncher.modpack;

public class LoadInfo {
    String args, mainClass, jarFile;
    public String mcVer;
    public String modpackName;

    public LoadInfo(String args, String mainClass, String jarFile, String mcVer, String modpackName) {
        this.args = args;
        this.mainClass = mainClass;
        this.jarFile = jarFile;
        this.mcVer = mcVer;
        this.modpackName = modpackName;
    }

}
