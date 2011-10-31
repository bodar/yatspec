package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.rendering.html.JavaSourceRenderer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JavaSourceRendererTest {
    @Test
    public void shouldRemoveDotClassFromRenderedOutput() throws Exception {
        assertThat(renderedVersionOf("get(SomeThing.class)"),                       is("get(SomeThing)"));
        assertThat(renderedVersionOf("SomeThing.class"),                            is("SomeThing"));
        assertThat(renderedVersionOf("get(SomeThing.class).get(SomeThing.class)"),  is("get(SomeThing).get(SomeThing)"));
        assertThat(renderedVersionOf("variable.classButActuallyMethod()"),          is("variable.classButActuallyMethod()"));
        assertThat(renderedVersionOf("variable.class_weird()"),                     is("variable.class_weird()"));
    }

    private String renderedVersionOf(String s) {
        return JavaSourceRenderer.removateDotClass(s);
    }
}
