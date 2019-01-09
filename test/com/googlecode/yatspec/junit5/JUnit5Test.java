package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.*;
import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.HyperlinkRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.rendering.html.tagindex.HtmlTagIndexRenderer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpecListener.class)
class JUnit5Test implements WithCustomResultListeners {

    @ParameterizedTest
    @Table({
        @Row({"1", "1", "2"}),
        @Row({"2", "3", "5"})
    })
    void sum(int a, int b, int sum) {
        assertEquals(sum, a + b);
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return sequence(
                new HtmlResultRenderer().
                        withCustomRenderer(Notes.class, new HyperlinkRenderer(new NotesRenderer(), "(?:#)([^\\s]+)", "<a href='http://localhost:8080/pretent-issue-tracking/$1'>$1</a>")),
                new HtmlIndexRenderer(),
                new HtmlTagIndexRenderer());
    }
}
