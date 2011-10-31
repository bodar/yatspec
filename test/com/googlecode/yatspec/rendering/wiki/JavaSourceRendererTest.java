package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.yatspec.parsing.JavaSource;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JavaSourceRendererTest {
    @Test
    public void removesFirstLevelOfIndentation() throws Exception {
        assertThat(new JavaSourceRenderer().render(new JavaSource("\tFoo\n\tBar")), is("Foo\nBar"));
    }

    @Test
    public void removesLeadingAndTrailingBlankLines() throws Exception {
        assertThat(new JavaSourceRenderer().render(new JavaSource("\n\nFoo\nBar\n\n")), is("Foo\nBar"));
    }
}
