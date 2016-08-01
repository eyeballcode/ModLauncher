package lib.mc.library;

public class ForgeLibraryObjectInfo extends LibraryObjectInfo {
    public ForgeLibraryObjectInfo(LibraryObjectInfo info) {
        super(info.getLibraryName(), info.getPackageName(), info.getVersion());
    }

    @Override
    public String toURL() {
        return super.toURL() + ".tar.xz";
    }
}
