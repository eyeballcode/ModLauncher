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

import lib.mc.auth.Authenticator;
import lib.mc.except.LoginException;
import lib.mc.player.AccessToken;
import lib.mc.player.Player;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class TestLogin {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            Console console = System.console();
            String password;
            if (console == null) {
                System.out.print("(Plaintext) ");
                password = scanner.nextLine();
            } else {
                password = String.valueOf(console.readPassword());
            }
            AccessToken token = Authenticator.login(password, username);
            System.out.println("AT: " + token.getAccessToken());
            System.out.println("CT: " + token.getClientToken());
            Player player = token.getPlayer();
            System.out.println("UUID: " + player.getUUID());
            System.out.println("Name: " + player.getName());
            System.out.println("Legacy: " + player.isLegacy());
        } catch (LoginException e) {
            System.out.println("Login error!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
