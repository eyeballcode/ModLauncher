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
 * A Library Object for standard minecraft packages.
 */
public class NativeMCLibraryObject extends LibraryObject {

    private String rawName, sha1Sum;

    private NativesRules rules;
    private ExtractRules extractRules;

    public NativeMCLibraryObject(String rawName, String sha1Sum, NativesRules rules, ExtractRules extractRules) {
        this.rawName = rawName;
        this.rules = rules;
        this.sha1Sum = sha1Sum;
        this.extractRules = extractRules;
    }

    public ExtractRules getExtractRules() {
        return extractRules;
    }

    public String getSHA1Sum() {
        return sha1Sum;
    }

    public NativesRules getRules() {
        return rules;
    }

    @Override
    public String getRawName() {
        return rawName;
    }

    @Override
    public String getHostServer() {
        return "https://libraries.minecraft.net/";
    }

    @Override
    public LibraryObjectInfo parseName() {
        return new NativeLibraryObjectInfo(super.parseName());
    }
}
