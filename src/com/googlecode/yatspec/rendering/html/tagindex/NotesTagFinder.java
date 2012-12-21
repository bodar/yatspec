package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.regex.Regex;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.TestMethod;

import java.util.regex.MatchResult;

import static com.googlecode.totallylazy.regex.Regex.regex;
import static com.googlecode.yatspec.junit.Notes.methods.notes;

public class NotesTagFinder implements TagFinder {
    private Regex regex;

    public NotesTagFinder() {
        this("#[^\\s]+");
    }

    public NotesTagFinder(String regex) {
        this.regex = regex(regex);
    }

    public Iterable<String> tags(TestMethod testMethod) {
        return notes(testMethod.getAnnotations())
                .map(notesToTags())
                .getOrElse(Sequences.<String>empty());
    }

    private Function1<Notes, Iterable<String>> notesToTags() {
        return new Function1<Notes, Iterable<String>>() {
            public Iterable<String> call(Notes notes) throws Exception {
                return regex.findMatches(notes.value())
                        .map(matchGroup());
            }
        };
    }

    private Function1<MatchResult, String> matchGroup() {
        return new Function1<MatchResult, String>() {
            public String call(MatchResult matchResult) throws Exception {
                return matchResult.group();
            }
        };
    }
}
