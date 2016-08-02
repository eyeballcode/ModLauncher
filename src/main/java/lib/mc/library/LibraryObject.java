package lib.mc.library;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LibraryObject {

    /**
     * Gets the raw name of the package.
     * <p>
     * Examples: <code>org.lwjgl.lwjgl:lwjgl-platform:2.9.1-nightly-20130708-debug3</code>
     *
     * @return The raw name of the package
     */
    public abstract String getRawName();

    /**
     * Gets the host server for the package.
     * <p>
     * Examples: <code>https://libraries.minecraft.net</code>
     *
     * @return The host server for the package.
     */
    public abstract String getHostServer();

    /**
     * Parse the raw name of the package into an object storing the data.
     *
     * @return A <code>{@link LibraryObjectInfo}</code> containing info about the package
     */
    public LibraryObjectInfo parseName() {
        Pattern regex = Pattern.compile("^([^:]+):([^:]+):(.+)$");
        Matcher matcher = regex.matcher(getRawName());
        if (matcher.matches()) {
            MatchResult result = matcher.toMatchResult();
            String packageName = result.group(1),
                    libraryName = result.group(2),
                    version = result.group(3);
            return new LibraryObjectInfo(libraryName, packageName, version);
        }
        return null;
    }

    public abstract String getSHA1Sum();

}
