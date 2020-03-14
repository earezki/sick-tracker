package com.sicktracker.patients;

import static java.lang.String.format;

public class Precision {

    private static final int MAX_PRECISION = 12;
    private static final int BYTES_BASE_32_COUNT = 5;

    private final int value;

    private Precision(int value) {
        this.value = value;
    }

    public static Precision of(int value) {
        if (value < 0 || value > MAX_PRECISION) {
            throw new IllegalArgumentException(format("Precision [%s] should be inclusively between [%s, %s]", value, 0, MAX_PRECISION));
        }
        return new Precision(value);
    }

    public int size() {
        return value * BYTES_BASE_32_COUNT;
    }
}
