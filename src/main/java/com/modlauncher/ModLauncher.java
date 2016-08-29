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

import com.modlauncher.gui.ModLauncherFrame;
import com.modlauncher.util.AWTUtil;
import lib.mc.util.Utils;

import java.awt.*;
import java.io.IOException;

public class ModLauncher {

    public static void main(String[] args) throws IOException {
        System.out.println("ModLauncher started on " + Utils.OSUtils.getOS());
        boolean asssetsCompleted = LauncherStructure.downloadAssets();
        if (asssetsCompleted) {
//        if (true) {
//            AWTUtil.setLAF();
            ModLauncherFrame launcherFrame = new ModLauncherFrame();
            launcherFrame.pack();
            launcherFrame.setVisible(true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            launcherFrame.setLocation(screenSize.width / 2 - launcherFrame.getWidth() / 2,
                    screenSize.height / 2 - launcherFrame.getHeight() / 2);
        }
    }

}
