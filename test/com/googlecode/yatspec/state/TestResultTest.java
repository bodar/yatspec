package com.googlecode.yatspec.state;

import com.googlecode.yatspec.junit.Notes;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Notes("Some notes")
public class TestResultTest {
    @Test
    public void supportsNotesOnClass() throws Exception {
        assertThat(new TestResult(getClass()).getNotes().value(), is("Some notes"));
    }
}
