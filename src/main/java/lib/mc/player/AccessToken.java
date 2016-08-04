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

package lib.mc.player;

public class AccessToken {
    String accessToken, clientToken;
    Player player;

    public AccessToken(String accessToken, String clientToken, Player player) {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.player = player;
    }

    /**
     * Gets the access token
     * @return The access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Gets the client token used
     * @return The client token
     */
    public String getClientToken() {
        return hasClientToken()? clientToken : "Minecraft";
    }

    /**
     * The player this access token is for
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * If it uses a custom client token.
     * @return True if it uses a custom client token, False if otherwise
     */
    public boolean hasClientToken() {
        return clientToken != null;
    }
}
