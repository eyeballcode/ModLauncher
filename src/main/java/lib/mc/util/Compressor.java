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

import lib.mc.library.ExtractRules;
import lib.mc.library.ForgeLibraryChecksums;
import org.tukaani.xz.XZInputStream;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Compressor {

    public static void unzip(File file, ExtractRules rules, File to) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        ZipEntry current;
        while (entries.hasMoreElements()) {
            current = entries.nextElement();
            if (rules.isExcluded(current.getName())) continue;
            if (current.isDirectory()) new File(to, current.getName()).mkdirs();
            InputStream inputStream = zipFile.getInputStream(current);
            ReadableByteChannel rbc = Channels.newChannel(inputStream);
            FileOutputStream fos = new FileOutputStream(new File(to, current.getName()));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        }
    }

    public static void unxz(File file, File to) throws IOException {
        XZInputStream inputStream = new XZInputStream(new FileInputStream(file));
        ReadableByteChannel rbc = Channels.newChannel(inputStream);
        FileOutputStream fos = new FileOutputStream(to);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
    }

    public static File stripChecksums(File unxzedFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(unxzedFile);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();
        if (!new String(bytes, bytes.length - 4, 4).equals("SIGN")) {
            throw new IOException("Invalid file footer");
        }
        int x = bytes.length;
        int actualLength = bytes[x - 8] & 255 | (bytes[x - 7] & 255) << 8 | (bytes[x - 6] & 255) << 16 | (bytes[x - 5] & 255) << 24;

        File tmp = new File(unxzedFile.getAbsolutePath().substring(0, unxzedFile.getAbsolutePath().lastIndexOf(".")) + ".art");
        FileOutputStream fos = new FileOutputStream(tmp);
        fos.write(bytes, 0, x - actualLength - 8);
        fos.close();
        return tmp;
    }


    public static ForgeLibraryChecksums getChecksums(File unxzedFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(unxzedFile);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();
        if (!new String(bytes, bytes.length - 4, 4).equals("SIGN")) {
            throw new IOException("Invalid file footer");
        }
        int x = bytes.length;
        int actualLength = bytes[x - 8] & 255 | (bytes[x - 7] & 255) << 8 | (bytes[x - 6] & 255) << 16 | (bytes[x - 5] & 255) << 24;
        String checksums = new String(bytes, x - actualLength - 8, actualLength);
        return new ForgeLibraryChecksums(checksums);
    }

    public static void unpack(File file, File out) throws IOException {
        Pack200.newUnpacker().unpack(file, new JarOutputStream(new FileOutputStream(out)));
    }

    public static boolean verify(File file, ForgeLibraryChecksums checksums) throws IOException {
        JarInputStream in = new JarInputStream(new FileInputStream(file));
        in.close();
        return true;
    }

}
