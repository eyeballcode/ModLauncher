package lib.mc.libraryutil;

import lib.mc.except.InvalidPathException;
import lib.mc.library.LibraryObject;
import lib.mc.library.LibraryObjectInfo;
import lib.mc.library.NativeMCLibraryObject;
import lib.mc.util.Downloader;
import lib.mc.util.Utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class to help download libraries, and unpack them as needed.
 */
public class LibraryDownloader {

    /**
     * Downloads the given library to a <code>File</code>
     *
     * @param library The library to download
     * @param to      The folder to download to
     */
    public static void downloadLibrary(LibraryObject library, File to) throws InvalidPathException, IOException {
        if (!to.isDirectory()) {
            throw new InvalidPathException("Output path must be a directory, not a file");
        }
        if (!to.exists()) {
            to.mkdir();
        }
        LibraryObjectInfo info = library.parseName();
        String url = library.getHostServer() + info.toURL();
        File packagedFolder = new File(to, new File(info.toURL()).getParentFile().getPath());
        File outputFile;

        if (library instanceof NativeMCLibraryObject) {
            NativeMCLibraryObject nativeMCLibraryObject = (NativeMCLibraryObject) library;
            if (!nativeMCLibraryObject.getRules().isAllowed(Utils.OSUtils.getOS())) return;
            outputFile = new File(packagedFolder, new File(info.toURL()).getName());
        } else {
            outputFile = new File(packagedFolder, new File(info.toURL()).getName());
        }
        packagedFolder.mkdirs();
        String sha1Sum = library.getSHA1Sum();
        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException ignored) {
            // Should never happen.
            return;
        }
        if (sha1Sum == null) {
            Downloader.download(urlObj, outputFile);
        } else {
            int tries = Downloader.sha1Download(urlObj, outputFile, sha1Sum, 5);
            if (tries == -1) {
                throw new IOException("Could not download " + info.getLibraryName());
            }
        }

    }

}
