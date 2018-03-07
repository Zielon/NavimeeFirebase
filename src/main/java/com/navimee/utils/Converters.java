package com.navimee.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Converters {

    private static ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> toMap(Object object) {
        return mapper.convertValue(object, Map.class);
    }
}
