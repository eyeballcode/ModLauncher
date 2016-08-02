package lib.mc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * A simple class to download files.
 */
public class Downloader {

    /**
     * Download a no-checksum file
     *
     * @param location The URL of the online file
     * @param output   The output file to write to
     * @return If the file was downloaded
     * @throws IOException If an error occurred while downloading
     */
    public static boolean download(URL location, File output) throws IOException {
        if (output.exists()) return false;
        ReadableByteChannel rbc = Channels.newChannel(location.openStream());
        FileOutputStream fos = new FileOutputStream(output);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        return true;
    }

    /**
     * Downloads a SHA1-checksumed file
     * <p>
     * If <code>outputFile</code> exists, it will check whether its SHA1SUM matches.
     * If it does, it will not redownload.
     * Otherwise, it will redownload the file.
     *
     * @param location   The URL of the online file
     * @param outputFile The output file to write to
     * @param sha1Sum    The SHA1SUM of the file being downloaded
     * @param tries      The number of attempts before giving up.
     * @return The number of tries needed to download. -1 if the file was not downloaded properly
     * @throws IOException If an error occurred while downloading
     */
    public static int sha1Download(URL location, File outputFile, String sha1Sum, int tries) throws IOException {
        if (outputFile.exists()) {
            if (ChecksumUtils.calcSHA1Sum(outputFile).equals(sha1Sum)) {
                return 0;
            }
            outputFile.delete();
        }

        for (int i = 1; i <= tries; i++) {
            download(location, outputFile);
            if (ChecksumUtils.calcSHA1Sum(outputFile).equals(sha1Sum)) {
                return i;
            }
        }
        return -1;
    }
}
