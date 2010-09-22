package com.googlecode.yatspec.regex;

import jedi.functional.Functor;

import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

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

    public String replace(Functor<MatchResult, String> matched) {
        return replace(doNothing(), matched);
    }

    public static Functor<String, String> doNothing() {
        return new Functor<String, String>() {
            public String execute(String value) {
                return value;
            }
        };
    }

    public String replace(Functor<String, String> notMatched, Functor<MatchResult, String> matched) {
        StringBuilder builder = new StringBuilder();
        int position = 0;
        for (MatchResult matchResult : this) {
            String before = text.substring(position, matchResult.start());
            if (before.length() > 0) builder.append(filterNull(notMatched.execute(before)));
            builder.append(filterNull(matched.execute(matchResult)));
            position = matchResult.end();
        }
        String after = text.substring(position);
        if (after.length() > 0) builder.append(filterNull(notMatched.execute(after)));
        return builder.toString();
    }

    public static String filterNull(String value) {
        if (value == null)
            return "";
        else
            return value;
    }
}
