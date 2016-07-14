package com.eyeball.modlauncher.modpack;

import com.eyeball.modlauncher.assets.AssetsHelper;
import com.eyeball.modlauncher.assets.LibrarySet;
import com.eyeball.modlauncher.file.FileHelper;
import com.eyeball.modlauncher.login.LoginHelper;
import com.eyeball.modlauncher.util.DownloadUtil;
import com.eyeball.modlauncher.util.StringUtils;
import com.eyeball.modlauncher.util.Utils;
import com.jrutil.TerminalHelper;
import com.jrutil.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        launchForgeInstaller(forgeDownload);

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
                File outputFile = new File(new File(FileHelper.getMCDir(), "libraries"), p.toString() + ".jar");
                outputFile.getParentFile().mkdirs();
                DownloadUtil.downloadFile(baseURL + p.toString() + "-universal.jar", outputFile);
            } else {
                p.append(".jar");
                String url = p.toString();
                if (lib.has("url")) continue;
//                if (lib.has("url")) url += ".pack.xz";

                File outputFile = new File(new File(FileHelper.getMCDir(), "libraries"), url);
                outputFile.getParentFile().mkdirs();
                DownloadUtil.downloadFile(baseURL + url, outputFile);
//                if (lib.has("url")) {
//                    try {
//                        File inputFile = new File(new File(FileHelper.getMCDir(), "libraries"), p.toString());
//                        System.out.println(inputFile);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }

    }

    private static void launchForgeInstaller(File forgeJar) {
        ArrayList<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(forgeJar.getAbsolutePath() + ":.");
        command.add("Launcher");
        command.add(FileHelper.getMCDir().getAbsolutePath());
        System.out.println(command);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(FileHelper.getMCLDir());
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        try {
            File launcherProfiles = new File(FileHelper.getMCDir(), "launcher_profiles.json");
            if (!launcherProfiles.exists()) {
                FileHelper.createFile(launcherProfiles, "{}");
            }
            System.out.println("Launching MC Forge Installer");
            Process process = processBuilder.start();
            synchronized (process) {
                try {
                    process.waitFor();
                } catch (InterruptedException ignored) {
                    ignored.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void downloadModpackFile(JSONObject modpackObj, String name) {
        File modpackDir = new File(new File(FileHelper.getMCLDir(), "modpacks"), name);
        File modpackFile = new File(modpackDir, "Modpack.zip");
        if (modpackFile.exists()) {
            try {
                if (DownloadUtil.calcSHA1(modpackFile).equals(modpackObj.getString("fileSHA1Sum")))
                    return;
                else {
                    System.out.println("Modpack update!");
                    FileHelper.delete(new File(modpackDir, "mods"));
                    FileHelper.delete(new File(modpackDir, "config"));
                    modpackFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DownloadUtil.hashedDownload(modpackObj.getString("fileSHA1Sum"), modpackObj.getString("location"), modpackFile, 3);
    }

    private static void createModpackDirs(String name) {
        File modpackDir = new File(new File(FileHelper.getMCLDir(), "modpacks"), name);
        modpackDir.mkdirs();
    }

    public static void launch(LoadInfo loadInfo) {
        String jar = loadInfo.jarFile,
                main = loadInfo.mainClass,
                args = loadInfo.args;
        JSONObject modpack = new JSONObject(FileHelper.read(new File(FileHelper.getMCLDir(), "modpacks.json"))).getJSONObject(loadInfo.modpackName);
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
        command.add(librarySet.classpathFormat(modpack.getJSONArray("drop")) + ":" + jar + (modded ? ":" + forgeLibsCP.substring(0, forgeLibsCP.length() - 1) : ""));
        command.add("-Djava.library.path=" + new File(new File(new File(FileHelper.getMCDir(), "versions"), loadInfo.mcVer), "natives").getAbsolutePath());
        command.add("-Duser.dir=" + new File(new File(FileHelper.getMCLDir(), "modpacks"), loadInfo.modpackName));
        List<String> extraArgs = askForExtra();
        command.addAll(extraArgs);
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
        Process process = null;
        try {
            process = processBuilder.start();
            synchronized (process) {
                process.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            if (process != null)
                process.destroy();
            System.exit(0);
        }
    }

    private static List<String> askForExtra() {
        ArrayList<String> e = new ArrayList<>();
        try {
            System.out.println("Extra java options: (Leave blank for none)");
            String maxram = TerminalHelper.read("How much max ram? (eg: 512M, 1G, 64G...) ");
            String minram = TerminalHelper.read("How much min ram? (eg: 512M, 1G, 64G, 128K...) ");
            String permGen = TerminalHelper.read("How much space for PermGen? (eg: 512M, 1G, 64G...) ");
            if (StringUtils.isNotNullOrEmpty(maxram)) {
                e.add("-Xmx" + maxram);
            }
            if (StringUtils.isNotNullOrEmpty(minram)) {
                e.add("-Xms" + minram);
            }
            if (StringUtils.isNotNullOrEmpty(permGen)) {
                e.add("-XX:MaxPermSize=" + permGen);
            }
            e.add("-XX:+UseConcMarkSweepGC");
            e.add("-XX:+UseParNewGC");
            e.add("-XX:+CMSParallelRemarkEnabled");
        } catch (IOException e1) {
        }
        return e;
    }
}
