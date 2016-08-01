package lib.mc.library;

public class LibraryObjectInfo {

    public String libraryName, packageName, version;

    protected LibraryObjectInfo(String libraryName, String packageName, String version) {
        this.libraryName = libraryName;
        this.packageName = packageName;
        this.version = version;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersion() {
        return version;
    }

    public String toURL() {
        return "/" + getPackageName() + "/" + getLibraryName() + "/" + getVersion() + "/" + getLibraryName() + "-" + getVersion() + ".jar";
    }

    public String getSHA1URL() {
        return toURL() + ".sha1";
    }

    @Override
    public final String toString() {
        return getPackageName() + ":" + getLibraryName() + ":" + getVersion();
    }
}
