package com.googlecode.yatspec.rendering;

public class Resources {
    public static String getResouceRelativeTo(Class aClass, String resource) {
        return aClass.getPackage().getName().replace('.', '/') + "/" + resource;
    }
}
