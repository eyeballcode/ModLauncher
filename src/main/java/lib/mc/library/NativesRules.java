package lib.mc.library;

import lib.mc.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A set of rules for native libraries
 */
public class NativesRules {

    private final ArrayList<Utils.OS> allowed = new ArrayList<>();
    private final ArrayList<Utils.OS> disallowed = new ArrayList<>();

    /**
     * Construct a new <code>NativeRules</code> using the default rules array in the native json object from mc.
     * @param rulesJSON The rules JSON.
     */
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

    /**
     * Gets the list of allowed OSes.
     * @return The list of allowed OSes.
     */
    public ArrayList<Utils.OS> getAllowed() {
        return allowed;
    }

    /**
     * Gets the list of disallowed OSes.
     * @return The list of disallowed OSes.
     */
    public ArrayList<Utils.OS> getDisallowed() {
        return disallowed;
    }

    /**
     * Checks if an OS is allowed.
     * @param os The OS to check.
     * @return If the os specified is allowed.
     */
    public boolean isAllowed(Utils.OS os) {
        return allowed.contains(os);
    }

}