package com.modlauncher.api.game;

import com.modlauncher.api.FileUtil;
import com.modlauncher.api.login.UserProfile;
import com.modlauncher.api.modpacks.ModPack;
import com.modlauncher.api.util.OSHelper;
import lib.mc.library.*;
import lib.mc.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameLauncher {

    public static void launch(ModPack modPack, UserProfile profile) throws IOException {
        launch(modPack, modPack.getLatestVersion(), profile);
    }

    public static void launch(ModPack modPack, String version, UserProfile profile) throws IOException {
        File modpackFolder = new File(new File(new File(FileUtil.mcLauncherFolder, "modpack"), modPack.getName()), version);
        ArrayList<String> args = new ArrayList<>();
        args.add("java");

        final File nativeOutputLib = extractNatives(modPack);
        args.add("-Djava.library.path=" + nativeOutputLib.getAbsolutePath().replace(" ", "\\ "));
        args.add("-Duser.dir=" + modpackFolder.getAbsolutePath().replace(" ", "\\ "));
        File forgeDataJSONFile = new File(new File(FileUtil.mcLauncherFolder, "forge-version-cache"), modPack.getForgeVersion().getForgeVersion());
        JSONObject forgeDataJSON = new JSONObject(new JSONTokener(new FileInputStream(forgeDataJSONFile)));
        String mainClass = forgeDataJSON.getJSONObject("versionInfo").getString("mainClass");

        ArrayList<String> mcArgs = parseArgs(forgeDataJSON.getJSONObject("versionInfo").getString("minecraftArguments"), modPack, modpackFolder, profile);

        args.add("-cp");

        String classPath = generateClassPath(modPack);
        args.add(classPath);

        args.add(mainClass);

        args.addAll(mcArgs);

        StringBuilder command = new StringBuilder();

        for (String s : args) command.append(s).append(" ");
        System.out.println(command);

        final File launchFile = new File(FileUtil.mcLauncherFolder, "launch-" + System.currentTimeMillis());

        FileOutputStream fos = new FileOutputStream(launchFile);
        fos.write(command.toString().getBytes());
        fos.close();

        switch(Utils.OSUtils.getOS()) {
            case OSX:
            case LINUX:
                Set<PosixFilePermission> perms = new HashSet<>();
                perms.add(PosixFilePermission.OWNER_EXECUTE);
                perms.add(PosixFilePermission.OWNER_READ);
                perms.add(PosixFilePermission.OWNER_WRITE);
                Files.setPosixFilePermissions(launchFile.toPath(), perms);
                break;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(launchFile.getAbsolutePath());
        processBuilder.inheritIO();
        final Process process = processBuilder.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    FileUtil.delete(nativeOutputLib);
                    launchFile.delete();
                    process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    FileUtil.delete(nativeOutputLib);
                    launchFile.delete();
                    System.exit(0);
                }
            }
        }.start();
    }

    private static String parseMCArg(String part, ModPack modpack, File modFolder, UserProfile profile) {
        switch (part) {
            case "${auth_player_name}":
                return profile.getPlayerName();
            case "${version_name}":
                return modpack.getMCVersion().getVersion();
            case "${game_directory}":
                return modFolder.getAbsolutePath().replace(" ", "\\ ");
            case "${assets_root}":
                return new File(FileUtil.mcLauncherFolder, "assets").getAbsolutePath();
            case "${assets_index_name}":
                return modpack.getMCVersion().getVersion();
            case "${auth_uuid}":
                return profile.getUuid();
            case "${auth_access_token}":
                return profile.getAccessToken();
            case "${user_properties}":
                return "{}";
            case "${user_type}":
                return "mojang";
            default:
                return part;
        }
    }

    private static ArrayList<String> parseArgs(String string, ModPack modpack, File modFolder, UserProfile profile) {
        ArrayList<String> parts = new ArrayList<>();
        for (String part : string.split(" ")) {
            String parsed = parseMCArg(part, modpack, modFolder, profile);
            parts.add(parsed);
        }
        return parts;
    }

    private static File extractNatives(ModPack modpack) throws IOException {
        File output = new File(FileUtil.mcLauncherFolder, "natives-" + System.currentTimeMillis());
        output.mkdir();
        File libraryFolder = new File(FileUtil.mcLauncherFolder, "libraries");
        for (LibraryObject libraryObject : modpack.getMCVersion().getLibrarySet().getLibraries()) {
            if (!(libraryObject instanceof NativeMCLibraryObject)) continue;
            File currentLibFolder = new File(libraryFolder, libraryObject.parseName().toURL()).getParentFile();
            if (!currentLibFolder.exists()) continue; // Native lib not avaliable for OS;
            //noinspection ConstantConditions
            for (File nativeLibFile : currentLibFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    System.out.println(name);
                    return name.endsWith(OSHelper.getOSNativeLibExt());
                }
            })) {
                System.out.println(nativeLibFile);
                FileUtil.copyFile(nativeLibFile, new File(output, nativeLibFile.getName()));
            }
        }
        return output;
    }

    private static String generateClassPath(ModPack modpack) throws IOException {
        LibrarySet minecraftLibraries = modpack.getMCVersion().getLibrarySet();
        File libraryFolder = new File(FileUtil.mcLauncherFolder, "libraries");

        JSONObject forgeVersionData = new JSONObject(new JSONTokener(new FileInputStream(new File(FileUtil.mcLauncherFolder, "forge-libraries.json"))));
        JSONArray librariesToDrop = forgeVersionData.getJSONObject(modpack.getForgeVersion().getForgeVersion()).getJSONArray("dropList");
        ArrayList<String> drop = new ArrayList<>();
        for (Object o : librariesToDrop) {
            String lib = (String) o;
            drop.add(lib);
        }

        StringBuilder builder = new StringBuilder();
        for (LibraryObject library : minecraftLibraries.getLibraries()) {
            LibraryObjectInfo info = library.parseName();

            if (drop.contains(library.getRawName())) continue; // Drop it
            File libraryFile = new File(libraryFolder, info.toURL());
            if (!libraryFile.exists()) continue; // Non available natives.
            builder.append(libraryFile.getAbsolutePath()).append(File.pathSeparator);
        }

        File forgeDataJSONFile = new File(new File(FileUtil.mcLauncherFolder, "forge-version-cache"), modpack.getForgeVersion().getForgeVersion());
        JSONObject forgeLibrariesJSON = new JSONObject(new JSONTokener(new FileInputStream(forgeDataJSONFile))).getJSONObject("versionInfo");
        JSONArray forgeVersionDataJSON = forgeLibrariesJSON.getJSONArray("libraries");

        for (Object e : forgeVersionDataJSON) {
            JSONObject libData = (JSONObject) e;
            String libRawName = libData.getString("name");
            LibraryObjectInfo info = new DefaultMCLibraryObject(libRawName, "").parseName();
            File libraryFile = new File(libraryFolder, info.toURL());
            if (!libraryFile.exists()) continue; // Shouldn't even happen
            builder.append(libraryFile.getAbsolutePath()).append(File.pathSeparator);
        }

        File versionJar = new File(new File(new File(FileUtil.mcLauncherFolder, "versions"), modpack.getMCVersion().getVersion()), modpack.getMCVersion().getVersion() + ".jar");
        File forgeVersionJar = new File(new File(new File(FileUtil.mcLauncherFolder, "versions"), modpack.getForgeVersion().getForgeVersion()), modpack.getForgeVersion().getForgeVersion() + ".jar");
        return (builder.toString() + versionJar.getAbsolutePath() + File.pathSeparator + forgeVersionJar.getAbsolutePath()).replace(" ", "\\ ");
    }

}
