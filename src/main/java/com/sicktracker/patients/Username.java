package com.sicktracker.patients;

import static org.apache.commons.lang3.Validate.notBlank;

public class Username {

    private final String value;

    private Username(String value) {
        this.value = value;
    }

    public static Username of(String value) {
        return new Username(notBlank(value));
    }

    public String asString() {
        return value;
    }
}
