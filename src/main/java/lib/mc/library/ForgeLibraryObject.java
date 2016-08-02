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

/**
 * A Forge Library Object, similar to <code>{@link DefaultMCLibraryObject}</code>.
 */
public class ForgeLibraryObject extends LibraryObject {

    private String rawName;
    private boolean isForgeLib;

    public ForgeLibraryObject(String rawName, boolean isForgeLib) {
        this.rawName = rawName;
        this.isForgeLib = isForgeLib;
    }

    @Override
    public String getRawName() {
        return rawName;
    }

    @Override
    public String getHostServer() {
        if (isForgeLib)
            return "http://files.minecraftforge.net/maven/";
        else
            return "https://libraries.minecraft.net/";
    }

    @Override
    public LibraryObjectInfo parseName() {
        LibraryObjectInfo info = super.parseName();
        return new ForgeLibraryObjectInfo(info, isForgeLib);
    }

    @Override
    public String getSHA1Sum() {
        return null;
    }
}
