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
 * A <code>{@link LibraryObjectInfo}</code> for MC Forge packages
 */
public class ForgeLibraryObjectInfo extends LibraryObjectInfo {

    private boolean isForgeLib;

    /**
     * Constructs a ForgeLibraryObjectInfo object
     *
     * @param info       The base info
     * @param isForgeLib Is it a forge lib or can it be obtained from libraries.minecraft.net?
     */
    public ForgeLibraryObjectInfo(LibraryObjectInfo info, boolean isForgeLib) {
        super(info.getLibraryName(), info.getPackageName(), info.getVersion());
        this.isForgeLib = isForgeLib;
    }

    @Override
    public String toURL() {
        if (getLibraryName().equals("forge")) {
            String defaultURL = super.toURL();
            return defaultURL.substring(0, defaultURL.length() - 4) + "-installer.jar";
        } else
            return super.toURL() + (isForgeLib ? ".pack.xz" : "");
    }
}
