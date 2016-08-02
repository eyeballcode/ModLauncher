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

import lib.mc.except.RateLimitedException;
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

    /**
     * Gets the status of the mojang API endpoints
     *
     * @return The statuses.
     * @throws IOException If the status could not be retrieved
     */
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

    /**
     * Get a UUID from a username
     *
     * @param username The username
     * @param time     The time. Only works if user has changed name before. If user has not or you want the latest, use a negative number.
     * @return The UUID of the user
     * @throws IOException If the UUID could not be fetched
     */
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

    /**
     * Gets cape and skin info for the player
     * <p>
     * <h1>WARNING!!!</h1>
     * This endpoint has extreme rate limiting.
     * Therefore, all results are cached and to update them, you need to clear the cache.
     *
     * @param playerUUID The player UUID
     * @return The skin and cape info
     * @throws IOException          If it could not be fetched
     * @throws RateLimitedException If you're rate limited
     * @see #clearSkinAndCapeCache(UUID)
     */
    public static SkinCapeInfo getSkinAndCapeInfo(UUID playerUUID) throws IOException, RateLimitedException {
        String uuid = playerUUID.toString().replaceAll("-", "");
        if (skinCapeInfoCache.containsKey(uuid)) {
            return skinCapeInfoCache.get(uuid);
        }
        HTTPGETRequest request = new HTTPGETRequest();
        request.send(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid));
        HTTPJSONResponse response = new HTTPJSONResponse(request.getResponse());
        if (response.toJSONObject().has("error")) {
            if (response.toJSONObject().getString("error").equals("TooManyRequestsException")) {
                throw new RateLimitedException();
            }
        }
        SkinCapeInfo skinCapeInfo = new SkinCapeInfo(response.toJSONObject());
        skinCapeInfoCache.put(uuid, skinCapeInfo);
        return skinCapeInfo;
    }

    /**
     * Clears the skin and cape cache for a user
     *
     * @param user The user's UUID
     * @see #getSkinAndCapeInfo(UUID)
     */
    public static void clearSkinAndCapeCache(UUID user) {
        skinCapeInfoCache.remove(user.toString().replaceAll("-", ""));
    }

}
