package com.eyeball.modlauncher.util;

import org.tukaani.xz.XZInputStream;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

public class ForgeLibUtil {
    public static void downloadForgeLib(String url, File outputFile) throws IOException, NoSuchAlgorithmException {
        String rawPath = outputFile.getAbsolutePath();
        File raw = outputFile,
                packFile = new File(rawPath.substring(0, rawPath.length() - 3)),
                jarFile = new File(rawPath.substring(0, rawPath.length() - 8));
        if (packFile.exists()) packFile.delete();
        if (jarFile.exists()) jarFile.delete();
        DownloadUtil.downloadFile(url, outputFile);
        String end, data, checksums;
        byte[] decompressed = readFully(new XZInputStream(new FileInputStream(outputFile)));

        data = new String(decompressed);
        end = data.substring(data.length() - 4);
        if (!end.equals("SIGN")) {
            System.out.println("Could not unpack as signature missing!");
            return;
        }
        int x = decompressed.length,
                lengthWeWant = decompressed[x - 8] & 255 | (decompressed[x - 7] & 255) << 8 | (decompressed[x - 6] & 255) << 16 | (decompressed[x - 5] & 255) << 24;
        FileOutputStream writePack = new FileOutputStream(packFile);
        writePack.write(decompressed, 0, decompressed.length - lengthWeWant - 8);
        writePack.close();
        System.out.println("Decompressed xz!");
        checksums = new String(decompressed, decompressed.length - lengthWeWant - 8, lengthWeWant);
        ChecksumList list = new ChecksumList(checksums);

        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(jarFile));
        Pack200.newUnpacker().unpack(packFile, jarOutputStream);
        jarOutputStream.close();
        System.out.println("Unpacked jar!");
        if (!verifyJar(jarFile, list)) {
            System.out.println("Jar checksums for " + jarFile.getName() + " incorrect, redownloading...");
            outputFile.delete();
            downloadForgeLib(url, outputFile);
        } else {
            System.out.println("Jar checksums for " + jarFile.getName() + " verified!");
        }
    }

    private static boolean verifyJar(File jarFile, ChecksumList list) throws IOException, NoSuchAlgorithmException {
        JarInputStream inputStream = new JarInputStream(new FileInputStream(jarFile), true);
        JarEntry entry = inputStream.getNextJarEntry();
        while (entry != null) {
            if (entry.isDirectory()) {
                entry = inputStream.getNextJarEntry();
                continue;
            }

            String name = entry.getName();
            String checksums = list.get(name);
            byte[] data = readFully(inputStream);
            String sha = DownloadUtil.calcSHA1(data);
            if (!sha.equals(checksums)) return false;
            entry = inputStream.getNextJarEntry();
        }
        return true;
    }

    public static byte[] readFully(InputStream stream) throws IOException {
        int len;
        byte[] data = new byte[4096];
        ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
        do {
            if ((len = stream.read(data)) <= 0) continue;
            entryBuffer.write(data, 0, len);
        } while (len != -1);
        return entryBuffer.toByteArray();
    }


}
