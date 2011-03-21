package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Predicate;

import java.io.File;

public final class Predicates {

    private Predicates() {
    }

    public static Predicate<? super File> directory() {
       return new Predicate<File>() {
           public boolean matches(File file) {
               return file.isDirectory();
           }
       };
    }

    public static Predicate<? super File> file() {
       return new Predicate<File>() {
           public boolean matches(File file) {
               return file.isFile();
           }
       };
    }

    public static Predicate<? super File> pathEndsWith(final String path) {
        return new Predicate<File>() {
            public boolean matches(File file) {
                return file.getPath().endsWith(path);
            }
        };
    }
}
