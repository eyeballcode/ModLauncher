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

package lib.mc.library;

import lib.mc.util.ChecksumUtils;

import java.util.HashMap;
import java.util.zip.Checksum;

public class ForgeLibraryChecksums {

    private HashMap<String, String> checksums = new HashMap<>();

    public ForgeLibraryChecksums(String checksums) {
        for (String line : checksums.split("\n")) {
            this.checksums.put(line.split(" ")[1], line.split(" ")[0]);
        }
    }

    public String sum(String path) {
        return checksums.get(path);
    }

}
