package com.eyeball.modlauncher.info;

public enum OSType {

    WINDOWS, MAC, LINUX, OTHER;

    public static OSType getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
            return WINDOWS;
        else if (os.contains("mac"))
            return MAC;
        else if (os.contains("linux"))
            return LINUX;
        else return OTHER;
    }

    @Override
    public String toString() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }
}
