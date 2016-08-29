package com.modlauncher.users;

import com.modlauncher.util.FileUtil;
import lib.mc.auth.Authenticator;
import lib.mc.player.AccessToken;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class GameUserCache {

    private static final Object lock = new Object();


    public static AccessToken getUser() throws IOException {
        synchronized (lock) {
            File file = new File(FileUtil.getMCLauncherDir(), "usercache.json");
            if (!file.exists()) {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write("{}".getBytes());
                outputStream.close();
                return null;
            }
            JSONObject userList = new JSONObject(new JSONTokener(new FileInputStream(file)));
            if (userList.has("name"))
                return Authenticator.genFromCache(userList);
            else return null;
        }
    }

    public static void setUser(AccessToken accessToken) throws IOException {
        synchronized (lock) {
            File file = new File(FileUtil.getMCLauncherDir(), "usercache.json");
            JSONObject userList = Authenticator.genToCache(accessToken);
            System.out.println(userList);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(userList.toString(4).getBytes());
            outputStream.close();
        }
    }

}
