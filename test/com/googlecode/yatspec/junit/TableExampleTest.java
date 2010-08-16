package com.googlecode.yatspec.junit;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

@RunWith(TableRunner.class)
public class TableExampleTest {
    @Test
    @Table({@Row({"The quick brown fox jumps over the lazy dog", "fox"}),
            @Row({"Sator Arepo Tenet Opera Rotas", "Opera"})})
    public void demonstratingPassingMuiltipleArguments(String phrase, String word){
        assertThat(phrase, contains(word));
    }

    private Matcher<String> contains(String value) {
        return containsString(value);
    }
}
