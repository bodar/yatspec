package com.googlecode.yatspec;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Some;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Creator {
    public static <T> T create(Class<?> aClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?>[] constructors = aClass.getConstructors();
        return (T) constructors[0].newInstance();
    }

    public static Option<? extends Class<?>> optionalClass(String name) {
        try {
            return Option.some(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return Option.none();
        }
    }
}
