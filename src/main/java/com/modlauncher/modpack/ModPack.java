package com.modlauncher.modpack;

import org.json.JSONObject;

public class ModPack {

    private String name;
    private JSONObject data;

    public ModPack(String name, JSONObject data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String genDesc() {
        StringBuilder stringBuilder = new StringBuilder("<html>");
        stringBuilder.append("<h1>About this modpack</h1>");
        stringBuilder.append("<p>").append(data.getString("description")).append("</p>");
        stringBuilder.append("<h1>Mod List (Version ").append(data.getString("latestVersion")).append(")</h1>");
        stringBuilder.append("<ul>");
        JSONObject latestModList = data.getJSONObject("versions").getJSONObject(data.getString("latestVersion")).getJSONObject("modlist");
        for (String modName : latestModList.keySet()) {
            stringBuilder.append("<li>").append(modName);
            JSONObject mod = latestModList.getJSONObject(modName);
            if (mod.has("depends")) {
                stringBuilder.append(" (Depends on ");
                int i = 0;
                for (Object object : mod.getJSONArray("depends")) {
                    stringBuilder.append(object);
                    if (i + 1 != mod.getJSONArray("depends").length()) {
                        stringBuilder.append(", ");
                    }

                    i++;
                }
                stringBuilder.append(")");
            }
        }
        stringBuilder.append("</ul>");
        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }
}
