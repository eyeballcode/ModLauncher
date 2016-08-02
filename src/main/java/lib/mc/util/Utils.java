package lib.mc.util;

public class Utils {

    public enum OS {
        WINDOWS, MACOSX, LINUX
    }

    public static class OSUtils {

        public static OS getOS() {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win"))
                return OS.WINDOWS;
            else if (os.contains("mac"))
                return OS.MACOSX;
            else if (os.contains("linux"))
                return OS.LINUX;
            else return OS.WINDOWS;
        }
    }
}
