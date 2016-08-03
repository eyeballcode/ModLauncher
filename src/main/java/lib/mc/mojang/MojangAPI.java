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
import lib.mc.http.HTTPPOSTRequest;
import lib.mc.player.Player;
import lib.mc.player.SkinCapeInfo;
import lib.mc.player.UsernameHistory;
import lib.mc.player.UsernameUUIDStorage;
import lib.mc.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
     * Get a Player object from a username
     *
     * @param username The username
     * @return The UUID of the user
     * @throws IOException If the user data could not be fetched
     */
    public static Player fromUsername(String username) throws IOException {
        HTTPGETRequest httpgetRequest = new HTTPGETRequest();
        httpgetRequest.send(new URL("https://api.mojang.com/users/profiles/minecraft/" + username));
        HTTPJSONResponse response = new HTTPJSONResponse(httpgetRequest.getResponse());
        if (response.getResponse().length() == 0) {
            throw new RuntimeException("No such player!");
        }
        String rawUUID = response.toJSONObject().getString("id");
        String parsedUUID = Utils.parseUUID(rawUUID);
        return new Player(parsedUUID, response.toJSONObject().getString("name"));
    }

    /**
     * Gets cape and skin info for the player
     * <p>
     * <h1>WARNING!!!</h1>
     * This endpoint has extreme rate limiting (1min / request).
     * Therefore, all results are cached and to update them, you need to clear the cache.
     *
     * @param playerInfo The player
     * @return The skin and cape info
     * @throws IOException          If it could not be fetched
     * @throws RateLimitedException If you're rate limited
     * @see #clearSkinAndCapeCache(Player)
     */
    public static SkinCapeInfo getSkinAndCapeInfo(Player playerInfo) throws IOException, RateLimitedException {
        UUID playerUUID = playerInfo.getUUID();
        String uuid = playerUUID.toString().replaceAll("-", "");
        if (skinCapeInfoCache.containsKey(uuid)) {
            return skinCapeInfoCache.get(uuid);
        }
        HTTPGETRequest request = new HTTPGETRequest();
        request.send(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid));
        HTTPJSONResponse response = new HTTPJSONResponse(request.getResponse());
        if (response.toJSONObject().has("error")) {
            if (response.toJSONObject().getString("error").equals("TooManyRequestsException")) {
                throw new RateLimitedException("");
            }
        }
        SkinCapeInfo skinCapeInfo = new SkinCapeInfo(response.toJSONObject());
        skinCapeInfoCache.put(uuid, skinCapeInfo);
        return skinCapeInfo;
    }

    /**
     * Clears the skin and cape cache for a user
     *
     * @param user The user
     * @see #getSkinAndCapeInfo(Player)
     */
    public static void clearSkinAndCapeCache(Player user) {
        skinCapeInfoCache.remove(user.getUUID().toString().replaceAll("-", ""));
    }

    /**
     * Gets the usernames and UUIDs of players requested
     *
     * @param usernames The names
     * @return The Username and UUIDs of the players requested
     * @throws IOException If there was an error
     */
    public static UsernameUUIDStorage getUUIDs(ArrayList<String> usernames) throws IOException {
        if (usernames.size() > 100) throw new RateLimitedException("Cannot have more than 100 usernames");
        JSONArray array = new JSONArray();
        for (String username : usernames) {
            array.put(username);
        }
        HTTPPOSTRequest httppostRequest = new HTTPPOSTRequest();
        httppostRequest.setContentType("application/json");
        httppostRequest.setPayload(array.toString());
        httppostRequest.send(new URL("https://api.mojang.com/profiles/minecraft"));
        HTTPJSONResponse response = new HTTPJSONResponse(httppostRequest.getResponse());

        JSONArray respArray = response.toJSONArray();
        return new UsernameUUIDStorage(respArray);
    }

    /**
     * Get the UUIDS from a list
     *
     * @param names The names
     * @return The Username and UUIDs of the players requested
     * @throws IOException If there was an error
     */
    public static UsernameUUIDStorage getUUIDs(String... names) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, names);
        return getUUIDs(list);
    }

    public static UsernameHistory getUsernameHistory(Player player) throws IOException {
        UUID uuid = player.getUUID();
        HashMap<Long, String> times = new HashMap<>();
        HTTPGETRequest httpgetRequest = new HTTPGETRequest();
        httpgetRequest.send(new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replaceAll("-", "") + "/names"));
        HTTPJSONResponse response = new HTTPJSONResponse(httpgetRequest.getResponse());
        for (Object o : response.toJSONArray()) {
            if (o instanceof JSONObject) {
                JSONObject user = (JSONObject) o;
                if (!user.has("changedToAt"))
                    times.put(0L, user.getString("name"));
                else
                    times.put(user.getLong("changedToAt"), user.getString("name"));

            }
        }
        return new UsernameHistory(uuid, times);
    }
}
