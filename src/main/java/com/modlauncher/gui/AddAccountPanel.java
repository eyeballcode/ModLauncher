package com.modlauncher.gui;

import com.modlauncher.LauncherStructure;
import com.modlauncher.users.GameUserCache;
import lib.mc.auth.Authenticator;
import lib.mc.player.AccessToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.net.UnknownHostException;

class AddAccountPanel extends JPanel {

    AddAccountPanel(final ModLauncherFrame modLauncherFrame, final boolean cancelExits) {
        JPanel loginDetailsContainer = new JPanel();
        JPanel username = new JPanel(new GridLayout(1, 2));
        JPanel password = new JPanel(new GridLayout(1, 2));
        final JLabel errorLabel = new JLabel();
        final JTextField usernameField = new JTextField();
        final JPasswordField passwordField = new JPasswordField();
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel usernameLabel = new JLabel("Username: ");
        username.add(usernameLabel);
        JLabel passwordLabel = new JLabel("Password: ");
        password.add(passwordLabel);
        usernameLabel.setLabelFor(usernameField);
        passwordLabel.setLabelFor(passwordField);
        usernameField.setSize(new Dimension(30, usernameField.getPreferredSize().height));
        passwordField.setSize(usernameField.getPreferredSize());
        usernameField.setColumns(20);
        passwordField.setColumns(20);
        username.add(usernameField);
        password.add(passwordField);
        loginDetailsContainer.setLayout(new BoxLayout(loginDetailsContainer, BoxLayout.Y_AXIS));
        loginDetailsContainer.add(username);
        loginDetailsContainer.add(password);
        JPanel control = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton done = new JButton("Login");
        final JButton close = new JButton("Cancel");
        control.add(done);
        control.add(close);
        modLauncherFrame.getRootPane().setDefaultButton(done);
        loginDetailsContainer.add(control);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cancelExits) {
                    modLauncherFrame.dispose();
                    System.exit(0);
                } else {
                    modLauncherFrame.setupActualFrame();
                }
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
                            LauncherStructure.isOffline = false;
                        } catch (UnknownHostException | SocketException e) {
                            errorLabel.setText("<html>UnknownHostException: Could not connect to Mojang login server. Please check your internet connection.</html>");
                            LauncherStructure.isOffline = true;
                        } catch (Exception ignored) {
//                            ignored.printStackTrace();
                            errorLabel.setText(ignored.getClass().getSimpleName() + ": " + ignored.getMessage());
                        }
                        done.setEnabled(true);
                        close.setEnabled(true);
                    }
                }.start();
            }
        });
        setLayout(new GridBagLayout());
        JPanel parent = new JPanel();
        JPanel errorContainer = new JPanel(new BorderLayout());
        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
        errorContainer.add(errorLabel, BorderLayout.CENTER);
        parent.add(errorContainer);
        parent.add(loginDetailsContainer);
        add(parent);
    }
}
