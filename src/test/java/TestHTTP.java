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

import lib.mc.http.HTTPGETRequest;
import lib.mc.http.HTTPJSONResponse;

import java.io.IOException;
import java.net.URL;

public class TestHTTP {

    public static void main(String[] args) throws IOException {
        HTTPGETRequest request = new HTTPGETRequest();
        request.setPayload("Chicken", "Tasty");
        request.send(new URL("http://httpbin.org/get"));
        HTTPJSONResponse response = new HTTPJSONResponse(request.getResponse());
//        System.out.println(response.getResponse());
        System.out.println(response.toJSONObject().toString(4));
    }

}
