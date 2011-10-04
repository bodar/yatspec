package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.yatspec.state.TestResult;
import org.junit.Test;

public class WikiResultRendererTest {
    @Test
    public void doesNotBlow() throws Exception {
        String render = new WikiResultRenderer().render(new TestResult(getClass()));
        System.out.println(render);
    }
}
