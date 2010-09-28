package com.googlecode.yatspec.regex;

import com.googlecode.totallylazy.Callable1;

import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Callables.call;

public class Matches implements Iterable<MatchResult> {
    private final Pattern pattern;
    private final String text;

    public Matches(Pattern pattern, String text) {
        this.pattern = pattern;
        this.text = text;
    }

    public static Matches matches(Pattern pattern, String text) {
        return new Matches(pattern, text);
    }

    public Iterator<MatchResult> iterator() {
        return new MatchIterator(pattern.matcher(text));
    }

    public String replace(Callable1<MatchResult, String> matched) {
        return replace(doNothing(), matched);
    }

    public static Callable1<String, String> doNothing() {
        return new Callable1<String, String>() {
            public String call(String value) {
                return value;
            }
        };
    }

    public String replace(Callable1<String, String> notMatched, Callable1<MatchResult, String> matched) {
        StringBuilder builder = new StringBuilder();
        int position = 0;
        for (MatchResult matchResult : this) {
            String before = text.substring(position, matchResult.start());
            if (before.length() > 0) builder.append(filterNull(call(notMatched, (before))));
            builder.append(filterNull(call(matched,(matchResult))));
            position = matchResult.end();
        }
        String after = text.substring(position);
        if (after.length() > 0) builder.append(filterNull(call(notMatched,after)));
        return builder.toString();
    }

    public static String filterNull(String value) {
        if (value == null)
            return "";
        else
            return value;
    }
}
