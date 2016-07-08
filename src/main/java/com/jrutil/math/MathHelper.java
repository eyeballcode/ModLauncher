package com.jrutil.math;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class MathHelper {

    /**
     * Generates a random int from the range provided.
     *
     * @param start The start range.
     * @param end   The end range.
     * @return The random number
     */
    public static int randomInt(int start, int end) {
        return new Random().nextInt(end - start + 1) + start;
    }

    /**
     * Generates a random float from the range provided.
     *
     * @param start The start range.
     * @param end   The end range.
     * @return The random number.
     */
    public static float randomFloat(float start, float end) {
        return start + (end - start) * new Random().nextFloat();
    }

    /**
     * Generates a random double from the range provided.
     *
     * @param start The start range.
     * @param end   The end range.
     * @return The random number.
     */
    public static double randomDouble(double start, double end) {
        return start + (end - start) * new Random().nextDouble();
    }

    /**
     * Rounds the given float to the provided number of decimal places.
     *
     * @param in         The float given in.
     * @param numberOfDP The number of decimal places expected.
     * @return The new float.
     */
    public static String roundFloatToXDP(float in, int numberOfDP) {
        StringBuilder n = new StringBuilder("0.");
        for (int i = 0; i < numberOfDP; i++) n.append("0");
        DecimalFormat df = new DecimalFormat(n.toString());
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(in);
    }

    /**
     * Rounds the given double to the provided number of decimal places.
     *
     * @param in         The double given in.
     * @param numberOfDP The number of decimal places expected.
     * @return The new double.
     */
    public static String roundDoubleToXDP(double in, int numberOfDP) {
        StringBuilder n = new StringBuilder("0.");
        for (int i = 0; i < numberOfDP; i++) n.append("0");
        DecimalFormat df = new DecimalFormat(n.toString());
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(in);
    }

}
