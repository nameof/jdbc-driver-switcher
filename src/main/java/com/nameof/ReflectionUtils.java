package com.nameof;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static Object getFieldValue(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}
