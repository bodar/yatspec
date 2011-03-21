package com.googlecode.yatspec.parsing;

import org.junit.Test;

import java.io.File;

import static com.googlecode.yatspec.parsing.Predicates.pathEndsWith;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilesTest {

    @Test
    public void shouldFindFile() {
        assertThat(Files.findOnly(workingDirectory(), pathEndsWith("FilesTest.java")), notNullValue());
    }

    @Test
    public void shouldNotFindFile() {
        assertThat(Files.findOnly(workingDirectory(), pathEndsWith("doesNotExist")), nullValue());
    }

    private File workingDirectory() {
        return new File(".");
    }
}
