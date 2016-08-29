package com.modlauncher.gui;

import com.modlauncher.logging.LauncherLogger;
import com.modlauncher.users.GameUserCache;
import com.modlauncher.util.AWTUtil;
import com.modlauncher.util.LoginListener;
import lib.mc.auth.Authenticator;
import lib.mc.player.AccessToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class AccountsPanel extends JPanel {

    public AccountsPanel(final JFrame parent) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        try {
            AccessToken token = GameUserCache.getUser();
            if (token != null) {
                boolean valid = Authenticator.validate(token);
                if (!valid) {
                    try {
                        LauncherLogger.logger.log("Refreshing access token...");
                        token = Authenticator.refresh(token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            final JLabel label = new JLabel("Logged in as " + (token == null ? "no one" : token.getPlayer().getName()));
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(label);
            JButton login = new JButton("Login");
            add(login);
            login.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AWTUtil.displayLogin(parent, new LoginListener() {
                        @Override
                        public void onLogin(AccessToken accessToken) {
                            try {
                                GameUserCache.setUser(accessToken);
                                label.setText("Logged in as " + accessToken.getPlayer().getName());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
