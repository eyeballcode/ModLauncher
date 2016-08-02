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

import lib.mc.mojang.MojangAPI;

public class TestMojangAPI {

    public static void main(String[] args) throws Exception {
        System.out.println(MojangAPI.getStatus().getStatus("minecraft.net"));
        System.out.println(MojangAPI.getStatus().getStatus("auth.mojang.com"));
        System.out.println(MojangAPI.fromUsername("Eyeballcode", -1).toString());
        System.out.println(MojangAPI.fromUsername("Eyeballcode", -1).toString());
    }

}