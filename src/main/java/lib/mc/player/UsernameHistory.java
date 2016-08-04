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

import java.util.HashMap;

public class UsernameHistory {

    private Player player;
    private HashMap<Long, String> times = new HashMap<>();

    public UsernameHistory(Player player, HashMap<Long, String> times) {
        this.player = player;
        this.times = times;
    }

    public HashMap<Long, String> getTimes() {
        return times;
    }

    /**
     * Gets the number of times the user changed his/her name
     * @return The number of times the user changed his/her name
     */
    public int getNumberOfChanges() {
        return times.size() - 1;
    }

    /**
     * The player this username history if for
     * @return The player
     */
    public Player forPlayer() {
        return player;
    }
}

