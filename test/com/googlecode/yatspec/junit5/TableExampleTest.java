package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.Table;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(SpecListener.class)
class TableExampleTest {

    @ParameterizedTest
    @Table({@Row("some value")})
    void demonstratingPassingSingleArgument(String argument) throws Exception {
        assertThat(argument, is(notNullValue()));
    }


    @ParameterizedTest
    @Table({@Row({"The quick brown fox jumps over the lazy dog", "fox"}),
            @Row({"Sator Arepo Tenet Opera Rotas", "Opera"})})
    void demonstratingPassingMultipleArguments(String phrase, String word) {
        assertThat(phrase, contains(word));
    }

    @ParameterizedTest
    @Table({@Row({"First", "Second"})})
    void demostratingPassingArrayOfArguments(String... arguments) {
        assertThat(arguments.length, is(2));
        assertThat(arguments[0], is("First"));
        assertThat(arguments[1], is("Second"));
    }

    private Matcher<String> contains(String value) {
        return containsString(value);
    }
}
