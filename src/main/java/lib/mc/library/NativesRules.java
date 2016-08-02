package lib.mc.library;

import lib.mc.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class NativesRules {

    private final ArrayList<Utils.OS> allowed = new ArrayList<>();
    private final ArrayList<Utils.OS> disallowed = new ArrayList<>();

    public NativesRules(JSONArray rulesJSON) {
        if (rulesJSON.length() == 0) {
            Collections.addAll(allowed, Utils.OS.values());
        } else {
            for (Object object : rulesJSON) {
                if (object instanceof JSONObject) {
                    JSONObject rule = (JSONObject) object;
                    String action = rule.getString("action");
                    if (rule.has("os")) {
                        JSONObject osTypes = rule.getJSONObject("os");
                        String name = osTypes.getString("name");
                        Utils.OS OS = Utils.OS.valueOf(name.toUpperCase());
                        if (action.equals("allow")) {
                            allowed.add(OS);
                            for (Utils.OS osType : Utils.OS.values()) {
                                if (!osType.toString().equals(OS.toString())) {
                                    disallowed.add(osType);
                                }
                            }
                        } else {
                            disallowed.add(OS);
                            for (Utils.OS osType : Utils.OS.values()) {
                                if (!osType.toString().equals(OS.toString())) {
                                    allowed.add(osType);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Utils.OS> getAllowed() {
        return allowed;
    }

    public ArrayList<Utils.OS> getDisallowed() {
        return disallowed;
    }

    public boolean isAllowed(Utils.OS os) {
        return allowed.contains(os);
    }

}