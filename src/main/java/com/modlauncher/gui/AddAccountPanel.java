package com.modlauncher.gui;

import com.modlauncher.users.GameUserCache;
import lib.mc.auth.Authenticator;
import lib.mc.player.AccessToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class AddAccountPanel extends JPanel {

    AddAccountPanel(final ModLauncherFrame modLauncherFrame) {
        JPanel container = new JPanel();
        JPanel username = new JPanel(new GridLayout(2, 1));
        JPanel password = new JPanel(new GridLayout(2, 1));
        final JLabel errorLabel = new JLabel();
        final JTextField usernameField = new JTextField();
        final JPasswordField passwordField = new JPasswordField();
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel labelC = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelC.add(errorLabel);
        container.add(labelC);
        username.add(new JLabel("Username: "));
        password.add(new JLabel("Password: "));
        usernameField.setSize(new Dimension(30, usernameField.getPreferredSize().height));
        passwordField.setSize(usernameField.getPreferredSize());
        usernameField.setColumns(20);
        passwordField.setColumns(20);
        username.add(usernameField);
        password.add(passwordField);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(username);
        container.add(password);
        JPanel control = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton done = new JButton("Login");
        final JButton close = new JButton("Cancel");
        control.add(done);
        control.add(close);
        modLauncherFrame.getRootPane().setDefaultButton(done);
        container.add(control);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modLauncherFrame.dispose();
                System.exit(0);
            }
        });
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                done.setEnabled(false);
                close.setEnabled(false);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            AccessToken accessToken = Authenticator.login(usernameField.getText(), String.valueOf(passwordField.getPassword()), "MCLauncher");
                            GameUserCache.setUser(accessToken);
                            modLauncherFrame.setupActualFrame();
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                            errorLabel.setText(ignored.getMessage());
                            done.setEnabled(true);
                            close.setEnabled(true);
                        }
                    }
                }.start();
            }
        });
        setLayout(new GridBagLayout());
        add(container);
    }
}
