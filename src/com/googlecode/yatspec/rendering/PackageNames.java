package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.*;

import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;
import static java.util.regex.Pattern.quote;

public class PackageNames {
    public static Predicate<? super String> directSubpackageOf(final String parentPackage) {
        return new Predicate<String>() {
            @Override
            public boolean matches(String packageName) {
                String pattern = format("%s\\.?[^.]+$", quote(parentPackage));
                return packageName.matches(pattern);
            }
        };
    }

    public static String packageDisplayName(String name) {
        if ("".equals(name)) {
            return "/";
        } else if (name.lastIndexOf(".") >= 0) {
            return name.substring(name.lastIndexOf(".") + 1, name.length());
        } else {
            return name;
        }
    }

    /*
    allAncestors().call("com.googlecode.yatspec");

    returns a sequence of:

    "com"
    "com.googlecode"
    "com.googlecode.yatspec"
     */
    public static Callable1<? super String, ? extends Iterable<String>> allAncestors() {
        return new Callable1<String, Iterable<String>>() {
            @Override
            public Iterable<String> call(String name) throws Exception {
                return sequence(name.split("\\.")).
                        fold(empty(String.class), buildPath());
            }

            private Callable2<? super Sequence<String>, ? super String, Sequence<String>> buildPath() {
                return new Callable2<Sequence<String>, String, Sequence<String>>() {
                    @Override
                    public Sequence<String> call(Sequence<String> allPaths, String name) throws Exception {
                        Option<String> longestPath = allPaths.
                                sortBy(returnArgument(String.class)).
                                lastOption();
                        if (longestPath.isEmpty())
                            return allPaths.append(name);
                        else
                            return allPaths.append(longestPath.get() + "." + name);
                    }
                };
            }
        };
    }
}
