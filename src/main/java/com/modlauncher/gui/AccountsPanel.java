package com.modlauncher.gui;

import com.modlauncher.users.GameUserCache;
import com.modlauncher.util.ModPackUtil;
import lib.mc.except.NoSuchVersionException;
import lib.mc.player.AccessToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class AccountsPanel extends JPanel {

    private JProgressBar progressBar = new JProgressBar();

    AccountsPanel(final ModLauncherFrame parent) {
        try {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            AccessToken token = GameUserCache.getUser();
            final JLabel label = new JLabel("Logged in as " + (token == null ? "no one" : token.getPlayer().getName()));
            JPanel progressBarContainer = new JPanel(new BorderLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            GridBagLayout gridBagLayout = new GridBagLayout();
            JPanel loginContainer = new JPanel(gridBagLayout);

            JButton playButton = new JButton("Launch MC");
            playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        ModPackUtil.downloadModpack(ModPackUtil.getActiveModpack(), progressBar);
                    } catch (IOException | NoSuchVersionException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(5, 0, 5, 5);
            constraints.gridx = 0;
            constraints.weightx = .2;

            loginContainer.add(label, constraints);
            JButton login = new JButton("Login");
            constraints.gridx = 1;

            loginContainer.add(login, constraints);
            accountPanel.add(loginContainer);
            login.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.setupLoginFrame(false);
                }
            });
            progressBarContainer.add(progressBar, BorderLayout.CENTER);
            JPanel playContainer = new JPanel(new GridBagLayout());
            playContainer.add(playButton);
            add(playContainer);
            add(progressBarContainer);
            add(accountPanel);
        } catch (Exception e) {
            e.printStackTrace();
            parent.setupLoginFrame(true);
        }
    }

}
