package com.sicktracker.patients;

import static java.lang.Math.abs;
import static java.lang.String.format;

public class Latitude {

    private static final int LATITUDE_BOUND = 90;

    private final double value;

    private Latitude(double value) {
        this.value = value;
    }

    public static Latitude of(double value) {
        if (abs(value) > LATITUDE_BOUND) {
            throw new IllegalArgumentException(format("Invalid latitude[%s], should be inclusively between [%s, %s]", value, -LATITUDE_BOUND, LATITUDE_BOUND));
        }
        return new Latitude(value);
    }

    public boolean isAbove(double other) {
        return value >= other;
    }

    public double asDecimal() {
        return value;
    }
}
