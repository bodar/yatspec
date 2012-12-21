package com.googlecode.yatspec.state;

import com.googlecode.yatspec.junit.Notes;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.junit.Notes.methods.notes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Notes("Some notes")
public class TestResultTest {
    @Test
    public void supportsNotesOnClass() throws Exception {
        assertThat(notes(sequence(getClass().getAnnotations())).get().value(), is("Some notes"));
    }
}
