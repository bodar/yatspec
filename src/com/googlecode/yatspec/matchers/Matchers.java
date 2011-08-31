package com.googlecode.yatspec.matchers;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.jdom.Document;
import org.jdom.Namespace;

import static org.hamcrest.CoreMatchers.allOf;

@SuppressWarnings("unused")
public final class Matchers {
    private Matchers() {
    }

    public static <T> Matcher<T> has(Matcher<T> ... matchers) {
        return allOf(matchers);
    }

    public static Matcher<Document> hasXPath(String xpath, Namespace... namespaces) {
        return HasXPath.hasXPath(xpath, namespaces);
    }
}