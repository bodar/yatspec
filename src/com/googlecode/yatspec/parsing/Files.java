package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.io.File;
import java.io.FileFilter;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.Predicates.directory;

@SuppressWarnings("unchecked")
public class Files {

    public static String toJavaPath(Class testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toHtmlPath(Class testClass) {
        return toPath(testClass) + ".html";
    }

    public static File findOnly(File directoryWithin, Predicate<? super File> filePredicate) {
        return find(directoryWithin, filePredicate).headOption().getOrNull();
    }

    public static Sequence<File> find(File directoryWithin, Predicate<? super File> filePredicate) {
        return findRecursively(directoryWithin, is(directory()), filePredicate);
    }

    private static Sequence<File> findRecursively(File directoryWithin, final Predicate<? super File> directoryPredicate, final Predicate<? super File> filePredicate) {
        return files(directoryWithin, or(directoryPredicate, filePredicate)).fold(empty(File.class), new Callable2<Sequence<File>, File, Sequence<File>>() {
            @Override
            public Sequence<File> call(Sequence<File> filesToReturn, File file) throws Exception {
                return filePredicate.matches(file) ? filesToReturn.add(file) : filesToReturn.join(findRecursively(file, directoryPredicate, filePredicate));
            }
        });
    }

    private static String toPath(Class clazz) {
        return clazz.getName().replaceAll("\\.", "/");
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
}