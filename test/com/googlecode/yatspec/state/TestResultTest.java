package com.googlecode.yatspec.state;

import com.googlecode.yatspec.junit.Link;
import com.googlecode.yatspec.junit.Notes;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Notes("Some notes")
@Link(href="Some Link", text = "Some Text")
public class TestResultTest {
    @Test
    public void supportsNotesOnClass() throws Exception {
        assertThat(new TestResult(getClass()).getNotes(), is("Some notes"));
    }
    
    @Test
    public void supportsLinkOnClass() throws Exception {
        TestResult.LinkDetails link = new TestResult(getClass()).getLink();
        assertThat(link.getHref(), is("Some Link"));
        assertThat(link.getText(), is("Some Text"));
    }

}
