package com.modlauncher.api.login;

import lib.mc.player.LoginSession;
import lib.mc.player.Player;
import lib.mc.player.UserData;
import org.json.JSONObject;

public class UserProfile {
    String accessToken, playerName, username, uuid, mcUUID;
    LoginSession session;
    Player player;

    public UserProfile(String accessToken, String playerName, String username, String uuid, String mcUUID) {
        this.accessToken = accessToken;
        this.playerName = playerName;
        this.username = username;
        this.uuid = uuid;
        this.mcUUID = mcUUID;
        this.player = new Player(uuid, username, playerName, false, false, new UserData(new JSONObject("{properties: []}")));
        session = new LoginSession(accessToken, "MC-ModLauncher", player);
    }

    @Override
    public String toString() {
        return playerName + "@" + username + "@" + uuid + "@" + accessToken;
    }
}
