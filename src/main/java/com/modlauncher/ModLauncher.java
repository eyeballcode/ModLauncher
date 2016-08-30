/*
 * 	Copyright (C) 2016 Eyeballcode
 *
 * 	This program is free software: you can redistribute it and/or modify
 * 	it under the terms of the GNU General Public License as published by
 * 	the Free Software Foundation, either version 3 of the License, or
 * 	(at your option) any later version.
 *
 * 	This program is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 	GNU General Public License for more details.
 *
 * 	You should have received a copy of the GNU General Public License
 * 	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 	See LICENSE.MD for more details.
 */

package com.modlauncher;

import com.modlauncher.gui.ConsoleTab;
import com.modlauncher.gui.ModLauncherFrame;
import com.modlauncher.logging.LauncherLogger;
import com.modlauncher.users.GameUserCache;
import com.modlauncher.util.AWTUtil;
import com.sun.deploy.uitoolkit.ui.ConsoleTraceListener;
import com.sun.deploy.uitoolkit.ui.LoggerConsole;
import lib.mc.auth.Authenticator;
import lib.mc.except.LoginException;
import lib.mc.player.AccessToken;
import lib.mc.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ModLauncher {

    public static void main(String[] args) throws IOException {
        ConsoleTab.tab = new ConsoleTab();
        LauncherLogger.logger.log("ModLauncher started on " + Utils.OSUtils.getOS());

        final ModLauncherFrame launcherFrame = new ModLauncherFrame();
        launcherFrame.pack();
        launcherFrame.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        launcherFrame.setLocation(screenSize.width / 2 - launcherFrame.getWidth() / 2,
                screenSize.height / 2 - launcherFrame.getHeight() / 2);
//        boolean asssetsCompleted = LauncherStructure.downloadAssets();
        AWTUtil.setLAF();
        SwingUtilities.updateComponentTreeUI(launcherFrame);
        try {
            AccessToken token = GameUserCache.getUser();
            if (token != null) {
                LauncherLogger.logger.log("Logging in as " + token.getPlayer().getName());
                boolean valid = Authenticator.validate(token);
                if (!valid) {
                    try {
                        LauncherLogger.logger.log("Refreshing access token...");
                        token = Authenticator.refresh(token);
                        GameUserCache.setUser(token);
                    } catch (Exception e) {
                        throw new IOException(e.getMessage());
                    }
                }
                launcherFrame.setupActualFrame();
            } else {
                launcherFrame.setupLoginFrame();
            }
        } catch (IOException e) {
            LauncherLogger.logger.error(e.getMessage());
        }
    }

}
