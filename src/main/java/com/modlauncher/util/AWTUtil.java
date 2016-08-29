package com.modlauncher.util;

import com.modlauncher.logging.LauncherLogger;
import com.modlauncher.users.GameUserCache;
import lib.mc.auth.Authenticator;
import lib.mc.player.AccessToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class AWTUtil {

    public static void setLAF() {
        try {
            if (UIManager.getSystemLookAndFeelClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
                LauncherLogger.logger.log("Setting LAF to System LAF has no effect, searching...");
                // In LXDE or XFCE this happens...
                // So we check if the previous LAF is equals to the current one.
                HashMap<String, String> founds = new HashMap<>();
                String[] preferences = {"GTK+", "Nimbus"};

                for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
                    founds.put(lookAndFeelInfo.getName(), lookAndFeelInfo.getClassName());
                }
                boolean found = false;
                for (String pref : preferences) {
                    if (founds.containsKey(pref)) {
                        LauncherLogger.logger.log("Found " + pref + ", using it");
                        UIManager.setLookAndFeel(founds.get(pref));
                        found = true;
                        break;
                    }
                }
                if (!found) LauncherLogger.logger.log("Could not find any preferred LAFs, using Metal.");
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Never happen
        }
    }


    public static void displayLogin(JFrame parent, final LoginListener listener) {
        final JDialog dialog = new JDialog(parent);
        dialog.setTitle("Login");
        JPanel username = new JPanel(new GridLayout(2, 1));
        JPanel password = new JPanel(new GridLayout(2, 1));
        final JTextField usernameField = new JTextField();
        final JPasswordField passwordField = new JPasswordField();
        username.add(new JLabel("Username: "));
        password.add(new JLabel("Password: "));
        username.add(usernameField);
        password.add(passwordField);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.add(username);
        dialog.add(password);
        JPanel control = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton done = new JButton("Login");
        JButton close = new JButton("Cancel");
        control.add(done);
        control.add(close);
        dialog.getRootPane().setDefaultButton(done);
        dialog.add(control);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AccessToken accessToken = Authenticator.login(usernameField.getText(), String.valueOf(passwordField.getPassword()), "MCLauncher");
                    listener.onLogin(accessToken);
                    dialog.dispose();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        });
        dialog.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(screenSize.width / 2 - dialog.getWidth() / 2, screenSize.height / 2 - dialog.getHeight() / 2);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }


}

