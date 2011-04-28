package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.TestMethod;
import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.TestParser.parseTestMethods;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestParserTest {
    @Test
    @Notes("Some method notes")
    public void testParseTestMethods() throws Exception {
        final List<TestMethod> methods = parseTestMethods(getClass());
        assertThat(sequence(methods).first().getNotes().value(), is("Some method notes"));

    }
}
