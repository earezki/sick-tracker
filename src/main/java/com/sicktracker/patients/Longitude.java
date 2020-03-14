package com.sicktracker.patients;

import static java.lang.Math.abs;
import static java.lang.String.format;

public class Longitude {

    private static final int LONGITUDE_BOUND = 180;

    private final double value;

    private Longitude(double value) {
        this.value = value;
    }

    public static Longitude of(double value) {
        if (abs(value) > LONGITUDE_BOUND) {
            throw new IllegalArgumentException(format("Invalid longitude[%s], should be inclusively between [%s, %s]", value, -LONGITUDE_BOUND, LONGITUDE_BOUND));
        }
        return new Longitude(value);
    }

    public boolean isToTheRightTo(double other) {
        return value >= other;
    }

    public double asDecimal() {
        return value;
    }

}
