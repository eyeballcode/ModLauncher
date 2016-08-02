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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class UsernameUUIDStorage {

    private HashMap<String, Player> data = new HashMap<>();

    public UsernameUUIDStorage(JSONArray respArray) {
        for (Object n : respArray) {
            JSONObject user = (JSONObject) n;
            Player player = new Player(user.getString("id"), user.getString("name"));
            data.put(user.getString("name"), player);
        }
    }

    public Player getPlayer(String playername) {
        return data.get(playername);
    }

}
