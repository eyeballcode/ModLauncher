package com.modlauncher.gui;

import com.modlauncher.modpack.ModPack;
import com.modlauncher.modpack.ModPackListItem;
import com.modlauncher.modpack.ModPackListRender;
import com.modlauncher.util.FileUtil;
import com.modlauncher.util.ModPackUtil;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

class ModpacksTab extends JPanel {

    ModpacksTab() {
        try {
            setLayout(new GridLayout(1, 2));
            JSONObject modpacksData = new JSONObject(new JSONTokener(new FileInputStream(new File(FileUtil.getMCLauncherDir(), "modpacks.json")))).getJSONObject("modpacks");
            final JList<ModPackListItem> list = new JList<>();
            final JEditorPane modpackData = new JEditorPane("text/html", "<html><center><h1>Please select a modpack first.</h1></center></html>");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            modpackData.setPreferredSize(new Dimension(screenSize.width / 3, screenSize.height / 2));
            modpackData.setEditable(false);
            modpackData.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Label.background").darker()));
            list.setCellRenderer(new ModPackListRender<ModPackListItem>());
            Vector<ModPackListItem> modpacksList = new Vector<>();
            for (String name : modpacksData.keySet()) {
                JSONObject modpackJSON = modpacksData.getJSONObject(name);
                ModPackListItem item = new ModPackListItem(modpackJSON, name);
                modpacksList.add(item);
            }
            list.setListData(modpacksList);
            list.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    new Thread() {
                        @Override
                        public void run() {
                            ModPackListItem item = list.getSelectedValue();
                            try {
                                modpackData.setText("<html><center><h2>Loading " + item.getName() + "...</h2></center></html>");
                                JSONObject modpackDataJSON = ModPackUtil.cacheModpack(item.getName());
                                ModPack modPack = new ModPack(item.getName(), modpackDataJSON);
                                modpackData.setText(modPack.genDesc());
                                interrupt();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                modpackData.setText("<html><center><h2>Could not load Modpack data! " + e1.getMessage() + "</h2></center></html>");
                            }
                        }
                    }.start();
                }
            });
            add(new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
            add(new JScrollPane(modpackData, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        } catch (FileNotFoundException e) {
            // Won't happen, we downloaded assets
        }

    }

}
