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

package lib.mc.versions;

import java.util.Date;

public abstract class GameVersion {
    /**
     * Gets the type of release: Snapshot or Release
     *
     * @return The type of release
     */
    public abstract String getType();

    /**
     * Get the release time
     *
     * @return The release time
     */
    public abstract Date getReleaseTime();

    /**
     * Returns the unparsed string
     * @return The unparsed release time string
     */
    public abstract String getReleaseTimeString();

    /**
     * The version name.
     * @return The version name
     */
    public abstract String getVersion();
}
