package com.googlecode.yatspec.rendering;

public class Resources {
    public static String getResourceRelativeTo(Class aClass, String resource) {
        return aClass.getPackage().getName().replace('.', '/') + "/" + resource;
    }
}
