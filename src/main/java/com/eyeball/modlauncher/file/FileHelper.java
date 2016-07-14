package com.eyeball.modlauncher.file;

import com.eyeball.modlauncher.info.OSType;
import com.eyeball.modlauncher.util.DownloadUtil;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FileHelper {

    public static File getMCLDir() {
        switch (OSType.getOS()) {
            case WINDOWS:
                return new File(System.getProperty("user.home") + "\\%APPDATA%\\Roaming\\MCLauncher");
            case MAC:
                return new File(System.getProperty("user.home") + "/Library/Application Support/MCLauncher");
            case LINUX:
            case OTHER:
            default:
                return new File(System.getProperty("user.home") + "/.MCLauncher");
        }
    }

    public static File getMCDir() {
        switch (OSType.getOS()) {
            case WINDOWS:
                return new File(System.getProperty("user.home") + "\\%APPDATA%\\Roaming\\.minecraft");
            case MAC:
                return new File(System.getProperty("user.home") + "/Library/Application Support/minecraft");
            case LINUX:
            case OTHER:
            default:
                return new File(System.getProperty("user.home") + "/.minecraft");
        }
    }

    public static void initFilesAsNeeded() {
        File root = getMCDir();
        if (!root.exists()) root.mkdirs();
        root = getMCLDir();
        if (!root.exists()) root.mkdirs();
        JSONObject filesList = new JSONObject(new JSONTokener(FileHelper.class.getResourceAsStream("/files.json")));
        for (String fileName : filesList.keySet()) {
            JSONObject fileDesc = filesList.getJSONObject(fileName);
            File fileObj = new File(root, fileName);
            String expectedFormat = fileDesc.getString("format");
            if (!fileObj.exists()) {
                if (expectedFormat.equals("json"))
                    createFile(fileObj, "{}");
                else if (expectedFormat.equals("dir"))
                    fileObj.mkdirs();
                else
                    createFile(fileObj);
            }
        }
    }

    public static void createFile(File fileObj) {
        try {
            FileWriter w = new FileWriter(fileObj);
            w.write("");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createFile(File fileObj, String text) {
        try {
            FileWriter w = new FileWriter(fileObj);
            w.write(text);
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadFiles() {
        ArrayList<JSONObject> filesQueued = new ArrayList<>();
        JSONObject files = new JSONObject(new JSONTokener(FileHelper.class.getResourceAsStream("/static.json")));
        for (String fileName : files.keySet()) {
            JSONObject object = files.getJSONObject(fileName);
            if (object.has("alwaysRefresh")) {
                if (object.getBoolean("alwaysRefresh")) {
                    System.out.println("Redownloading " + object.getString("name"));
                    filesQueued.add(object);
                }
                continue;
            }
            if (new File(getMCLDir(), fileName).exists()) {
                if (!hashMatch(new File(getMCLDir(), fileName), object.getString("sha1sum"))) {
                    System.out.println("Queued file " + fileName + " to redownload.");
                    new File(getMCLDir(), fileName).delete();
                    filesQueued.add(object);
                }
            } else {
                filesQueued.add(object);
            }
        }
        for (JSONObject object : filesQueued) {
            File output = new File(getMCLDir(), object.getString("name"));
            output.delete();
            if (object.has("alwaysRefresh"))
                DownloadUtil.downloadFile(object.getString("location"), output);
            else
                DownloadUtil.hashedDownload(object.getString("sha1sum"), object.getString("location"), output, 5);
        }
    }

    public static String calcSHA1(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (InputStream input = new FileInputStream(file)) {

            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            return new HexBinaryAdapter().marshal(sha1.digest()).toLowerCase();
        }
    }

    public static boolean hashMatch(File check, String sha1sum) {
        try {
            return calcSHA1(check).equals(sha1sum);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String read(File file) {
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(new FileInputStream(file));
            byte[] bytes = new byte[inputStream.available()];
            inputStream.readFully(bytes);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void delete(File file) {
        System.out.println("Delete " + file.getAbsolutePath());
        File[] files = file.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isFile()) f.delete();
                else delete(f);
            }
        file.delete();
    }
}


