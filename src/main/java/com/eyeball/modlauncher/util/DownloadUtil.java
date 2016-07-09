package com.eyeball.modlauncher.util;

import com.jrutil.io.FileUtils;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DownloadUtil {

    public static void hashedDownload(String sha1sum, String location, File output, int tries) {
        if (output.exists()) {
            System.out.println("Skipping " + location);
            if (matchesSHA1(output, sha1sum)) return;
        }
        for (int t = 0; t < tries; t++) {
            System.out.println("Download " + location + " (Try " + (t + 1) + " / " + tries + ")");
            downloadFile(location, output);
            if (matchesSHA1(output, sha1sum))
                return;
        }
    }

    public static String calcSHA1(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (InputStream input = new FileInputStream(file)) {

            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            return new HexBinaryAdapter().marshal(sha1.digest()).toLowerCase();
        }
    }


    private static boolean matchesSHA1(File check, String sha1sum) {
        try {
            return calcSHA1(check).equals(sha1sum);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static void downloadFile(String location, File name) {
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

    public static String wget(String url) {
        try {
            File output = new File(FileUtils.getTmpDir(), new File(new URL(url).getFile()).getName());
            downloadFile(url, output);
            DataInputStream inputStream = new DataInputStream(new FileInputStream(output));
            byte[] bytes = new byte[inputStream.available()];
            inputStream.readFully(bytes);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
