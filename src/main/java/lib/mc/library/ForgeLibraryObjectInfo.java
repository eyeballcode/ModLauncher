package lib.mc.library;

/**
 * A <code>{@link LibraryObjectInfo}</code> for MC Forge packages
 */
public class ForgeLibraryObjectInfo extends LibraryObjectInfo {

    private boolean isForgeLib;

    /**
     * Constructs a ForgeLibraryObjectInfo object
     *
     * @param info       The base info
     * @param isForgeLib Is it a forge lib or can it be obtained from libraries.minecraft.net?
     */
    public ForgeLibraryObjectInfo(LibraryObjectInfo info, boolean isForgeLib) {
        super(info.getLibraryName(), info.getPackageName(), info.getVersion());
    }

    @Override
    public String toURL() {
        return super.toURL() + (isForgeLib ? ".pack.xz" : "");
    }
}
