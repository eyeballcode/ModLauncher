package com.eyeball.modlauncher.modpack;

import com.eyeball.modlauncher.assets.AssetsHelper;
import com.eyeball.modlauncher.file.FileHelper;
import com.eyeball.modlauncher.util.DownloadUtil;
import com.jrutil.TerminalHelper;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModPackHelper {

    public static void requestVersion() {
        try {
            JSONObject modpacksList = new JSONObject(new JSONTokener(new FileInputStream(new File(FileHelper.getMCLDir(), "modpacks.json"))));
            System.out.println("Choose modpack to play: ");
            for (String modpackName : modpacksList.keySet()) {
                JSONObject modpack = modpacksList.getJSONObject(modpackName);
                System.out.println(" " + modpackName);
                System.out.println("    Modded: " + modpack.getBoolean("modded"));
                if (modpack.getBoolean("modded"))
                    System.out.println("    Forge Version: " + modpack.getString("forgeVersion"));
                System.out.println("    Minecraft Version: " + modpack.getString("mcVersion"));
                System.out.println();
            }

            String name = TerminalHelper.read("Modpack name: ");
            loadModpack(name, modpacksList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadModpack(String name, JSONObject modpacks) {
        if (!modpacks.has(name)) {
            System.err.println("Modpack not found! Case sensitive");
            System.exit(2);
        }
        JSONObject modpackObj = modpacks.getJSONObject(name);
        createModpackDirs(name);
        downloadModpackFile(modpackObj, name);
        unzipModpackFile(name);
        AssetsHelper.downloadAssetsForMCVersion(modpackObj.getString("mcVersion"));

    }

    private static void unzipModpackFile(String name) {
        File modpackDir = new File(new File(FileHelper.getMCLDir(), "modpacks"), name);
        File modpackFile = new File(modpackDir, "Modpack.zip");
        try {
            ZipFile zip = new ZipFile(modpackFile);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    new File(modpackDir, entry.getName()).mkdirs();
                } else {
                    File file = new File(modpackDir, entry.getName());
                    if (file.exists()) {
                        long required = entry.getSize(),
                                existing = file.length();
                        if (required == existing) {
                            System.out.println("Not extracting " + entry.getName());
                            continue;
                        }
                    }

                    InputStream input = zip.getInputStream(entry);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    ReadableByteChannel rbc = Channels.newChannel(input);
                    outputStream.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    System.out.println("Unzipped " + entry.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void downloadModpackFile(JSONObject modpackObj, String name) {
        File modpackDir = new File(new File(FileHelper.getMCLDir(), "modpacks"), name);
        DownloadUtil.hashedDownload(modpackObj.getString("fileSHA1Sum"), modpackObj.getString("location"), new File(modpackDir, "Modpack.zip"), 3);
    }

    private static void createModpackDirs(String name) {
        File modpackDir = new File(new File(FileHelper.getMCLDir(), "modpacks"), name);
        modpackDir.mkdirs();
    }
}
