package com.googlecode.yatspec.parsing;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JavaSourceTest {

    @Test
    public void replaceMethodArgumentsWithValues() {
        verifyReplacement("given(radicand(radicand));", "given(radicand(9));", "radicand", "9");
        verifyReplacement("given(radicand(xyz, radicand));", "given(radicand(xyz, 9));", "radicand", "9");
        verifyReplacement("given(radicand(radicand, xyz));", "given(radicand(9, xyz));", "radicand", "9");
        verifyReplacement("given(radicand(radicand, xyz, radicand));", "given(radicand(9, xyz, 9));", "radicand", "9");
        verifyReplacement("then(b(b), is(b));", "then(b(9), is(9));", "b", "9");
        verifyReplacement("then(b( b ), is( b ));", "then(b( 9 ), is( 9 ));", "b", "9");
    }

    private void verifyReplacement(String original, String replaced, String argument, String value) {
        JavaSource originalSource = new JavaSource(original);

        JavaSource replacedSource = originalSource.replace(asList(argument), asList(value));

        assertThat(replacedSource.value(), is(replaced));

    }
}
