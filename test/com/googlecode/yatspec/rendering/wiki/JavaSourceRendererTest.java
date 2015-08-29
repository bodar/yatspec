package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.yatspec.parsing.JavaSource;
import org.junit.Test;

import static java.lang.System.lineSeparator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JavaSourceRendererTest {
    @Test
    public void removesFirstLevelOfIndentation() throws Exception {
        String withIndentation = "\tFoo" + lineSeparator() +"\tBar";
        String withoutIndentation = "Foo" + lineSeparator() + "Bar";
        assertThat(new JavaSourceRenderer().render(new JavaSource(withIndentation)), is(withoutIndentation));
    }

    @Test
    public void removesLeadingAndTrailingBlankLines() throws Exception {
        assertThat(new JavaSourceRenderer().render(new JavaSource("\n\nFoo\nBar\n\n")), is("Foo\nBar"));
    }
}
