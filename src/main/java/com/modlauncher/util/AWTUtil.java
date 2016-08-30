package com.modlauncher.util;

import com.modlauncher.gui.ModLauncherFrame;
import com.modlauncher.logging.LauncherLogger;
import com.modlauncher.users.GameUserCache;
import lib.mc.auth.Authenticator;
import lib.mc.player.AccessToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class AWTUtil {

    public static void setLAF() {
        try {
            if (UIManager.getSystemLookAndFeelClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
                LauncherLogger.logger.log("Setting LAF to System LAF has no effect, searching...");
                // In LXDE or XFCE this happens...
                // So we check if the previous LAF is equals to the current one.
                HashMap<String, String> founds = new HashMap<>();
                String[] preferences = {"GTK+", "Nimbus"};

                for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
                    founds.put(lookAndFeelInfo.getName(), lookAndFeelInfo.getClassName());
                }
                boolean found = false;
                for (String pref : preferences) {
                    if (founds.containsKey(pref)) {
                        LauncherLogger.logger.log("Found " + pref + ", using it");
                        UIManager.setLookAndFeel(founds.get(pref));
                        found = true;
                        break;
                    }
                }
                if (!found) LauncherLogger.logger.log("Could not find any preferred LAFs, using Metal.");
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Never happen
        }
    }
}

