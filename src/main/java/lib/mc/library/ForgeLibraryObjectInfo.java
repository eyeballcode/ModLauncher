package lib.mc.library;

/**
 * A <code>{@link LibraryObjectInfo}</code> for MC Forge packages
 */
public class ForgeLibraryObjectInfo extends LibraryObjectInfo {

    public ForgeLibraryObjectInfo(LibraryObjectInfo info) {
        super(info.getLibraryName(), info.getPackageName(), info.getVersion());
    }

    @Override
    public String toURL() {
        return super.toURL() + ".pack.xz";
    }
}
