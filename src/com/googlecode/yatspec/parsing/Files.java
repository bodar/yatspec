package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import java.io.File;

import static com.googlecode.totallylazy.Sequences.characters;

public class Files {

    public static String toJavaPath(Class testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toHtmlPath(Class testClass) {
        return toPath(testClass) + ".html";
    }

    public static String toPath(Class clazz) {
        return classNameToString(characters(clazz.getName()).map(toSeparator()));
    }

    private static String classNameToString(Sequence<Character> className) {
        StringBuilder builder = new StringBuilder();
        for (Character c : className.toArray(Character.class)) {
            builder.append(c);
        }
        return builder.toString();
    }

    private static Callable1<? super Character, Character> toSeparator() {
        return new Callable1<Character, Character>() {
            public Character call(Character character) throws Exception {
                return character == '.' ? File.separatorChar : character;
            }
        };
    }
}