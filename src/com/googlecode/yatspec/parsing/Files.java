package com.googlecode.yatspec.parsing;

public class Files {

    public static String toJavaPath(Class testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toHtmlPath(Class testClass) {
        return toPath(testClass) + ".html";
    }

    public static String toPath(Class clazz) {
        return clazz.getName().replaceAll("\\.", "/");
    }
}