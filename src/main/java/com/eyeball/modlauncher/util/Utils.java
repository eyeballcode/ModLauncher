package com.eyeball.modlauncher.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Utils {
    public static String noise() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }


    public static void extractZip(File outputDir, File zipFile) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<? extends ZipEntry> entries = zip.entries();
        int numberOfFiles = EnumerationUtils.count(zip.entries());
        float count = 0F;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                new File(outputDir, entry.getName()).mkdirs();
            } else {
                File file = new File(outputDir, entry.getName());
                if (file.exists()) {
                    long required = entry.getSize(),
                            existing = file.length();
                    if (required == existing) {
                        count++;
                        System.out.println("Progress: " + count / numberOfFiles * 100 + "% [Skipped " + entry.getName() + "]");
                        continue;
                    }
                }

                InputStream input = zip.getInputStream(entry);
                FileOutputStream outputStream = new FileOutputStream(file);
                ReadableByteChannel rbc = Channels.newChannel(input);
                outputStream.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                count++;
                System.out.println("Progress: " + count / numberOfFiles * 100 + "% [Unzipped " + entry.getName() + "]");
            }
        }
    }
}
