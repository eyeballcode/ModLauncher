package com.jrutil;

import java.io.IOException;
import java.io.InputStream;

/**
 * A class that helps with terminal / console stuff.
 */
public class TerminalHelper {

    /**
     * Gets the width of the terminal
     *
     * @return The terminal's width, or 80 if unknown.
     */
    public static int getTerminalWidth() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                Process p = Runtime.getRuntime().exec("mode 80, 25");
                InputStream inputStream = p.getInputStream();
                StringBuilder b = new StringBuilder();
                for (int i = inputStream.read(); i != -1; i = inputStream.read()) b.append((char) i);
                return Integer.parseInt(b.toString().trim());
            } else if (os.contains("mac") || os.contains("linux")) {
                Process p = Runtime.getRuntime().exec(new String[]{
                        "bash", "-c", "tput cols 2> /dev/tty"});
                InputStream inputStream = p.getInputStream();
                StringBuilder b = new StringBuilder();
                for (int i = inputStream.read(); i != -1; i = inputStream.read()) b.append((char) i);
                return Integer.parseInt(b.toString().trim());
            } else {
                throw new IOException("Hmmm, unsupported os?");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 80 Is a default for most terminals.
        return 80;
    }

    /**
     * Reads a password off the console
     * <p/>
     * Do note that this may not work in IDEs. If not supported and invoked, a {@link NullPointerException} will be thrown.
     *
     * @return The password read
     */
    public static String readPassword() {
        return readPassword("Password: ");
    }

    /**
     * Reads a password off the console with a custom prefix.
     * <p/>
     * Do note that this may not work in IDEs. If not supported and invoked, a {@link NullPointerException} will be thrown.
     *
     * @param prefix The prefix to print before asking for a password.
     * @return The password read.
     */
    public static String readPassword(String prefix) {
        System.out.print(prefix);
        return String.valueOf(System.console().readPassword());
    }

    /**
     * Reads a string from the console, with a custom prefix.
     *
     * @param prefix The prefix
     * @return The string read
     * @throws IOException If an error occured while reading from {@link System#in}.
     */
    public static String read(String prefix) throws IOException {
        System.out.print(prefix);
        StringBuilder out = new StringBuilder();
        for (int i = System.in.read(); i != -1 && i != '\n' && i != '\r'; i = System.in.read()) out.append((char) i);
        return out.toString().trim();
    }

    /**
     * Reads a string from the console.
     *
     * @return The string read
     * @throws IOException If an error occured while reading from {@link System#in}.
     */
    public static String read() throws IOException {
        return read("");
    }

    /**
     * Reads an int from the console.
     *
     * @return The int
     * @throws IOException           If an error occurred while reading from {@link System#in}.
     * @throws NumberFormatException If the input is not a valid number.
     */
    public static int readInt() throws IOException, NumberFormatException {
        return readInt("");
    }


    /**
     * Reads an int from the console, with a custom prefix.
     *
     * @param prefix The prefix
     * @return The int
     * @throws IOException           If an error occurred while reading from {@link System#in}.
     * @throws NumberFormatException If the input is not a valid number.
     */
    public static int readInt(String prefix) throws IOException, NumberFormatException {
        return Integer.parseInt(read(prefix));
    }


    /**
     * Reads a boolean from the console.
     *
     * @return The boolean
     * @throws IOException If an error occurred while reading from {@link System#in}.
     */
    public static boolean readBoolean() throws IOException, NumberFormatException {
        return readBoolean("");
    }


    /**
     * Reads an boolean from the console, with a custom prefix.
     *
     * @param prefix The prefix
     * @return The boolean
     * @throws IOException If an error occurred while reading from {@link System#in}.
     */
    public static boolean readBoolean(String prefix) throws IOException, NumberFormatException {
        return Boolean.parseBoolean(read(prefix));
    }


    /**
     * Reads a double from the console.
     *
     * @return The double
     * @throws IOException           If an error occurred while reading from {@link System#in}.
     * @throws NumberFormatException If the input is not a valid double.
     */
    public static double readDouble() throws IOException, NumberFormatException {
        return readDouble("");
    }


    /**
     * Reads a double from the console, with a custom prefix.
     *
     * @param prefix The prefix
     * @return The double
     * @throws IOException           If an error occurred while reading from {@link System#in}.
     * @throws NumberFormatException If the input is not a valid double.
     */
    public static double readDouble(String prefix) throws IOException, NumberFormatException {
        return Double.parseDouble(read(prefix));
    }


    /**
     * Reads a float from the console.
     *
     * @return The float
     * @throws IOException           If an error occurred while reading from {@link System#in}.
     * @throws NumberFormatException If the input is not a valid float.
     */
    public static float readFloat() throws IOException, NumberFormatException {
        return readFloat("");
    }


    /**
     * Reads a float from the console, with a custom prefix.
     *
     * @param prefix The prefix
     * @return The float
     * @throws IOException           If an error occurred while reading from {@link System#in}.
     * @throws NumberFormatException If the input is not a valid float.
     */
    public static float readFloat(String prefix) throws IOException, NumberFormatException {
        return Float.parseFloat(read(prefix));
    }

    /**
     * Reads a long from the console.
     *
     * @return The long
     * @throws IOException           If an error occurred while reading from {@link System#in}.
     * @throws NumberFormatException If the input is not a valid number.
     */
    public static long readLong() throws IOException, NumberFormatException {
        return readLong("");
    }


    /**
     * Reads a long from the console, with a custom prefix.
     *
     * @param prefix The prefix
     * @return The long
     * @throws IOException           If an error occurred while reading from {@link System#in}.
     * @throws NumberFormatException If the input is not a valid number.
     */
    public static long readLong(String prefix) throws IOException, NumberFormatException {
        return Long.parseLong(read(prefix));
    }

}
