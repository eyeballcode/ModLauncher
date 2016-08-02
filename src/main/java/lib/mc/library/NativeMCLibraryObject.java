package lib.mc.library;

/**
 * A Library Object for standard minecraft packages.
 */
public class NativeMCLibraryObject extends LibraryObject {

    private String rawName, sha1Sum;

    private NativesRules rules;

    public NativeMCLibraryObject(String rawName, String sha1Sum, NativesRules rules) {
        this.rawName = rawName;
        this.rules = rules;
        this.sha1Sum = sha1Sum;
    }

    public String getSHA1Sum() {
        return sha1Sum;
    }

    public NativesRules getRules() {
        return rules;
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
