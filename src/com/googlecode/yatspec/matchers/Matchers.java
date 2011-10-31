package com.googlecode.yatspec.matchers;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.allOf;

@SuppressWarnings("unused")
public final class Matchers {
    private Matchers() {
    }

    public static <T> Matcher<T> has(Matcher<T> ... matchers) {
        return allOf(matchers);
    }

}