package com.eyeball.modlauncher.login;

import org.json.JSONObject;

public class LoginProfile {

    public String name, accessToken, username;
    private JSONObject profileData;

    public LoginProfile(JSONObject data) {
        this(data.getString("name"), data.getString("accessToken"), data.getString("username"), data.getString("id"));
    }

    public LoginProfile(String name, String accessToken, String username, String id) {
        this.name = name;
        this.accessToken = accessToken;
        this.username = username;
        profileData = new JSONObject();
        profileData.put("id", id);
        profileData.put("name", username);

    }

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "LoginProfile[name=" + getName() + ",accessToken=" + getAccessToken() + ",username=" + getAccessToken() + "]@" + hashCode();
    }

    public JSONObject getProfileData() {
        return profileData;
    }
}
