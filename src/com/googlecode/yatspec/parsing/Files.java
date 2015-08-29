package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Strings;

import java.io.File;

import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Sequences.characters;

public class Files {

    public static void overwrite(File output, String content) throws Exception {
        output.delete();
        output.getParentFile().mkdirs();
        write(content.getBytes("UTF-8"), output);
        System.out.println("Yatspec output:\n" + output);
    }

    public static String toJavaResourcePath(Class testClass) {
        return toResourcePath(testClass) + ".java";
    }

    public static String toJavaPath(Class testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toHtmlPath(Class testClass) {
        return toPath(testClass) + ".html";
    }

    public static String toResourcePath(Class clazz) {
        return replaceDotsWithForwardSlashes(clazz.getName());
    }

    public static String toPath(Class clazz) {
        return replaceDotsWithSlashes(clazz.getName());
    }

    public static String replaceDotsWithSlashes(final String name) {
        return characters(name).map(dotsToSlashes()).toString(Strings.EMPTY, Strings.EMPTY, Strings.EMPTY);
    }

    public static String replaceDotsWithForwardSlashes(final String name) {
        return characters(name).map(dotsToForwardSlashes()).toString(Strings.EMPTY, Strings.EMPTY, Strings.EMPTY);
    }

    private static Callable1<? super Character, Character> dotsToSlashes() {
        return new Callable1<Character, Character>() {
            public Character call(Character character) throws Exception {
                return character == '.' ? File.separatorChar : character;
            }
        };
    }

    private static Callable1<? super Character, Character> dotsToForwardSlashes() {
        return new Callable1<Character, Character>() {
            public Character call(Character character) throws Exception {
                return character == '.' ? '/' : character;
            }
        };
    }
}
