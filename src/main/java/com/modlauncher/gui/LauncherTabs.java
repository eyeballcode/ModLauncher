package com.modlauncher.gui;

import javax.swing.*;

public class LauncherTabs extends JTabbedPane {

    public LauncherTabs() {
        add("Modpacks", new ModpacksTab());
    }

}
