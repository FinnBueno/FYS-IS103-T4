package me.is103t4.corendonluggagesystem.util;

import javafx.scene.paint.Color;

/**
 * Class to calculate 'distance' between colours
 *
 * @author Finn Bon
 */
public class ColorUtil {

    /**
     * Source: https://stackoverflow.com/questions/9018016/how-to-compare-two-colors-for-similarity-difference
     * Author: https://stackoverflow.com/users/690204/alonisser
     *
     * @param colour1 color 1
     * @param colour2 color 2
     * @return The 'distance' between the colours
     */
    public static double getDistance(String colour1, String colour2) {
        Color c1 = toColor(colour1);
        return getDistance(c1, colour2);
    }

    public static double getDistance(Color c1, String colour2) {
        Color c2 = toColor(colour2);
        if (c1 == null || c2 == null)
            return 0D;
        long rmean = ((long) (c1.getRed() * 255) + (long) (c2.getRed() * 255)) / 2;
        long r = (long) (c1.getRed() * 255) - (long) (c2.getRed() * 255);
        long g = (long) (c1.getGreen() * 255) - (long) (c2.getGreen() * 255);
        long b = (long) (c1.getBlue() * 255) - (long) (c2.getBlue() * 255);
        return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
    }

    private static Color toColor(String colour) {
        if (colour == null)
            return null;
        return Color.color(
                Integer.parseInt(colour.substring(0, 2), 16) / 255D,
                Integer.parseInt(colour.substring(2, 4), 16) / 255D,
                Integer.parseInt(colour.substring(4), 16) / 255D
        );
    }
    
}
