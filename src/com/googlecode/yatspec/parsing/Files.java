package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.*;

import java.io.File;

import static com.googlecode.totallylazy.Files.files;
import static com.googlecode.totallylazy.Files.isDirectory;
import static com.googlecode.totallylazy.Files.recursiveFiles;

@SuppressWarnings("unchecked")
public class Files {

    public static String toJavaPath(Class testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toHtmlPath(Class testClass) {
        return toPath(testClass) + ".html";
    }

    public static Option<File> find(File directory, Predicate<? super File> filePredicate) {
        return recursiveFiles(directory).filter(filePredicate).headOption();
    }

    public static Sequence<File> filter(File directory, final Predicate<? super File> filePredicate) {
        return recursiveFiles(directory).filter(filePredicate);
    }

    private static String toPath(Class clazz) {
        return clazz.getName().replaceAll("\\.", "/");
    }
}