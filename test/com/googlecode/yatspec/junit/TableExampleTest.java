package com.googlecode.yatspec.junit;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpecRunner.class)
public class TableExampleTest {

    @Test
    @Table({@Row("some value")})
    public void demonstratingPassingSingleArgument(String argument) throws Exception {
        assertThat(argument, is(notNullValue()));
    }


    @Test
    @Table({@Row({"The quick brown fox jumps over the lazy dog", "fox"}),
            @Row({"Sator Arepo Tenet Opera Rotas", "Opera"})})
    public void demonstratingPassingMultipleArguments(String phrase, String word){
        assertThat(phrase, contains(word));
    }

    private Matcher<String> contains(String value) {
        return containsString(value);
    }
}
