package com.googlecode.yatspec.parsing;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Strings.EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FilesTest {

    private static final String LONG_NAME = repeat('A').take(101).toString(EMPTY, EMPTY, EMPTY);

    @Test
    public void supportsLongFileNames() throws Exception {
        assertThat(Files.replaceDotsWithSlashes(LONG_NAME).length(), is(LONG_NAME.length()));
    }

}
