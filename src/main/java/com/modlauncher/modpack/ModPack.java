package com.modlauncher.modpack;

import org.json.JSONObject;

public class ModPack {

    private String name;
    private JSONObject data;

    public ModPack(String name, JSONObject data) {
        this.name = name;
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public boolean isModded() {
        return !data.getString("forgeVersion").equals("none");
    }

    public String genDesc() {
        StringBuilder stringBuilder = new StringBuilder("<html>");
        stringBuilder.append("<h1>About this modpack</h1>");
        stringBuilder.append("<p>").append(data.getString("description")).append("</p>");
        stringBuilder.append("<p>").append("This modpack uses MC version ").append(data.getJSONObject("versions").getJSONObject(data.getString("latestVersion")).getString("minecraftVersion")).append("</p>");

        JSONObject latestModList = data.getJSONObject("versions").getJSONObject(data.getString("latestVersion")).getJSONObject("modlist");
        if (latestModList.has("FastCraft")) {
            stringBuilder.append("<h1>WARNING: THIS MODPACK HAS FASTCRAFT!</h1>")
                    .append("<h2>When reporting bugs, please: </h2>")
                    .append("<ul><li>You say clearly that there's fastcraft</li><li>You make sure the bug has not been reported</li>");
        }
        stringBuilder.append("<h1>Mod List (Version ").append(data.getString("latestVersion")).append(")</h1>");
        stringBuilder.append("<ul>");
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
