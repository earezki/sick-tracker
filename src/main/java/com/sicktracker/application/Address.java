package com.sicktracker.application;

import static org.apache.commons.lang3.Validate.notBlank;

public class Address {

    private final String value;

    private Address(String value) {
        this.value = value;
    }

    public static Address of(String address) {
        return new Address(notBlank(address));
    }

    public String asString() {
        return value;
    }
}
