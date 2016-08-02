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

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtils {

    private static final String BASE64_CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    /**
     * Calculates the SHA1SUM from the given bytes
     *
     * @param bytes The bytes
     * @return The SHA1SUM
     * @throws IOException If an error occurred. Should not happen
     */
    public static String calcSHA1Sum(byte[] bytes) throws IOException {
        return calcSHA1Sum(new ByteArrayInputStream(bytes));
    }

    private static String calcSHA1Sum(InputStream input) throws IOException {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            return new HexBinaryAdapter().marshal(sha1.digest()).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calculates the SHA1SUM for the given file
     *
     * @param input The file
     * @return The SHA1SUM of the file
     * @throws IOException If an error occurred reading the file
     */
    public static String calcSHA1Sum(File input) throws IOException {
        return calcSHA1Sum(new FileInputStream(input));
    }

    /**
     * Un-base64s the given string
     *
     * @param base64 The base64 string
     * @return The original string
     */
    public static String fromBase64(String base64) {

        char[] chars = base64.toCharArray();
        if (base64.length() % 4 != 0) {
            throw new IllegalArgumentException("Invalid base64 base64");
        }
        byte decoded[] = new byte[((base64.length() * 3) / 4) - (base64.indexOf('=') > 0 ? (base64.length() - base64.indexOf('=')) : 0)];
        char[] inChars = base64.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4) {
            b[0] = BASE64_CODES.indexOf(inChars[i]);
            b[1] = BASE64_CODES.indexOf(inChars[i + 1]);
            b[2] = BASE64_CODES.indexOf(inChars[i + 2]);
            b[3] = BASE64_CODES.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64) {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64) {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }

        return new String(decoded);
    }

    /**
     * Converts a string to base64
     *
     * @param original The original string
     * @return The string in base64
     */
    public static String toBase64(String original) {
        byte[] bytes = original.getBytes();
        StringBuilder out = new StringBuilder((bytes.length * 4) / 3);
        int b;
        for (int i = 0; i < bytes.length; i += 3) {
            b = (bytes[i] & 0xFC) >> 2;
            out.append(BASE64_CODES.charAt(b));
            b = (bytes[i] & 0x03) << 4;
            if (i + 1 < bytes.length) {
                b |= (bytes[i + 1] & 0xF0) >> 4;
                out.append(BASE64_CODES.charAt(b));
                b = (bytes[i + 1] & 0x0F) << 2;
                if (i + 2 < bytes.length) {
                    b |= (bytes[i + 2] & 0xC0) >> 6;
                    out.append(BASE64_CODES.charAt(b));
                    b = bytes[i + 2] & 0x3F;
                    out.append(BASE64_CODES.charAt(b));
                } else {
                    out.append(BASE64_CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(BASE64_CODES.charAt(b));
                out.append("==");
            }
        }

        return out.toString();
    }

}
