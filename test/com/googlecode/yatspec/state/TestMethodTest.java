package com.googlecode.yatspec.state;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.googlecode.yatspec.fixture.RandomFixtures.anyString;
import static com.googlecode.yatspec.junit.SpecRunner.TEST_METHOD_INVOCATION_NAME_RENDERER;
import static com.googlecode.yatspec.state.TestMethod.invocationName;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestMethodTest {

    @Test
    public void createsAnInvocationNameForAMethodNameWithoutArgs() throws Exception {
        String methodName = anyString();
        String expectedInvocationName = methodName + "()";
        List<String> noArgs = emptyList();

        String actualInvocationName = invocationName(new ScenarioName(methodName, noArgs));

        assertThat(actualInvocationName, is(expectedInvocationName));
    }

    @Test
    public void createsAnInvocationNameForAMethodNameWithArgs() throws Exception {
        String methodName = anyString();
        String firstArg = anyString();
        String secondArg = anyString();
        String expectedInvocationName = String.format("%s(%s, %s)", methodName, firstArg, secondArg);
        List<String> someArgs = Arrays.asList(firstArg, secondArg);

        String actualInvocationName = invocationName(new ScenarioName(methodName, someArgs));

        assertThat(actualInvocationName, is(expectedInvocationName));
    }
    
    @Ignore
    @Test
    public void createsAMavenSurefireInvocationNameForAMethodNameWithoutArgs() throws Exception {
        System.setProperty(TEST_METHOD_INVOCATION_NAME_RENDERER, "");
        String methodName = anyString();
        List<String> noArgs = emptyList();

        String actualInvocationName = invocationName(new ScenarioName(methodName, noArgs));

        assertThat(actualInvocationName, is(methodName));
    }
}
