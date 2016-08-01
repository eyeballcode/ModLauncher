package lib.mc.library;

/**
 * A Library Object for standard minecraft packages.
 */
public class NativeMCLibraryObject extends LibraryObject {

    private String rawName;

    public NativeMCLibraryObject(String rawName) {
        this.rawName = rawName;
    }

    @Override
    public String getRawName() {
        return rawName;
    }

    @Override
    public String getHostServer() {
        return "https://libraries.minecraft.net/";
    }

    @Override
    public LibraryObjectInfo parseName() {
        return new NativeLibraryObjectInfo(super.parseName());
    }
}
