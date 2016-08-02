import lib.mc.library.ForgeLibraryObject;
import lib.mc.library.LibraryObject;
import lib.mc.library.NativesRules;
import lib.mc.libraryutil.LibraryDownloader;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        NativesRules nativesRules = new NativesRules(new JSONArray());
        System.out.println(nativesRules.getAllowed());
        System.out.println(nativesRules.getDisallowed());
//        LibraryObject libraryObject = new NativeMCLibraryObject("net.java.jinput:jinput-platform:2.0.5", "7ff832a6eb9ab6a767f1ade2b548092d0fa64795", nativesRules);
        LibraryObject libraryObject = new ForgeLibraryObject("org.scala-lang:scala-xml_2.11:1.0.2", true);
        System.out.println(libraryObject.parseName().getLibraryName());
        File dir = new File("libraries");
        dir.mkdir();
        LibraryDownloader.downloadLibrary(libraryObject, dir);
    }

}