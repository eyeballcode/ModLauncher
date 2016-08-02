package lib.mc.library;

/**
 * A <code>{@link LibraryObjectInfo}</code> for MC Forge packages
 */
public class ForgeLibraryObjectInfo extends LibraryObjectInfo {

    private boolean isForgeLib;

    public ForgeLibraryObjectInfo(LibraryObjectInfo info, boolean isForgeLib) {
        super(info.getLibraryName(), info.getPackageName(), info.getVersion());
    }

    @Override
    public String toURL() {
        return super.toURL() + (isForgeLib ? ".pack.xz" : "");
    }
}
