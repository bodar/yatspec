package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.io.File;
import java.io.FileFilter;

import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.Sequences.sequence;

@SuppressWarnings("unchecked")
public class Files {

    public static File find(File directoryWithin, String filename) {
        return find(directoryWithin, is(directory()), is(fileWithName(filename)));
    }

    public static File find(File directoryWithin, Predicate<? super File> directoryPredicate, Predicate<? super File> filePredicate) {
        Sequence<File> files = findRecursively(directoryWithin, directoryPredicate, filePredicate);
        return files.filter(filePredicate).headOption().getOrNull();
    }

    private static Sequence<File> findRecursively(File directoryWithin, Predicate<? super File> directoryPredicate, Predicate<? super File> filePredicate) {
        Sequence<File> files = files(directoryWithin, or(directoryPredicate, filePredicate));
        for (File file : files.filter(directoryPredicate)) {
            files = files.join(findRecursively(file, directoryPredicate, filePredicate));
        }
        return files;
    }

    private static Predicate<File> fileWithName(String filename) {
        return and(is(file()), withFilename(filename));
    }

    private static Sequence<File> files(File directoryWithin, final Predicate<? super File> predicate) {
        return sequence(directoryWithin.listFiles(fileFilter(predicate)));
    }

    private static FileFilter fileFilter(final Predicate<? super File> predicate) {
        return new FileFilter() {
            public boolean accept(File file) {
                return predicate.matches(file);
            }
        };
    }

    private static Predicate<? super File> file() {
       return new Predicate<File>() {
           public boolean matches(File file) {
               return file.isFile();
           }
       };
    }

    private static Predicate<? super File> directory() {
       return new Predicate<File>() {
           public boolean matches(File file) {
               return file.isDirectory();
           }
       };
    }

    private static Predicate<? super File> withFilename(final String filename) {
        return new Predicate<File>() {
            public boolean matches(File file) {
                return file.getName().equals(filename);
            }
        };
    }
}