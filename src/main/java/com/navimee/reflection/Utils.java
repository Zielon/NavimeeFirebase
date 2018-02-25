package com.navimee.reflection;

import java.lang.reflect.Field;

public class Utils {
    public static <T> String nameof(Class<T> type, String fieldName) throws NoSuchFieldException {
        Field field = type.getDeclaredField(fieldName);
        return field.getName();
    }

    public static <T, V> V valueof(T object, String fieldName) throws NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        try {
            return (V) field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
