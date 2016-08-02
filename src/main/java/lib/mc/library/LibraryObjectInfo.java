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


package lib.mc.library;

/**
 * A class containing data about a package
 */
public class LibraryObjectInfo {

    public String libraryName, packageName, version;

    protected LibraryObjectInfo(String libraryName, String packageName, String version) {
        this.libraryName = libraryName;
        this.packageName = packageName;
        this.version = version;
    }

    /**
     * Get a simple name
     *
     * @return The simple name
     */
    public String getLibraryName() {
        return libraryName;
    }

    /**
     * Get the package name
     *
     * @return The package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Get the version
     *
     * @return The version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets a URL referring to the download URL of the package
     *
     * @return The download URL
     */
    public String toURL() {
        return getPackageName().replaceAll("\\.", "/") + "/" + getLibraryName() + "/" + getVersion() + "/" + getLibraryName() + "-" + getVersion() + ".jar";
    }

    /**
     * Gets the URL containing the package's SHA1SUM.
     *
     * @return The URL
     */
    public String getSHA1URL() {
        return toURL() + ".sha1";
    }

    @Override
    public String toString() {
        return getPackageName() + ":" + getLibraryName() + ":" + getVersion();
    }
}
