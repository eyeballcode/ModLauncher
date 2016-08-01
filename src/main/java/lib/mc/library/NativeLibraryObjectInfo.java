package lib.mc.library;

import lib.mc.util.Utils;

/**
 * A class containing data about a package
 */
public class NativeLibraryObjectInfo extends LibraryObjectInfo {

    public String libraryName, packageName, version, nativesName;

    protected NativeLibraryObjectInfo(LibraryObjectInfo parent) {
        super(parent.getLibraryName(), parent.getPackageName(), parent.getVersion());
        this.libraryName = parent.getLibraryName();
        this.packageName = parent.getPackageName();
        this.version = parent.getVersion();
        this.nativesName = "natives-" + Utils.OSUtils.getOS().name().toLowerCase();
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
     * @return The URL referring to the download URL of the package
     */
    public String toURL() {
        return "/" + getPackageName() + "/" + getLibraryName() + "/" + getVersion() + "/" + getLibraryName() + "-" + getVersion() + "-" + getNativesName() + ".jar";
    }

    /**
     * Gets the URL containing the package's SHA1SUM.
     *
     * @return The URL containing the package's SHA1SUM.
     */
    public String getSHA1URL() {
        return toURL() + ".sha1";
    }

    @Override
    public String toString() {
        return getPackageName() + ":" + getLibraryName() + ":" + getVersion();
    }

    public String getNativesName() {
        return nativesName;
    }
}
