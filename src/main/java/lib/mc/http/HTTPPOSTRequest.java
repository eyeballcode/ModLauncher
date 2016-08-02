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

package lib.mc.http;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class HTTPPOSTRequest extends HTTPRequest {

    HashMap<String, String> payload = new HashMap<>();
    boolean sent = false;
    HTTPResponse response;

    public void setPayload(String key, String value) {
        payload.put(key, value);
    }

    @Override
    public void send(URL url) throws IOException {
        if (sent) throw new IOException("Already sent");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setRequestMethod("POST");
        for (String key : payload.keySet()) {
            connection.setRequestProperty(key, payload.get(key));
        }
        connection.getOutputStream().close();
        connection.getInputStream();
        String response = new Scanner(connection.getInputStream()).useDelimiter("\\A").next();
        this.response = new HTTPResponse(response, connection.getResponseCode());
    }

    @Override
    public HTTPResponse getResponse() {
        return response;
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
