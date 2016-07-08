package com.jrutil.ui;

import java.awt.event.KeyEvent;

public class KeyUtil {
    /**
     * Checks if a key is printable
     *
     * @param e The KeyEvent to check it's key
     * @return If it's a printable key
     */
    public static boolean isPrintableKey(KeyEvent e) {
        return e.getKeyChar() >= 33 && e.getKeyChar() <= 126 && !e.isControlDown() && !e.isMetaDown() && !e.isAltDown();
    }
}
