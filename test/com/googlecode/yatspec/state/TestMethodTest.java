package com.googlecode.yatspec.state;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.googlecode.yatspec.fixture.RandomFixtures.anyString;
import static com.googlecode.yatspec.state.TestMethod.buildName;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestMethodTest {

    @Test
    public void rendersMethodName() throws Exception {
        String methodName = anyString();
        String renderedMethodName = methodName + "()";
        List<String> noArgs = emptyList();

        String actualTestMethodName = buildName(methodName, noArgs);

        assertThat(actualTestMethodName, is(renderedMethodName));
    }

    @Test
    public void rendersMethodNameWithArguments() throws Exception {
        String methodName = anyString();
        String firstArg = anyString();
        String secondArg = anyString();
        String renderedMethodName = String.format("%s(%s, %s)", methodName, firstArg, secondArg);
        List<String> someArgs = Arrays.asList(firstArg, secondArg);

        String actualTestMethodName = buildName(methodName, someArgs);

        assertThat(actualTestMethodName, is(renderedMethodName));
    }
}
