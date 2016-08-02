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


package lib.mc.assets;

import lib.mc.except.InvalidPathException;
import lib.mc.util.Downloader;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AssetSet {

    ArrayList<Asset> assets = new ArrayList<>();

    /**
     * Constructs an <code>AssetSet</code>, which contains many assets.
     *
     * @param rawJSON The raw json as provided by minecraft.
     */
    public AssetSet(JSONObject rawJSON) {
        if (rawJSON.has("objects")) {
            JSONObject objects = rawJSON.getJSONObject("objects");
            for (String path : objects.keySet()) {
                JSONObject assetJSON = objects.getJSONObject(path);
                String hash = assetJSON.getString("hash");
                Asset asset = new Asset(path, hash);
                assets.add(asset);
            }
        }
    }

    /**
     * Download the entire AssetSet
     *
     * @param outputDir the output directory.
     * @throws InvalidPathException If the path is invalid
     * @throws IOException          If an error occurred while downloading.
     */
    public void download(File outputDir) throws InvalidPathException, IOException {
        if (!outputDir.isDirectory()) {
            throw new InvalidPathException("Path must be a directory");
        }
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        for (Asset asset : assets) {
            String hash = asset.getSHA1Sum();
            String id = hash.substring(0, 2);
            File outputFile = new File(new File(outputDir, id), hash);
            outputFile.getParentFile().mkdirs();
            try {
                Downloader.sha1Download(new URL("http://resources.download.minecraft.net/" + id + "/" + hash), outputFile, hash, 5);
            } catch (MalformedURLException exception) {
                //Should never happen
            }
        }
    }

}
