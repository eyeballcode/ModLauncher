package com.modlauncher.api.game;

import com.modlauncher.api.FileUtil;
import com.modlauncher.api.login.UserProfile;
import com.modlauncher.api.modpacks.ModPack;
import com.modlauncher.api.util.OSHelper;
import lib.mc.library.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameLauncher {

    public static void launch(ModPack modPack, UserProfile profile) throws IOException {
        launch(modPack, modPack.getLatestVersion(), profile);
    }

    public static void launch(ModPack modPack, String version, UserProfile profile) throws IOException {
        File modpackFolder = new File(new File(new File(FileUtil.mcLauncherFolder, "modpack"), modPack.getName()), version);
        ArrayList<String> args = new ArrayList<>();
        args.add("java");
        args.add("-cp");

        String classPath = generateClassPath(modPack);
        args.add(classPath);
        final File nativeOutputLib = extractNatives(modPack);
        args.add("-Djava.library.path=" + nativeOutputLib.getAbsolutePath());
        args.add("-Duser.dir=" + modpackFolder.getAbsolutePath());
        File forgeDataJSONFile = new File(new File(FileUtil.mcLauncherFolder, "forge-version-cache"), modPack.getForgeVersion().getForgeVersion());
        JSONObject forgeDataJSON = new JSONObject(new JSONTokener(new FileInputStream(forgeDataJSONFile)));
        String mainClass = forgeDataJSON.getJSONObject("versionInfo").getString("mainClass");

        String mcArgs = parseArgs(forgeDataJSON.getJSONObject("versionInfo").getString("minecraftArguments"), modPack, modpackFolder, profile);

        args.add(mainClass);

        args.addAll(Arrays.asList(mcArgs.split(" ")));

        for (String s : args) System.out.print(s + " ");
        System.out.println();


        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.inheritIO();
        final Process process = processBuilder.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    FileUtil.delete(nativeOutputLib);
                }
            }
        }.start();
    }

    private static String parseArgs(String string, ModPack modpack, File modFolder, UserProfile profile) {
        return string.replace("${auth_player_name}", profile.getPlayerName())
                .replace("${version_name}", modpack.getMCVersion().getVersion())
                .replace("${game_directory}", modFolder.getAbsolutePath())
                .replace("${assets_root}", new File(FileUtil.mcLauncherFolder, "assets").getAbsolutePath())
                .replace("${assets_index_name}", modpack.getMCVersion().getVersion())
                .replace("${auth_uuid}", profile.getUuid())
                .replace("${auth_access_token}", profile.getAccessToken())
                .replace("${user_properties}", "{}")
                .replace("${user_type}", "Player");
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
                    return name.endsWith(OSHelper.getOSNativeLibExt());
                }
            })) {
                FileUtil.copyFile(nativeLibFile, new File(output, nativeLibFile.getName()));
            }
        }
        return output;
    }

    private static String generateClassPath(ModPack modpack) throws IOException {
        LibrarySet minecraftLibraries = modpack.getMCVersion().getLibrarySet();
        File libraryFolder = new File(FileUtil.mcLauncherFolder, "libraries");
        StringBuilder builder = new StringBuilder();
        for (LibraryObject library : minecraftLibraries.getLibraries()) {
            LibraryObjectInfo info = library.parseName();
            File libraryFile = new File(libraryFolder, info.toURL());
            if (!libraryFile.exists()) continue; // Non available natives.
            builder.append(libraryFile.getAbsolutePath()).append(File.pathSeparator);
        }

        File forgeDataJSONFile = new File(new File(FileUtil.mcLauncherFolder, "forge-version-cache"), modpack.getForgeVersion().getForgeVersion());
        JSONArray forgeVersionDataJSON = new JSONObject(new JSONTokener(new FileInputStream(forgeDataJSONFile))).getJSONObject("versionInfo").getJSONArray("libraries");

        for (Object e : forgeVersionDataJSON) {
            JSONObject libData = (JSONObject) e;
            String libRawName = libData.getString("name");
            LibraryObjectInfo info = new DefaultMCLibraryObject(libRawName, "").parseName();
            File libraryFile = new File(libraryFolder, info.toURL());
            System.out.println(libraryFile);
            if (!libraryFile.exists()) continue; // Shouldn't even happen
            builder.append(libraryFile.getAbsolutePath()).append(File.pathSeparator);
        }

        File versionJar = new File(new File(new File(FileUtil.mcLauncherFolder, "versions"), modpack.getMCVersion().getVersion()), modpack.getMCVersion().getVersion() + ".jar");
        File forgeVersionJar = new File(new File(new File(FileUtil.mcLauncherFolder, "versions"), modpack.getForgeVersion().getForgeVersion()), modpack.getForgeVersion().getForgeVersion() + ".jar");
        return builder.toString() + versionJar.getAbsolutePath() + File.pathSeparator + forgeVersionJar.getAbsolutePath();
    }

}
