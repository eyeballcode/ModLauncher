/*
 * 	Copyright (C) 2016 Eyeballcode
 *
 * 	This program is free software: you can redistribute it and/or modify
 * 	it under the terms of the GNU General Public License as published by
 * 	the Free Software Foundation, either version 3 of the License, or
 * 	(at your option) any later version.
 *
 * 	This program is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the ied warranty of
 * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 	GNU General Public License for more details.
 *
 * 	You should have received a copy of the GNU General Public License
 * 	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 	See LICENSE.MD for more details.
 */

package lib.mc.versions;

import lib.mc.except.NoSuchVersionException;
import lib.mc.util.Downloader;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GameVersionDownloader {

    private static JSONObject versionManifestCache = new JSONObject();
    private static JSONObject versionsList = new JSONObject();

    private static void downloadCacheIfNeeded() throws IOException {
        versionManifestCache = new JSONObject(Downloader.wget(new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json")));
        for (Object o : versionManifestCache.getJSONArray("versions")) {
            JSONObject version = (JSONObject) o;
            versionsList.put(version.getString("id"), version);
        }
    }


    /**
     * Downloads a minecraft version
     * @param version The version to download
     * @param downloadTo The file to download to
     * @throws IOException If an IO operation failed
     */
    public static void downloadVersion(GameVersion version, File downloadTo) throws IOException {
        downloadCacheIfNeeded();

        JSONObject versionJSON = versionsList.getJSONObject(version.getVersion());
        Downloader.download(new URL(versionJSON.getString("url")), downloadTo);
    }

    /**
     * Gets a {@link GameVersion} for the given ID: 16w15a, a1.2.1_01
     * @param id The game ID
     * @return The {@link GameVersion} for that version
     * @throws NoSuchVersionException If the version is invalid
     * @throws IOException If an IO operation failed
     */
    public static StandardGameVersion forVersion(String id) throws NoSuchVersionException, IOException {
        downloadCacheIfNeeded();
        if (!versionsList.has(id)) throw new NoSuchVersionException("No such version " + id);
        JSONObject versionJSON = versionsList.getJSONObject(id);
        return new StandardGameVersion(id, versionJSON.getString("type"), versionJSON.getString("releaseTime"));
    }

}
