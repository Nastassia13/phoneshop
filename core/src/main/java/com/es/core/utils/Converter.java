package com.es.core.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    public Map<String, Object> convertObjectToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            try {
                Object fieldObject = field.getType().equals(LocalDateTime.class)
                        ? Timestamp.valueOf((LocalDateTime) field.get(object)) : field.get(object);
                map.put(field.getName(), fieldObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return map;
    }
}
