package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.io.File;

import static com.googlecode.totallylazy.Files.files;
import static com.googlecode.totallylazy.Files.isDirectory;

@SuppressWarnings("unchecked")
public class Files {

    public static String toJavaPath(Class testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toHtmlPath(Class testClass) {
        return toPath(testClass) + ".html";
    }

    public static Option<File> find(File directory, Predicate<? super File> filePredicate) {
        return filter(directory, filePredicate).headOption();
    }

    public static Sequence<File> filter(File directory, final Predicate<? super File> filePredicate) {
        return files(directory).
                filter(filePredicate).
                join(files(directory).
                        filter(isDirectory()).
                        flatMap(filter(filePredicate)));
    }

    public static Callable1<File, Iterable<File>> filter(final Predicate<? super File> filePredicate) {
        return new Callable1<File, Iterable<File>>() {
            @Override
            public Iterable<File> call(File directory) throws Exception {
                return filter(directory, filePredicate);
            }
        };
    }

    private static String toPath(Class clazz) {
        return clazz.getName().replaceAll("\\.", "/");
    }
}