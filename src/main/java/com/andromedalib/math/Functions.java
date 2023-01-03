package com.andromedalib.math;

public class Functions {

    /**
     * Clamps a number between to the nearest min/max value
     * 
     * @param num Number to clamp
     * @param min Minimum value
     * @param max Maximum value
     * @return Clamped number
     */
    public static double clamp(double num, double min, double max) {
        return Math.max(min, Math.min(max, num));
    }
}
