package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Strings;

import java.io.File;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Files {

    public static String toJavaPath(Class testClass) {
        return toPath(testClass) + ".java";
    }

    public static String toHtmlPath(Class testClass) {
        return toPath(testClass) + ".html";
    }

    public static String toPath(Class clazz) {
        return characters(clazz.getName()).map(toSeparator()).toString("");
    }

    private static Callable1<? super Character, Character> toSeparator() {
        return new Callable1<Character, Character>() {
            public Character call(Character character) throws Exception {
                return character == '.' ? File.separatorChar : character;
            }
        };
    }
}