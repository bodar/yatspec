package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.state.ScenarioName;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.googlecode.yatspec.fixture.RandomFixtures.anyString;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HumanReadableScenarioNameRendererTest {

    private Renderer<ScenarioName> renderer;

    @Before
    public void setUp() throws Exception {
        renderer = new HumanReadableScenarioNameRenderer();
    }

    @Test
    public void rendersAScenarioNameWithoutArgs() throws Exception {
        String methodName = anyString();
        String expectedInvocationName = methodName + "()";
        List<String> noArgs = emptyList();
        ScenarioName scenarioName = new ScenarioName(methodName, noArgs);

        String actualInvocationName = renderer.render(scenarioName);

        assertThat(actualInvocationName, is(expectedInvocationName));
    }

    @Test
    public void rendersAScenarioNameWithArgs() throws Exception {
        String methodName = anyString();
        String firstArg = anyString();
        String secondArg = anyString();
        String expectedInvocationName = String.format("%s(%s, %s)", methodName, firstArg, secondArg);
        List<String> someArgs = Arrays.asList(firstArg, secondArg);
        ScenarioName scenarioName = new ScenarioName(methodName, someArgs);

        String actualInvocationName = renderer.render(scenarioName);

        assertThat(actualInvocationName, is(expectedInvocationName));
    }
}
