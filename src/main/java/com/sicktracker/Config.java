package com.sicktracker;

import java.util.List;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;

public class Config {

    private final Properties properties;

    public Config(Properties properties) {
        this.properties = properties;
    }

    public String getString(String name) {
        return properties.getProperty(name);
    }

    public List<String> getStrings(String name) {
        return asList(properties.getProperty(name).split(","));
    }

    public int getInt(String name) {
        return parseInt(properties.getProperty(name));
    }

    public long getLong(String name) {
        return parseLong(properties.getProperty(name));
    }

    public boolean hasNot(String name) {
        return !properties.containsKey(name);
    }
}
