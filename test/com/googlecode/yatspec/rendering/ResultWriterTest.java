package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.TestResult;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class ResultWriterTest {
    @Test
    public void writesFileToDirectory() throws Exception {
        // setup
        TestResult result = new TestResult(this.getClass());

        // execute
        File file = new ResultWriter(new TemporaryDirectory()).write(result);

        // verify
        assertThat(FileUtils.readFileToString(file), is(not(nullValue())));
    }
}