package com.eyeball.modlauncher.util;

import java.util.HashMap;

public class ChecksumList extends HashMap<String, String> {

    public ChecksumList(String data) {
        String[] lines = data.split("\n");
        for (String line : lines) {
            String sha1sum = line.split(" ")[0],
                    name = line.split(" ")[1];
            put(name, sha1sum);
        }
    }

}
