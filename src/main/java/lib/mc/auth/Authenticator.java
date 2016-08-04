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

package lib.mc.auth;

import lib.mc.except.LoginException;
import lib.mc.http.HTTPJSONResponse;
import lib.mc.http.HTTPPOSTRequest;
import lib.mc.player.AccessToken;
import lib.mc.player.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class Authenticator {

    /**
     * Logs into minecraft
     *
     * @param username The username. Email for mojang accounts. Use email or username for old legacy accounts.
     * @param password The password
     * @return A {@link AccessToken} storing the login info.
     * @throws IOException    If an IO operation failed
     * @throws LoginException If the username or password is incorrect
     */
    public static AccessToken login(String username, String password) throws IOException, LoginException {
        return login(username, password, null);
    }

    /**
     * Logs into minecraft
     *
     * @param username    The username
     * @param password    The password
     * @param clientToken A client token. Use null or {@link #login(String, String)} for default client token.
     * @return A {@link AccessToken} storing the login info
     * @throws IOException    If an IO operation failed
     * @throws LoginException If the username or password is incorrect
     */
    public static AccessToken login(String username, String password, String clientToken) throws IOException, LoginException {
        HTTPPOSTRequest request = new HTTPPOSTRequest();
        JSONObject payload = new JSONObject();
        payload.put("agent", new JSONObject().put("name", "Minecraft").put("version", 1));
        payload.put("username", username);
        payload.put("password", password);
        String expectedToken = (clientToken == null ? UUID.randomUUID().toString() : clientToken);
        payload.put("clientToken", expectedToken);
        request.setPayload(payload.toString());
        request.setContentType("application/json");
        request.send(new URL("https://authserver.mojang.com/authenticate"));
        HTTPJSONResponse response = new HTTPJSONResponse(request.getResponse());
        JSONObject respPayload = response.toJSONObject();
        if (respPayload.has("error")) {
            if (respPayload.getString("errorMessage").equals("Invalid credentials. Invalid username or password.")) {
                throw new LoginException("Invalid credentials. Invalid username or password.");
            }
        }
        String accessToken = respPayload.getString("accessToken");
        String foundToken = respPayload.getString("clientToken");
        if (!foundToken.equals(expectedToken)) throw new RuntimeException("Did not match client tokens!");
        JSONObject profile = respPayload.getJSONObject("selectedProfile");
        String uuid = profile.getString("id"),
                name = profile.getString("name");
        //noinspection SimplifiableConditionalExpression
        return new AccessToken(accessToken, expectedToken, new Player(uuid, name, profile.has("legacy"), profile.has("demo")));
    }

    /**
     * Refreshes an Access token
     * @param accessTokenObj The access token
     * @return The new {@link AccessToken}
     * @throws IOException If an IO operation failed
     */
    public static AccessToken refresh(AccessToken accessTokenObj) throws IOException {
        HTTPPOSTRequest request = new HTTPPOSTRequest();
        JSONObject payload = new JSONObject();
        String accessToken = accessTokenObj.getAccessToken(),
                clientToken = accessTokenObj.getClientToken();
        Player player = accessTokenObj.getPlayer();
        payload.put("accessToken", accessToken);
        payload.put("clientToken", clientToken == null ? "Minecraft" : clientToken);
        payload.put("selectedProfile", new JSONObject().put("id", player.getUUID().toString().replaceAll("-", "")).put("name", player.getName()));
        request.setPayload(payload.toString());
        request.send(new URL("https://authserver.mojang.com/refresh"));
        HTTPJSONResponse response = new HTTPJSONResponse(request.getResponse());
        String newAT = response.toJSONObject().getString("accessToken"),
                ct = response.toJSONObject().getString("clientToken");
        if (!ct.equals(clientToken)) throw new RuntimeException("Did not match client tokens!");
        return new AccessToken(newAT, clientToken, accessTokenObj.getPlayer());
    }

    /**
     * Checks if an access token is valid
     * @param accessTokenObj The access token
     * @return If its valid
     * @throws IOException If an IO operation failed
     */
    public static boolean validate(AccessToken accessTokenObj) throws IOException {
        HTTPPOSTRequest request = new HTTPPOSTRequest();
        JSONObject payload = new JSONObject();
        String accessToken = accessTokenObj.getAccessToken(),
                clientToken = accessTokenObj.getClientToken();
        Player player = accessTokenObj.getPlayer();
        payload.put("accessToken", accessToken);
        payload.put("clientToken", clientToken == null ? "Minecraft" : clientToken);
        request.setPayload(payload.toString());
        request.send(new URL("https://authserver.mojang.com/validate"));
        return request.getResponse().getResponseCode() == 204;
    }

    /**
     * Invalidates ALL {@link AccessToken}s
     * @param username The username
     * @param password The password
     * @return If it invalidated all access tokens successfully
     * @throws IOException If an IO operation failed
     */
    public static boolean signout(String username, String password) throws IOException {
        HTTPPOSTRequest request = new HTTPPOSTRequest();
        JSONObject payload = new JSONObject();
        payload.put("username", username);
        payload.put("password", password);
        request.setPayload(payload.toString());
        request.send(new URL("https://authserver.mojang.com/signout"));
        return request.getResponse().getResponseCode() == 204;
    }

    /**
     *
     * Invalidates an {@link AccessToken}
     *
     * @param accessTokenObj The {@link AccessToken}
     * @return If it was invalidated successfully
     * @throws IOException If an IO operation failed
     */
    public static boolean invalidate(AccessToken accessTokenObj) throws IOException {
        HTTPPOSTRequest request = new HTTPPOSTRequest();
        JSONObject payload = new JSONObject();
        String accessToken = accessTokenObj.getAccessToken(),
                clientToken = accessTokenObj.getClientToken();
        payload.put("accessToken", accessToken);
        payload.put("clientToken", clientToken == null ? "Minecraft" : clientToken);
        request.setPayload(payload.toString());
        request.send(new URL("https://authserver.mojang.com/invalidate"));
        return request.getResponse().getResponseCode() == 204;
    }


}

