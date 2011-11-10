package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.yatspec.state.TestResult;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class WikiResultRendererTest {
    @Test
    public void preservesJavaFormatting() throws Exception {
        String result = new WikiResultRenderer().render(new TestResult(getClass()));
            // Supports indents and comments
        assertThat(result, containsString("    // Supports indents and comments"));
    }
}