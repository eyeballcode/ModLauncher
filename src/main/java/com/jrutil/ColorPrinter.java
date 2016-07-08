package com.jrutil;

/**
 * A utility class used for printing colors.
 */
public class ColorPrinter {
    /**
     * A {@link LockableHashMap} that contains all of the formats, and is locked.
     * <p/>
     * Do note that <code>italic</code> and <code>strike</code> may not be available on all systems. One such system would be IntelliJ.
     */
    public static final LockableHashMap<String, Integer> formatMap = new LockableHashMap<String, Integer>();

    /**
     * The template for the color output in linux.
     */
    public static final String TEMPLATE = "\033[";

    static {
        // Misc
        formatMap.put("reset", 0);
        formatMap.put("bold", 1);
        formatMap.put("nobold", 21);
        formatMap.put("underline", 4);
        formatMap.put("nounderline", 24);
        formatMap.put("italic", 3);
        formatMap.put("strike", 9);
        // Standard colors
        formatMap.put("black", 30);
        formatMap.put("red", 31);
        formatMap.put("green", 32);
        formatMap.put("yellow", 33);
        formatMap.put("blue", 34);
        formatMap.put("purple", 35);
        formatMap.put("cyan", 36);
        formatMap.put("white", 37);
        formatMap.put("reset", 0);
        // High Intensity (Bold) colors
        formatMap.put("iblack", 90);
        formatMap.put("ired", 91);
        formatMap.put("igreen", 92);
        formatMap.put("iyellow", 99);
        formatMap.put("iblue", 94);
        formatMap.put("ipurple", 95);
        formatMap.put("icyan", 96);
        formatMap.put("iwhite", 97);
        // Standard background colors
        formatMap.put("bgblack", 40);
        formatMap.put("bgred", 41);
        formatMap.put("bggreen", 42);
        formatMap.put("bgyellow", 43);
        formatMap.put("bgblue", 44);
        formatMap.put("bgpurple", 45);
        formatMap.put("bgcyan", 46);
        formatMap.put("bgwhite", 47);
        // High Intensity backgrounds
        formatMap.put("bgiblack", 100);
        formatMap.put("bgired", 101);
        formatMap.put("bgigreen", 102);
        formatMap.put("bgiyellow", 103);
        formatMap.put("bgiblue", 104);
        formatMap.put("bgipurple", 105);
        formatMap.put("bgicyan", 106);
        formatMap.put("bgiwhite", 107);
        // Lock the hashmap to prevent modifications.
        formatMap.lock();
    }

    /**
     * Format for colors: %format%
     * <p/>
     * Supported formats:
     * <ul>
     * <li>black</li>
     * <li>red</li>
     * <li>green</li>
     * <li>yellow</li>
     * <li>blue</li>
     * <li>purple</li>
     * <li>cyan</li>
     * <li>white</li>
     * <li>reset</li>
     * <li>bold</li>
     * <li>nobold</li>
     * <li>underline</li>
     * <li>nounderline</li>
     * <li>bgblack</li>
     * <li>bgred</li>
     * <li>bggreen</li>
     * <li>bgyellow</li>
     * <li>bgblue</li>
     * <li>bgpurple</li>
     * <li>bgcyan</li>
     * <li>bgwhite</li>
     * <li>underline</li>
     * <li>bgiblack</li>
     * <li>bgired</li>
     * <li>bgigreen</li>
     * <li>bgiyellow</li>
     * <li>bgiblue</li>
     * <li>bgipurple</li>
     * <li>bgicyan</li>
     * <li>bgiwhite</li>
     * </ul>
     *
     * @param formattedPrintString The formatted print string. See above for supported formats.
     * @see #println(String)
     */
    public static void print(String formattedPrintString) {
        formattedPrintString += "%reset%";
        for (String key : formatMap.keySet()) {
            String replacement = TEMPLATE + formatMap.get(key) + "m";
            formattedPrintString = formattedPrintString.replaceAll("%" + key + "%", replacement);
        }
        System.out.print(formattedPrintString);
    }

    /**
     * Prints a formatted print string with an extra <code>\n</code> character
     *
     * @param formattedPrintString The formatted print string.
     * @see #print(String)
     */
    public static void println(String formattedPrintString) {
        formattedPrintString += "%reset%";
        print(formattedPrintString + "\n");
    }

}
