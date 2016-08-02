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

import org.json.JSONArray;
import org.json.JSONObject;

public class MojangAPIStatus {

    JSONObject info = new JSONObject();

    MojangAPIStatus(JSONArray rawInfo) {
        JSONObject info = new JSONObject();
        for (Object site_ : rawInfo) {
            JSONObject site = (JSONObject) site_;
            String siteURL = site.keySet().toArray()[0].toString();
            info.put(siteURL, site.getString(siteURL));
        }
        this.info = info;
    }

    /**
     * Gets the status for a site.
     *
     * @param site The site eg minecraft.net
     * @return The status GREEN, YELLOW or RED
     * @throws Exception If the site doesn't exist
     */
    public MojangAPIStatusType getStatus(String site) throws Exception {
        if (!info.has(site)) {
            throw new Exception("No such site!");
        }
        return MojangAPIStatusType.valueOf(info.getString(site).toUpperCase());
    }

}
    
