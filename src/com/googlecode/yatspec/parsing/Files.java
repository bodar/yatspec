package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable2;
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

    public static Option<File> find(File directoryWithin, Predicate<? super File> filePredicate) {
        return filter(directoryWithin, filePredicate).headOption();
    }

    public static Sequence<File> filter(File directoryWithin, final Predicate<? super File> filePredicate) {
        Sequence<File> matchingFiles = files(directoryWithin).filter(filePredicate);
        return files(directoryWithin).filter(isDirectory()).fold(matchingFiles, addFiles(filePredicate));
    }

    private static Callable2<Sequence<File>, File, Sequence<File>> addFiles(final Predicate<? super File> filePredicate) {
        return new Callable2<Sequence<File>, File, Sequence<File>>() {
            @Override
            public Sequence<File> call(Sequence<File> filesToReturn, File file) throws Exception {
                return filesToReturn.join(filter(file, filePredicate));
            }
        };
    }

    private static String toPath(Class clazz) {
        return clazz.getName().replaceAll("\\.", "/");
    }
}