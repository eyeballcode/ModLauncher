/*
 * 	Copyright (C) 2016 Eyeballcode
 *
 * 	This program is free software: you can redistribute it and/or modify
 * 	it under the terms of the GNU General Public License as published by
 * 	the Free Software Foundation, either version 3 of the License, or
 * 	(at your option) any later version.
 *
 * 	This program is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 	GNU General Public License for more details.
 *
 * 	You should have received a copy of the GNU General Public License
 * 	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 	See LICENSE.MD for more details.
 */


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

    public static String wget(URL location) throws IOException {
        File tmpFile = new File("tmp-" + System.currentTimeMillis() + ".tmp");
        download(location, tmpFile);
        String data = FileIO.read(tmpFile);
        tmpFile.delete();
        return data;
    }

}
