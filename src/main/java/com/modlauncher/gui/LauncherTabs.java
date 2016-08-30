package com.modlauncher.gui;

import javax.swing.*;

class LauncherTabs extends JTabbedPane {


    LauncherTabs() {
        add("Modpacks", new ModpacksTab());
        add("Launcher Console", ConsoleTab.tab);
    }

}
