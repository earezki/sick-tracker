package com.sicktracker.patients;

import static org.apache.commons.lang3.Validate.notBlank;

public class GeoHash {

    private final String value;

    private GeoHash(String value) {
        this.value = value;
    }

    public static GeoHash of(String hash) {
        return new GeoHash(notBlank(hash));
    }

    public String asString() {
        return value;
    }

    public boolean isSameAs(GeoHash rhs) {
        return value.equals(rhs.value);
    }
}
