package lib.mc.library;

public class DefaultMCLibraryObject extends LibraryObject {

    private String rawName;

    public DefaultMCLibraryObject(String rawName) {
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

}
