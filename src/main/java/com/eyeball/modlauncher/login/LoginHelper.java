package com.eyeball.modlauncher.login;

import com.eyeball.modlauncher.file.FileHelper;
import com.jrutil.TerminalHelper;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class LoginHelper {

    private static ArrayList<LoginProfile> profiles = new ArrayList<>();

    public static LoginProfile USED_PROFILE;

    public static void loginIfNeeded() {
        loadProfileAndLogin();
    }

    private static void loadProfileAndLogin() {
        JSONObject profiles = loadProfilesFromFile();
        JSONObject profilesCopy = new JSONObject(profiles.toString());
        updateProfiles(profiles);
        if (!profiles.toString().equals(profilesCopy.toString())) {
            FileHelper.createFile(new File(FileHelper.getMCLDir(), "usernamecache.json"), profiles.toString());
        }
        if (LoginHelper.profiles.size() == 0) {
            LoginHelper.showLogin();
        }
        chooseProfileToPlayAs();

    }

    private static void updateProfiles(JSONObject profiles) {
        LoginHelper.profiles.clear();
        for (String profileName : profiles.keySet()) {
            JSONObject profile = profiles.getJSONObject(profileName);
            if (profile.has("accessToken") || profile.has("username") || profile.has("id")) {
                if (!profile.has("name")) profile.put("name", profileName);
                LoginHelper.profiles.add(new LoginProfile(profile));
            } else {
                profiles.remove(profileName);
            }
        }
    }

    private static void chooseProfileToPlayAs() {
        System.out.println("Choose a profile to play with: ");
        updateProfiles(loadProfilesFromFile());
        for (LoginProfile profile : LoginHelper.profiles) {
            System.out.println("  " + profile.getName());
        }
        try {
            String name = TerminalHelper.read("Profile name: ");
            if (name.equals("mod")) {
                modifyProfiles();
                System.exit(0);
            }
            boolean found = false;
            LoginProfile profileToUse = null;
            for (LoginProfile profile : LoginHelper.profiles) {
                if (profile.getName().equals(name)) {
                    found = true;
                    profileToUse = profile;
                }
            }
            if (!found) {
                System.err.println("No such profile! (Case sensitive!)");
                System.exit(1);
            }
            System.out.println("Logging in as " + profileToUse.getUsername() + "... Updating access token as needed...");
            if (!MCAuthUtils.validate(profileToUse.getAccessToken())) {
//            if (false) {
                System.out.println("Invalid token! Refreshing....");
                MCAuthUtils.refresh(profileToUse.getAccessToken(), profileToUse.getProfileData());
            } else {
                System.out.println("Token still good! Let's go!");
            }
            USED_PROFILE = profileToUse;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void modifyProfiles() {
        JSONObject profiles = loadProfilesFromFile();
        System.out.println("Choose a profile to edit: ");
        for (String profileName : profiles.keySet()) {
            System.out.println("  " + profileName);
        }
        try {
            String name = TerminalHelper.read("Profile: ");
            if (name.equals("add")) {
                showLogin();
            } else {
                JSONObject profileDesc = profiles.getJSONObject(name);
                System.out.println("Choose: ");
                System.out.println("1) Rename");
                System.out.println("2) Delete");
                int choice = TerminalHelper.readInt("Choice: ");
                switch (choice) {
                    case 2:
                        profiles.remove(name);
                        break;
                    case 1:
                    default:
                        profiles.remove(name);
                        String newName = TerminalHelper.read("New Name: ");
                        profileDesc.put("name", newName);
                        profiles.put(newName, profileDesc);
                        break;
                }
                FileHelper.createFile(new File(FileHelper.getMCLDir(), "usernamecache.json"), profiles.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject loadProfilesFromFile() {
        File usernameCacheFile = new File(FileHelper.getMCLDir(), "usernamecache.json");
        try {
            return new JSONObject(new JSONTokener(new FileInputStream(usernameCacheFile)));
        } catch (FileNotFoundException e) {
            FileHelper.createFile(usernameCacheFile, "");
            return new JSONObject();
        }
    }

    private static void showLogin() {
        try {
            String username = TerminalHelper.read("[Login] Username: ");
            String password = TerminalHelper.readPassword("[Login] Password: ");
            MCAuthUtils.login(username, password);
        } catch (IOException e) {
        }

    }
}
