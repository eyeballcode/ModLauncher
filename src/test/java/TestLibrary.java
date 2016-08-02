import lib.mc.library.*;
import lib.mc.libraryutil.LibraryDownloader;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;

public class TestLibrary {

    public static void main(String[] args) throws IOException {
        NativesRules nativesRules = new NativesRules(new JSONArray());
        System.out.println(nativesRules.getAllowed());
        System.out.println(nativesRules.getDisallowed());
        LibraryObject nativeMCLibraryObject = new NativeMCLibraryObject("net.java.jinput:jinput-platform:2.0.5", "7ff832a6eb9ab6a767f1ade2b548092d0fa64795", nativesRules);
        LibraryObject forgeLibraryObject = new ForgeLibraryObject("lzma:lzma:0.0.1", false);
        LibraryObject defaultMCLibraryObject = new DefaultMCLibraryObject("com.google.guava:guava:15.0", "ed727a8d9f247e2050281cb083f1c77b09dcb5cd");
        File dir = new File("libraries");
        dir.mkdir();
        LibraryDownloader.downloadLibrary(defaultMCLibraryObject, dir);
        LibraryDownloader.downloadLibrary(nativeMCLibraryObject, dir);
        LibraryDownloader.downloadLibrary(forgeLibraryObject, dir);
    }

}