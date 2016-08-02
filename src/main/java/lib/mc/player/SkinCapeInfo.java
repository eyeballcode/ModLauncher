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

import lib.mc.util.ChecksumUtils;
import org.json.JSONObject;

public class SkinCapeInfo {

    JSONObject skin, cape;
    String playerType = "Steve";

    public SkinCapeInfo(JSONObject response) {
        String uuid = response.getString("id");
        String username = response.getString("name");
        for (Object n : response.getJSONArray("properties")) {
            JSONObject texture = (JSONObject) n;
            if (texture.getString("name").equals("textures")) {
                String base64value = texture.getString("value");
                JSONObject valuesDecoded = new JSONObject(ChecksumUtils.fromBase64(base64value));
                JSONObject texturesInfo = valuesDecoded.getJSONObject("textures");
                skin = texturesInfo.getJSONObject("SKIN");
                if (texturesInfo.has("CAPE")) {
                    cape = texturesInfo.getJSONObject("CAPE");
                }
                if (skin.has("metadata")) {
                    if (skin.getJSONObject("metadata").has("model")) {
                        if (skin.getJSONObject("metadata").getString("model").equals("slim"))
                            playerType = "Alex";
                    }
                }
            }
        }
    }

    public PlayerCape getCape() {
        return hasCape() ? new PlayerCape(cape) : null;
    }

    public PlayerSkin getSkin() {
        return new PlayerSkin(skin);
    }

    public boolean hasCape() {
        return cape != null;
    }

    public String getPlayerType() {
        return playerType;
    }
}
