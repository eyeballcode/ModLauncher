package com.eyeball.modlauncher.login;

import com.eyeball.modlauncher.file.FileHelper;
import com.eyeball.modlauncher.util.Utils;
import com.jrutil.TerminalHelper;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class MCAuthUtils {

    public static String getClientID() {
        File authFile = new File(FileHelper.getMCLDir(), "auth.json");
        if (!authFile.exists()) {
            FileHelper.createFile(authFile, "{\"clientID\": \"\"}");
        }
        String random = Utils.noise();
        try {
            JSONObject object = new JSONObject(new JSONTokener(new FileInputStream(authFile)));
            if (!object.has("clientID") || object.getString("clientID").trim().equals("")) {
                object.put("clientID", random);
                FileHelper.createFile(authFile, object.toString());
            } else {
                random = object.getString("clientID");
            }
        } catch (Exception e) {
            return "MCL";
        }
        return random;
    }

    public static boolean validate(String accessToken) {
        String clientID = getClientID();
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://authserver.mojang.com/validate").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            JSONObject payload = new JSONObject();
            payload.put("accessToken", accessToken);
            payload.put("clientToken", clientID);
            outputStream.write(payload.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            return connection.getResponseCode() == 204;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean refresh(String accessToken, JSONObject profileData) {
        String clientID = getClientID();
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://authserver.mojang.com/refresh").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            JSONObject payload = new JSONObject();
            payload.put("accessToken", accessToken);
            payload.put("clientToken", clientID);
            payload.put("selectedProfile", profileData);
            outputStream.write(payload.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            JSONObject resp = new JSONObject(new JSONTokener(connection.getInputStream()));
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean login(String username, String password) {
        String clientID = getClientID();
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://authserver.mojang.com/authenticate").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            JSONObject payload = new JSONObject();
            payload.put("clientToken", clientID);
            payload.put("username", username);
            payload.put("password", password);
            payload.put("agent", new JSONObject("{}").put("name", "Minecraft").put("version", 1));
            outputStream.write(payload.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            JSONObject resp = new JSONObject(new JSONTokener(connection.getInputStream()));
            JSONObject selectedProfile = resp.getJSONObject("selectedProfile");
            String accessToken = resp.getString("accessToken"),
                    id = selectedProfile.getString("id"),
                    name = selectedProfile.getString("name");
            JSONObject userJSON = new JSONObject();
            userJSON.put("accessToken", accessToken);
            userJSON.put("id", id);
            userJSON.put("username", name);
            String profileName = TerminalHelper.read("What should I call this profile? ");
            while (profileName.toLowerCase().equals("mod") || profileName.toLowerCase().equals("add")) {
                System.err.println("Cannot call profile mod or add, sorry. Mod will modify profile settings later. ");
                profileName = TerminalHelper.read("Name? ");
            }
            userJSON.put("name", profileName);

            updateUserCache(userJSON, userJSON.getString("name"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void updateUserCache(JSONObject userJSON, String name) {
        File userCacheFile = new File(FileHelper.getMCLDir(), "usernamecache.json");
        try {
            JSONObject object = new JSONObject(new JSONTokener(new FileInputStream(userCacheFile)));
            object.put(name, userJSON);
            FileHelper.createFile(userCacheFile, object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
