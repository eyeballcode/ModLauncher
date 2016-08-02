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
