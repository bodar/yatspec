package com.googlecode.yatspec.parsing;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class TextTest {
    @Test
    public void insertsASingleSpacesBetweenCapitalsAndTrims() {
         assertThat(Text.wordify("replaceAWithB;"), is("Replace a with b"));
         assertThat(Text.wordify("doesNotBlow"), is("Does not blow"));
    }

    @Test
    public void wordifyShouldNotRemoveLineBreaks() {
         assertThat(Text.wordify("Foo\nBar"), is("Foo\nBar"));
    }

    @Test
    public void wordifyShouldNotRemoveLeadingSpaces() {
         assertThat(Text.wordify("Foo\nBar"), is("Foo\nBar"));
    }
}
