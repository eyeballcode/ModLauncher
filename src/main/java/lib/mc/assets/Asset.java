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

package lib.mc.assets;

public class Asset {

    private String filepath, sha1sum;

    /**
     * Constructs a new <code>Asset</code> object.
     *
     * @param filepath The filepath name of the object.
     * @param sha1sum  The SHA1SUM of the file.
     */
    public Asset(String filepath, String sha1sum) {
        this.filepath = filepath;
        this.sha1sum = sha1sum;
    }

    /**
     * Get the SHA1SUM of the hash
     *
     * @return The SHA1SUM of the hash
     */
    public String getSHA1Sum() {
        return sha1sum;
    }

    /**
     * Get the file path to download to
     *
     * @return The file path to download to
     */
    public String getFilepath() {
        return filepath;
    }
}
