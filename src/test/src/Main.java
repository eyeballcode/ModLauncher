import lib.mc.library.DefaultMCLibraryObject;
import lib.mc.library.ForgeLibraryObject;
import lib.mc.library.NativeMCLibraryObject;

public class Main {

    public static void main(String[] args) {
        DefaultMCLibraryObject defaultMCLibraryObject = new DefaultMCLibraryObject("org.lwjgl.lwjgl:lwjgl-platform:2.9.1-nightly-20130708-debug3");
        System.out.println(defaultMCLibraryObject.parseName().getLibraryName());
        ForgeLibraryObject forgeLibraryObject = new ForgeLibraryObject("org.lwjgl.lwjgl:lwjgl-platform:2.9.1-nightly-20130708-debug3");
        System.out.println(forgeLibraryObject.parseName().toURL());
        NativeMCLibraryObject nativeLibraryObject = new NativeMCLibraryObject("org.lwjgl.lwjgl:lwjgl-platform:2.9.1-nightly-20130708-debug3");
        System.out.println(nativeLibraryObject.parseName().toURL());
    }

}
