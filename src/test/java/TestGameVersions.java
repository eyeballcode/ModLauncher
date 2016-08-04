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

import lib.mc.except.NoSuchVersionException;
import lib.mc.versions.GameVersionDownloader;
import lib.mc.versions.StandardGameVersion;

import java.io.File;
import java.io.IOException;

public class TestGameVersions {

    public static void main(String[] args) throws NoSuchVersionException, IOException {
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();

        StandardGameVersion version = GameVersionDownloader.forVersion("1.7.10");
        System.out.println(version.getVersion());
        System.out.println(version.getReleaseTime());
        System.out.println(version.getType());
        GameVersionDownloader.downloadVersion(version, new File("1.7.10.jar"));
    }
}