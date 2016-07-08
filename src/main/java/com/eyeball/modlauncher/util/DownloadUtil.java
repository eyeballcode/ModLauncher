package com.eyeball.modlauncher.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadUtil {
    public static void hashedDownload(String sha1sum, String location, File name) {
        for (int t = 0; t < 10; t++) {
            downloadFile(location, name);
        }
    }

    private static void downloadFile(String location, File name) {
        URL website = null;
        try {
            website = new URL(location);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(name);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
