package com.sicktracker.web.toolkit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Mapping {

    private final ObjectMapper objectMapper;

    public Mapping(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new MappingException(e);
        }
    }

    public <T> T fromJson(byte[] json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new MappingException(e);
        }
    }

    public <T> String toJson(T data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new MappingException(e);
        }
    }
}
