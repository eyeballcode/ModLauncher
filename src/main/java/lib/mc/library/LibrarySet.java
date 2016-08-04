/*
 * 	Copyright (C) 2016 Eyeballcode
 *
 * 	This program is free software: you can redistribute it and/or modify
 * 	it under the terms of the GNU General Public License as published by
 * 	the Free Software Foundation, either version 3 of the License, or
 * 	(at your option) any later version.
 *
 * 	This program is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 	GNU General Public License for more details.
 *
 * 	You should have received a copy of the GNU General Public License
 * 	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 	See LICENSE.MD for more details.
 */

package lib.mc.library;

import lib.mc.libraryutil.LibraryDownloader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class LibrarySet {

    private ArrayList<LibraryObject> libraries = new ArrayList<>();

    /**
     * Constructs a Library Set.
     * @param libraries The array of libraries from the JSON. It will try to determine the library type (Native, Forge, Jar)
     */
    public LibrarySet(JSONArray libraries) {
        for (Object o : libraries) {
            JSONObject library = (JSONObject) o;
            if (!library.has("name"))
                throw new IllegalArgumentException("Invalid JSON");
            if (library.has("natives")) {
                NativesRules nativesRules = new NativesRules(library.has("rules") ? library.getJSONArray("rules") : new JSONArray("[]"));
                ExtractRules extractRules = new ExtractRules(library.getJSONObject("extract"));
                this.libraries.add(new NativeMCLibraryObject(library.getString("name"), library.getString("sha1"), nativesRules, extractRules));
            } else if (!library.has("download") && (library.has("url") || library.has("serverreq") || library.has("clientreq") || library.has("checksum"))) {
                this.libraries.add(new ForgeLibraryObject(library.getString("name"), library.has("url")));
            } else {
                this.libraries.add(new DefaultMCLibraryObject(library.getString("name"), library.getString("sha1")));
            }
        }
    }

    /**
     * Download all the libraries to a given folder
     * @param to The folder to download to
     * @throws IOException If an IO operation failed
     */
    public void downloadAll(File to) throws IOException {
        for (LibraryObject libraryObject : libraries) {
            LibraryDownloader.downloadLibrary(libraryObject, to);
        }
    }

}
