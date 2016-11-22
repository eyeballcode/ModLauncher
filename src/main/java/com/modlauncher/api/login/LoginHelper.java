package com.modlauncher.api.login;

import com.modlauncher.api.FileUtil;
import lib.mc.auth.Authenticator;
import lib.mc.player.LoginSession;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;

public class LoginHelper {

    private final static Object lock = new Object();

    /**
     * Example for users.json:
     * <p>
     * {
     * "myuuid": {
     * accessToken: "token",
     * player: {
     * displayName: "Eyeballcode",
     * username: "email",
     * uuid: "my-uuid"
     * }
     * }
     * }
     *
     * @return
     * @throws FileNotFoundException
     */
    public static UserProfile[] getProfiles() throws FileNotFoundException {
        synchronized (lock) {
            File profileFile = new File(FileUtil.mcLauncherFolder, "users.json");
            if (profileFile.exists()) {
                JSONObject object = new JSONObject(new JSONTokener(new FileInputStream(profileFile)));
                ArrayList<UserProfile> profilesArray = new ArrayList<>();
                for (String mcUUID : object.keySet()) {
                    JSONObject profile = object.getJSONObject(mcUUID);
                    String accessToken = profile.getString("accessToken");
                    JSONObject playerData = profile.getJSONObject("player");
                    String playerName = playerData.getString("displayName");
                    String username = playerData.getString("username");
                    String uuid = playerData.getString("uuid");
                    profilesArray.add(new UserProfile(accessToken, playerName, username, uuid, mcUUID));
                }
                return profilesArray.toArray(new UserProfile[profilesArray.size()]);
            } else {
                return new UserProfile[0];
            }
        }

    }

    public static void login(String username, String password) throws IOException {
        synchronized (lock) {
            LoginSession loginSession = Authenticator.login(username, password, "MC-ModLauncher");
            JSONObject data = new JSONObject();
            data.put("accessToken", loginSession.getAccessToken());
            JSONObject playerData = new JSONObject();
            playerData.put("displayName", loginSession.forPlayer().getName());
            playerData.put("username", loginSession.forPlayer().getUsername());
            playerData.put("uuid", loginSession.forPlayer().getUUID());
            data.put("player", playerData);
            File profileFile = new File(FileUtil.mcLauncherFolder, "users.json");
            if (!profileFile.exists()) {
                PrintWriter printWriter = new PrintWriter(profileFile);
                printWriter.write("{}");
                printWriter.close();
            }
            JSONObject profiles = new JSONObject(new JSONTokener(new FileInputStream(profileFile)));
            profiles.put(loginSession.forPlayer().getUUIDMCFormat(), data);
            PrintWriter printWriter = new PrintWriter(profileFile);
            printWriter.write(profiles.toString(4));
            printWriter.close();
        }
    }
}
