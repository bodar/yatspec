package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Option;
import org.junit.Test;

import java.io.File;

import static com.googlecode.totallylazy.Files.name;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Strings.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilesTest {

    @Test
    public void shouldFindFile() {
        assertThat(Files.find(workingDirectory(), where(name(), endsWith("FilesTest.java"))), notNullValue());
    }

    @Test
    public void shouldNotFindFile() {
        assertThat(Files.find(workingDirectory(), where(name(), endsWith("doesNotExist"))), is((Option<File>)none(File.class)));
    }

    private File workingDirectory() {
        return new File(".");
    }
}
