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

import java.util.regex.Pattern;

public class Utils {

    /**
     * Creates a Java compatible UUID from the hyphenless minecraft UUIDS
     *
     * @param rawUUID The minecraft UUID
     * @return The Java UUID
     */
    public static String parseUUID(String rawUUID) {
        Pattern pattern = Pattern.compile("^(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})$");
        return pattern.matcher(rawUUID).replaceAll("$1-$2-$3-$4-$5");
    }

    public enum OS {
        WINDOWS, MACOSX, LINUX
    }

    public static class OSUtils {

        /**
         * Gets the OS
         * @return The current OS
         */
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
