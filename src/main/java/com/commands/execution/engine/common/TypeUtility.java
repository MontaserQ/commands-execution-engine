package com.commands.execution.engine.common;

import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@UtilityClass
public class TypeUtility {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericTypeParameter(Class<?> myClass, int i) {
        final Type[] genericInterfaces = myClass.getGenericInterfaces();
        final ParameterizedType type;

        if (genericInterfaces.length == 0) {
            type = (ParameterizedType) myClass.getGenericSuperclass();
        } else {
            type = (ParameterizedType) genericInterfaces[0];
        }

        final Type actualType = type.getActualTypeArguments()[i];
        return (Class<T>) actualType;
    }
}
