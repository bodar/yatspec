package com.googlecode.yatspec.state;

import com.googlecode.yatspec.rendering.junit.MavenSurefireScenarioNameRenderer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.googlecode.yatspec.fixture.RandomFixtures.anyString;
import static com.googlecode.yatspec.junit.SpecRunner.SCENARIO_NAME_RENDERER;
import static com.googlecode.yatspec.state.TestMethod.invocationName;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestMethodTest {

    private static final String MAVEN_SCENARIO_NAME_RENDERER = MavenSurefireScenarioNameRenderer.class.getName();

    @Test
    public void createsAnInvocationNameForAScenarioNameWithoutArgs() throws Exception {
        String methodName = anyString();
        String expectedInvocationName = methodName + "()";
        List<String> noArgs = emptyList();
        ScenarioName scenarioName = new ScenarioName(methodName, noArgs);

        String actualInvocationName = invocationName(scenarioName);

        assertThat(actualInvocationName, is(expectedInvocationName));
    }

    @Test
    public void createsAnInvocationNameForAScenarioNameWithArgs() throws Exception {
        String methodName = anyString();
        String firstArg = anyString();
        String secondArg = anyString();
        String expectedInvocationName = String.format("%s(%s, %s)", methodName, firstArg, secondArg);
        List<String> someArgs = Arrays.asList(firstArg, secondArg);
        ScenarioName scenarioName = new ScenarioName(methodName, someArgs);

        String actualInvocationName = invocationName(scenarioName);

        assertThat(actualInvocationName, is(expectedInvocationName));
    }
    
    @Test
    public void createsAMavenSurefireInvocationNameForAScenarioNameWithoutArgs() throws Exception {
        System.setProperty(SCENARIO_NAME_RENDERER, MAVEN_SCENARIO_NAME_RENDERER);
        String methodName = anyString();
        List<String> noArgs = emptyList();
        ScenarioName scenarioName = new ScenarioName(methodName, noArgs);

        String actualInvocationName = invocationName(scenarioName);

        assertThat(actualInvocationName, is(methodName));
    }
}
