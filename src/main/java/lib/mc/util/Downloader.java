package lib.mc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader {

    public static boolean download(URL location, File output) throws IOException {
        if (output.exists()) return false;
        ReadableByteChannel rbc = Channels.newChannel(location.openStream());
        FileOutputStream fos = new FileOutputStream(output);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        return true;
    }

    public static int sha1Download(URL location, File outputFile, String sha1Sum, int tries) throws IOException {
        for (int i = 1; i <= tries; i++) {
            download(location, outputFile);
            if (ChecksumUtils.calcSHA1Sum(outputFile).equals(sha1Sum)) {
                return i;
            }
        }
        return -1;
    }
}
