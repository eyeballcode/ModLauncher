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

package com.modlauncher.gui;

import com.modlauncher.LauncherStructure;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ModLauncherFrame extends JFrame {

    public ModLauncherFrame() {
        super("ModLauncher by Eyeballcode V" + LauncherStructure.VERSION);
        add(ConsoleTab.tab);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension((int) (screenSize.width / 1.25F), (int) (screenSize.height / 1.5F)));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setupLoginFrame() {
        getContentPane().removeAll();
        setLayout(new GridLayout(1, 1));
        setPreferredSize(null);
        add(new AddAccountPanel(this));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width / 2, screenSize.height / 2));
        pack();
        repaint();
        setLocation(screenSize.width / 2 - getWidth() / 2,
                screenSize.height / 2 - getHeight() / 2);
    }

    public void setupActualFrame() {
        getContentPane().removeAll();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new LauncherTabs());
        add(new AccountsPanel(this));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width / 2, screenSize.height / 2));
        pack();
        repaint();
        setLocation(screenSize.width / 2 - getWidth() / 2,
                screenSize.height / 2 - getHeight() / 2);
    }
}
