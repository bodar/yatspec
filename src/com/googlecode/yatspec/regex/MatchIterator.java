package com.googlecode.yatspec.regex;

import jedi.option.None;
import jedi.option.Option;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

public class MatchIterator implements Iterator<MatchResult> {
    private final Matcher matcher;
    private MatchResult currentMatch = null;

    public MatchIterator(Matcher matcher) {
        this.matcher = matcher;
        matcher.reset();
    }

    public boolean hasNext() {
        if (currentMatch == null) {
            if (matcher.find()) {
                currentMatch = matcher.toMatchResult();
                return true;
            }
            return false;
        }
        return true;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public MatchResult next() {
        if (currentMatch != null) {
            final MatchResult result = currentMatch;
            currentMatch = null;
            return result;
        } else {
            if (hasNext()) {
                return next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
