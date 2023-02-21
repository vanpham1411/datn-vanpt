package com.example.datn.domain.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUntil {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static byte[] writeAsByte(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static String serialize(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    public static <T> T deserializeList(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    public static <T> T deepCopy(Object value, Class<T> clazz)  {
        try {
            return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(value), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
