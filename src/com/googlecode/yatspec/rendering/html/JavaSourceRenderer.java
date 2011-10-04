package com.googlecode.yatspec.rendering.html;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.Renderer;

import java.util.List;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Sequences.map;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.EMPTY;

public class JavaSourceRenderer implements Renderer<JavaSource> {
    private static final Pattern DOT_CLASS = Pattern.compile("\\.class(\\W|$)");

    @Override
    public String render(JavaSource javaSource) throws Exception {
        return lines(removateDotClass(javaSource.value().trim())).map(Text.wordify()).toString("\n");
    }

    public static Sequence<String> lines(final String sourceCode) {
        return sequence(sourceCode.split(lineSeperator()));
    }

    public static String lineSeperator() {
        return System.getProperty("line.separator");
    }

    public static String removateDotClass(String s) {
        return DOT_CLASS.matcher(s).replaceAll("$1");
    }
}
