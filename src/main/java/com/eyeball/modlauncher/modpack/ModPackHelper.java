package com.eyeball.modlauncher.modpack;

import com.eyeball.modlauncher.assets.AssetsHelper;
import com.eyeball.modlauncher.assets.LibrarySet;
import com.eyeball.modlauncher.file.FileHelper;
import com.eyeball.modlauncher.login.LoginHelper;
import com.eyeball.modlauncher.util.DownloadUtil;
import com.eyeball.modlauncher.util.Utils;
import com.jrutil.TerminalHelper;
import com.jrutil.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.tukaani.xz.XZInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class ModPackHelper {

    static String forge_ver;
    static boolean modded;

    public static LoadInfo requestVersion() {
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
            return loadModpack(name, modpacksList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static LoadInfo loadModpack(String name, JSONObject modpacks) {
        if (!modpacks.has(name)) {
            System.err.println("Modpack not found! Case sensitive");
            System.exit(2);
        }
        JSONObject modpackObj = modpacks.getJSONObject(name);
        createModpackDirs(name);
        downloadModpackFile(modpackObj, name);
        unzipModpackFile(name);
        String mcVersion = modpackObj.getString("mcVersion");
        AssetsHelper.downloadAssetsForMCVersion(mcVersion);
        String args, mainClass, jarFile;
        if (modpackObj.getBoolean("modded")) {
            modded = true;
            String forgeVersion = modpackObj.getString("forgeVersion");
            forge_ver = "Forge-" + forgeVersion;
            System.out.println("Downloading MC Forge " + forgeVersion);
            downloadForge(forgeVersion);
            File forgeInfo = new File(new File(FileUtils.getTmpDir(), "Forge-" + forgeVersion), "install_profile.json");
            JSONObject profile = new JSONObject(FileHelper.read(forgeInfo)).getJSONObject("versionInfo");
            args = profile.getString("minecraftArguments");
            mainClass = profile.getString("mainClass");
        } else {
            File versionsCache = new File(new File(FileHelper.getMCLDir(), "versionsCache"), mcVersion + ".json");
            JSONObject versionInfo = new JSONObject(FileHelper.read(versionsCache));
            args = versionInfo.getString("minecraftArguments");
            mainClass = versionInfo.getString("mainClass");
        }
        jarFile = new File(new File(new File(FileHelper.getMCDir(), "versions"), mcVersion), mcVersion + ".jar").getAbsolutePath();
        return new LoadInfo(args, mainClass, jarFile, mcVersion, name);
    }

    private static void downloadForge(String forgeVersion) {
        File forgeDownload = new File(FileUtils.getTmpDir(), "Forge-" + forgeVersion + ".jar");
        String forgeURL = "http://files.minecraftforge.net/maven/net/minecraftforge/forge/" + forgeVersion + "/forge-" + forgeVersion + "-installer.jar";
        DownloadUtil.downloadFile(forgeURL, forgeDownload);
        File extractDir = new File(FileUtils.getTmpDir(), "Forge-" + forgeVersion);
        extractDir.mkdirs();
        try {
            Utils.extractZip(extractDir, forgeDownload);
        } catch (IOException ignored) {
        }

        JSONObject forge = new JSONObject(FileHelper.read(new File(new File(FileUtils.getTmpDir(), forge_ver), "install_profile.json")));
        JSONArray forgeLibs = forge.getJSONObject("versionInfo").getJSONArray("libraries");
        for (Object _ : forgeLibs) {
            JSONObject lib = (JSONObject) _;
            String name = lib.getString("name");
            String[] parts = name.split(":");

            StringBuilder p = new StringBuilder();

            String baseURL = "https://libraries.minecraft.net/";
            if (lib.has("url")) baseURL = lib.getString("url");
            p.append(parts[0].replaceAll("\\.", "/"));
            p.append("/");
            p.append(parts[1]);
            p.append("/");
            p.append(parts[2]);
            p.append("/");
            p.append(parts[1]);
            p.append("-");
            p.append(parts[2]);
            if (parts[1].equals("forge")) {
                p.append("-universal.jar");
                File outputFile = new File(new File(FileHelper.getMCDir(), "libraries"), p.toString());
                outputFile.getParentFile().mkdirs();
                DownloadUtil.downloadFile(baseURL + p.toString(), outputFile);
            } else {
                p.append(".jar");
                if (lib.has("url")) p.append(".pack.xz");
                File outputFile = new File(new File(FileHelper.getMCDir(), "libraries"), p.toString());
                outputFile.getParentFile().mkdirs();
                DownloadUtil.downloadFile(baseURL + p.toString(), outputFile);
                if (lib.has("url")) {
                    try {
                        XZInputStream input = new XZInputStream(new FileInputStream(outputFile));
                        ReadableByteChannel rbc = Channels.newChannel(input);
                        FileOutputStream fos = new FileOutputStream(new File(outputFile.getName().substring(0, outputFile.getName().length() - ".pack.xz".length())));
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        System.out.println("Unpacked " + outputFile.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        launchForgeInstaller(forgeDownload);
    }

    private static void launchForgeInstaller(File forgeJar) {
        ArrayList<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(forgeJar.getAbsolutePath() + ":.");
        command.add("Launcher");
        command.add(FileHelper.getMCDir().getAbsolutePath());
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(FileHelper.getMCLDir());
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        try {
            System.out.println("Launching MC Forge Installer");
            Process process = processBuilder.start();
            synchronized (process) {
                try {
                    process.waitFor();
                } catch (InterruptedException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void unzipModpackFile(String name) {
        File modpackDir = new File(new File(FileHelper.getMCLDir(), "modpacks"), name);
        File modpackFile = new File(modpackDir, "Modpack.zip");
        try {
            Utils.extractZip(modpackDir, modpackFile);
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

    public static void launch(LoadInfo loadInfo) {
        String jar = loadInfo.jarFile,
                main = loadInfo.mainClass,
                args = loadInfo.args;
        args = args
                .replaceAll("\\$\\{auth_player_name\\}", LoginHelper.USED_PROFILE.getUsername())
                .replaceAll("\\$\\{version_name\\}", loadInfo.mcVer)
                .replaceAll("\\$\\{game_directory\\}", new File(new File(FileHelper.getMCLDir(), "modpacks"), loadInfo.modpackName).getAbsolutePath())
                .replaceAll("\\$\\{assets_root\\}", new File(FileHelper.getMCDir(), "assets").getAbsolutePath())
                .replaceAll("\\$\\{assets_index_name\\}", loadInfo.mcVer)
                .replaceAll("\\$\\{auth_uuid\\}", LoginHelper.USED_PROFILE.getID())
                .replaceAll("\\$\\{auth_access_token\\}", LoginHelper.USED_PROFILE.getAccessToken())
                .replaceAll("\\$\\{user_properties\\}", "{}")
                .replaceAll("\\$\\{user_type\\}", "Player");

        File versionsCache = new File(new File(FileHelper.getMCLDir(), "versionsCache"), loadInfo.mcVer + ".json");
        JSONObject versionManifest = new JSONObject(FileHelper.read(versionsCache));
        LibrarySet librarySet = new LibrarySet(versionManifest.getJSONArray("libraries"), versionManifest);
        StringBuilder forgeLibsCP = new StringBuilder();
        if (modded) {
            System.out.println("Adding forge jars...");
            JSONObject forge = new JSONObject(FileHelper.read(new File(new File(FileUtils.getTmpDir(), forge_ver), "install_profile.json")));
            JSONArray forgeLibs = forge.getJSONObject("versionInfo").getJSONArray("libraries");
            for (Object _ : forgeLibs) {
                JSONObject lib = (JSONObject) _;
                String name = lib.getString("name");
                String[] parts = name.split(":");
                StringBuilder p = new StringBuilder();

                p.append(new File(FileHelper.getMCDir(), "libraries").getAbsolutePath()).append(File.separator);
                p.append(parts[0].replaceAll("\\.", File.separator));
                p.append(File.separator);
                p.append(parts[1]);
                p.append(File.separator);
                p.append(parts[2]);
                p.append(File.separator);
                p.append(parts[1]);
                p.append("-");
                p.append(parts[2]);
                p.append(".jar:");
                forgeLibsCP.append(p);
            }
        }
        ArrayList<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(librarySet.classpathFormat() + ":" + jar + (modded ? ":" + forgeLibsCP.substring(0, forgeLibsCP.length() - 1) : ""));
        command.add("-Djava.library.path=" + new File(new File(new File(FileHelper.getMCDir(), "versions"), loadInfo.mcVer), "natives").getAbsolutePath());
        command.add(main);
        for (String part : args.split(" ")) {
            command.add(part);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        for (String s : command) {
            System.out.print(s + " ");
        }
        System.out.println("");
        try {
            Process process = processBuilder.start();
//            new InputStreamThread(process.getInputStream()).start();
            synchronized (process) {
                process.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
