package com.modlauncher.gui;

import com.modlauncher.logging.ILogTarget;
import com.modlauncher.logging.LauncherLogger;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class ConsoleTab extends JPanel {

    private JTextPane textPane = new JTextPane();

    public static ConsoleTab tab;

    public ConsoleTab() {
        setLayout(new GridLayout(1, 1));
        add(textPane);
        textPane.setEditable(false);
        LauncherLogger.logger.addTarget(new WindowedLogger());
    }

    private class WindowedLogger implements ILogTarget {

        private void appendToPane(final String msg, final Color c) {
            try {
                textPane.getStyledDocument().insertString(textPane.getText().length(), msg + "\n", SimpleAttributeSet.EMPTY);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
                    StyleContext sc = StyleContext.getDefaultStyleContext();
                    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

                    aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
                    aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
//
//                    int len = textPane.getDocument().getLength();
//                    textPane.setCharacterAttributes(aset, false);
//                    try {
//                        textPane.getStyledDocument().insertString(len, msg + "\n", aset);
//                    } catch (BadLocationException ignored) {
//                    }
        }

        @Override
        public void log(Object message) {
            appendToPane(message.toString(), Color.BLACK);
        }

        @Override
        public void error(Object message) {
            appendToPane(message.toString(), Color.RED);
        }
    }

}
