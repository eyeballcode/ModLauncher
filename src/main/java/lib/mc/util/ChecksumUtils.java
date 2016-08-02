package lib.mc.util;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtils {

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

    public static String calcSHA1Sum(File input) throws IOException {
        return calcSHA1Sum(new FileInputStream(input));
    }

}
