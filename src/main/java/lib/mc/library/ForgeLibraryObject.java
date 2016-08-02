package lib.mc.library;

/**
 * A Forge Library Object, similar to <code>{@link DefaultMCLibraryObject}</code>.
 */
public class ForgeLibraryObject extends LibraryObject {

    private String rawName;
    private boolean isForgeLib;

    public ForgeLibraryObject(String rawName, boolean isForgeLib) {
        this.rawName = rawName;
        this.isForgeLib = isForgeLib;
    }

    @Override
    public String getRawName() {
        return rawName;
    }

    @Override
    public String getHostServer() {
        if (isForgeLib)
            return "http://files.minecraftforge.net/maven/";
        else
            return "https://libraries.minecraft.net/";
    }

    @Override
    public LibraryObjectInfo parseName() {
        LibraryObjectInfo info = super.parseName();
        return new ForgeLibraryObjectInfo(info);
    }

    @Override
    public String getSHA1Sum() {
        return null;
    }
}
