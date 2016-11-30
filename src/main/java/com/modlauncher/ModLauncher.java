package com.modlauncher;

import com.modlauncher.api.game.GameLauncher;
import com.modlauncher.api.login.LoginHelper;
import com.modlauncher.api.login.UserProfile;
import com.modlauncher.api.modpacks.ModPack;
import com.modlauncher.api.modpacks.ModPackLookup;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class ModLauncher {

    public static void main(String[] args) throws IOException {
        UserProfile[] userProfiles = LoginHelper.getProfiles();
        if (userProfiles.length == 0) {
            login();
        }

        userProfiles = LoginHelper.getProfiles();
        System.out.println("Choose a profile: ");
        int i = 0;
        for (UserProfile userProfile : userProfiles) {
            System.out.println("  " + ++i + ") " + userProfile.toString());
        }
        Scanner scanner = new Scanner(System.in);
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice > userProfiles.length) throw new IllegalArgumentException("Invalid choice!");
        UserProfile profile = userProfiles[choice];

        System.out.println(profile);
        ModPack modPack = ModPackLookup.lookupModpackByName("EyePack");
        System.out.println(modPack.getDescription());

        modPack.getMCVersion().download();
        modPack.getForgeVersion().download();
        modPack.downloadMods();
        modPack.setupFolders();
        modPack.setupMods();
        modPack.grabConfigs();
        GameLauncher.launch(modPack, profile);
    }

    private static void login() throws IOException {
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

        LoginHelper.login(username, password);
    }

}
