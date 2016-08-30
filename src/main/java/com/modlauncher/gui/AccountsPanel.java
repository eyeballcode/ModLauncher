package com.modlauncher.gui;

import com.modlauncher.users.GameUserCache;
import lib.mc.player.AccessToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AccountsPanel extends JPanel {

    public AccountsPanel(final ModLauncherFrame parent) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        try {
            AccessToken token = GameUserCache.getUser();
            final JLabel label = new JLabel("Logged in as " + (token == null ? "no one" : token.getPlayer().getName()));
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(label);
            JButton login = new JButton("Login");
            add(login);
            login.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.getContentPane().removeAll();
                    parent.add(new AddAccountPanel(parent));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
