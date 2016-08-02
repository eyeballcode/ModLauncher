package lib.mc.library;

/**
 * A Library Object for standard minecraft packages.
 */
public class DefaultMCLibraryObject extends LibraryObject {

    private String rawName, sha1sum;

    public DefaultMCLibraryObject(String rawName, String sha1sum) {
        this.rawName = rawName;
        this.sha1sum = sha1sum;
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
    public String getSHA1Sum() {
        return sha1sum;
    }

}
