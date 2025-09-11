package org.townsimulator.utils;

import org.annotationlib.annotations.LogField;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class to enhance class's toString and filter fields to show
 */
public class CustomFieldsLogging {

    private CustomFieldsLogging() {
    }

    public static <T> String toString(T obj) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Field> fields = Arrays.stream(obj.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(LogField.class)).toList();
        stringBuilder.append("\n");
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                stringBuilder.append(field.getName()).append(" = ").append(value).append(" ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
