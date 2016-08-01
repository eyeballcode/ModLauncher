package lib.mc.library;

public class ForgeLibraryObject extends LibraryObject {

    private String rawName;

    public ForgeLibraryObject(String rawName) {
        this.rawName = rawName;
    }

    @Override
    public String getRawName() {
        return rawName;
    }

    @Override
    public String getHostServer() {
        return "https://files.minecraftforge.net/";
    }

    @Override
    public LibraryObjectInfo parseName() {
        LibraryObjectInfo info = super.parseName();
        return new ForgeLibraryObjectInfo(info);
    }
}
