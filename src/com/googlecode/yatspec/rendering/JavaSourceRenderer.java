package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.parsing.Text;

import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.EMPTY;

public class JavaSourceRenderer implements Renderer<JavaSource>{
    public static final Pattern DOT_CLASS = Pattern.compile("\\.class([^A-Za-z_0-9]|$)");

    @Override
    public String render(JavaSource javaSource) throws Exception {
        return sequence(javaSource.value()).map(removeDotClass()).map(wordify()).toString(EMPTY, "\r\n", EMPTY, Long.MAX_VALUE);
    }

    private static Callable1<? super String, String> wordify() {
        return new Callable1<String, String>() {
            @Override
            public String call(String value) throws Exception {
                return Text.wordify(value);
            }
        };
    }

    private static Callable1<? super String, String> removeDotClass() {
        return new Callable1<String, String>() {
            public String call(String s) throws Exception {
                return DOT_CLASS.matcher(s).replaceAll("$1");
            }
        };
    }
}
