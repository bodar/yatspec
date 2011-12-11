package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.regex.Regex;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.TestMethod;

import java.util.regex.MatchResult;

import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.regex.Regex.regex;

public class NotesTagFinder implements TagFinder {
    private Regex regex;

    public NotesTagFinder() {
        this("#[^ ]+");
    }

    public NotesTagFinder(String regex) {
        this.regex = regex(regex);
    }

    @Override
    public Iterable<String> tags(TestMethod testMethod) {
        Notes notes = testMethod.getNotes();
        if (notes == null) return empty();
        return regex.findMatches(notes.value()).map(new Callable1<MatchResult, String>() {
            @Override
            public String call(MatchResult matchResult) throws Exception {
                return matchResult.group();
            }
        });
    }
}
