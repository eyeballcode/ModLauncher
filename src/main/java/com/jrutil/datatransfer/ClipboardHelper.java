package com.jrutil.datatransfer;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class ClipboardHelper {

    /**
     * Gets the string contents of the clipboard
     *
     * @return The string contents, or null if it is empty or not a string.
     */
    public static String getStringClipboardContents() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the image contents of the clipboard
     *
     * @return The image contents in a {@link BufferedImage}, or null if there is nothing or it is not a string
     */
    public static BufferedImage getImageClipboardContents() {
        try {
            return (BufferedImage) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<File> getFileClipboardContents() {
        try {
            return (ArrayList<File>) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.javaFileListFlavor);
        } catch (Exception e) {
            return null;
        }
    }


}
