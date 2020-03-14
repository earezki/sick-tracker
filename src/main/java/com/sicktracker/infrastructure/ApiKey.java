package com.sicktracker.infrastructure;

import static org.apache.commons.lang3.Validate.notBlank;

public class ApiKey {

    private final String value;

    private ApiKey(String value) {
        this.value = value;
    }

    public static ApiKey of(String apiKey) {
        return new ApiKey(notBlank(apiKey));
    }

    public String asString() {
        return value;
    }

}
