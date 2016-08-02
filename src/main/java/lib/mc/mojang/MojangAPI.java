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

package lib.mc.mojang;

import lib.mc.http.HTTPGETRequest;
import lib.mc.http.HTTPJSONResponse;
import lib.mc.player.SkinCapeInfo;
import lib.mc.util.Utils;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class MojangAPI {

    private static MojangAPIStatus statusCache;
    private static long lastCacheTime_Status = 0;
    private static HashMap<String, SkinCapeInfo> skinCapeInfoCache = new HashMap<>();

    public static MojangAPIStatus getStatus() throws IOException {
        if (statusCache != null) {
            if (lastCacheTime_Status - System.currentTimeMillis() < (1000 * 60 * 60)) {
                return statusCache;
            }
        }
        HTTPGETRequest httpgetRequest = new HTTPGETRequest();
        httpgetRequest.send(new URL("https://status.mojang.com/check"));
        JSONArray response = new HTTPJSONResponse(httpgetRequest.getResponse()).toJSONArray();
        MojangAPIStatus status = new MojangAPIStatus(response);

        lastCacheTime_Status = System.currentTimeMillis();
        statusCache = status;

        return status;
    }

    public static UUID fromUsername(String username, long time) throws IOException {
        HTTPGETRequest httpgetRequest = new HTTPGETRequest();
        if (time > 0)
            httpgetRequest.setPayload("at", String.valueOf(time));
        httpgetRequest.send(new URL("https://api.mojang.com/users/profiles/minecraft/" + username));
        HTTPJSONResponse response = new HTTPJSONResponse(httpgetRequest.getResponse());
        if (response.getResponse().length() == 0) {
            throw new RuntimeException("No such player!");
        }
        String rawUUID = response.toJSONObject().getString("id");
        String parsedUUID = Utils.parseUUID(rawUUID);
        return UUID.fromString(parsedUUID);
    }

    public static SkinCapeInfo getSkinAndCapeInfo(UUID playerUUID) throws IOException {
        String uuid = playerUUID.toString().replaceAll("-", "");
        if (skinCapeInfoCache.containsKey(uuid)) {
            return skinCapeInfoCache.get(uuid);
        }
        HTTPGETRequest request = new HTTPGETRequest();
        request.send(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid));
        HTTPJSONResponse response = new HTTPJSONResponse(request.getResponse());

        SkinCapeInfo skinCapeInfo = new SkinCapeInfo(response.toJSONObject());
        skinCapeInfoCache.put(uuid, skinCapeInfo);
        return skinCapeInfo;
    }

}
