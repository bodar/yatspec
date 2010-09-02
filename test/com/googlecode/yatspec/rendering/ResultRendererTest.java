package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.TestResult;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class ResultRendererTest {
    @Test
    public void loadsTemplateOffClassPath() throws Exception {
        // setup
        TestResult result = new TestResult(this.getClass());

        // execute
        String html = new ResultRenderer().render(result);

        // verify
        assertThat(html, is(not(nullValue())));
    }
}
