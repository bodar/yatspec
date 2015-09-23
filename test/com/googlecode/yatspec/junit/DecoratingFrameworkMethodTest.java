package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.rendering.junit.HumanReadableScenarioNameRenderer;
import com.googlecode.yatspec.rendering.junit.MavenSurefireScenarioNameRenderer;
import com.googlecode.yatspec.state.ScenarioName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.yatspec.fixture.RandomFixtures.anyString;
import static com.googlecode.yatspec.fixture.RandomFixtures.pickOneOf;
import static com.googlecode.yatspec.rendering.ScenarioNameRendererFactory.SCENARIO_NAME_RENDERER;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DecoratingFrameworkMethodTest {
    private static final String MAVEN_SCENARIO_NAME_RENDERER = MavenSurefireScenarioNameRenderer.class.getName();
    private String originalScenarioNameRenderer;
    private List<String> args;
    private String methodName;
    private DecoratingFrameworkMethod decoratingFrameworkMethod;
    private Method method;

    @Before
    public void setUp() throws Exception {
        originalScenarioNameRenderer = System.getProperty(SCENARIO_NAME_RENDERER);
        args = asList(anyString(),anyString());
        method = method();
        methodName = method.getName();
        decoratingFrameworkMethod = new DecoratingFrameworkMethod(frameworkMethod(), row(args));
    }

    @After
    public void tearDown() throws Exception {
        if (null != originalScenarioNameRenderer) {
            System.setProperty(SCENARIO_NAME_RENDERER, originalScenarioNameRenderer);
        } else {
            System.clearProperty(SCENARIO_NAME_RENDERER);
        }
    }

    @Test
    public void getsNameUsingDefaultRenderer() throws Exception {
        System.clearProperty(SCENARIO_NAME_RENDERER);
        String expectedName = new HumanReadableScenarioNameRenderer().render(new ScenarioName(methodName, args));

        String actualName = decoratingFrameworkMethod.getName();

        assertThat(actualName, is(expectedName));
    }

    @Test
    public void getsNameUsingRenderSpecifiedBySystemProperty() throws Exception {
        System.setProperty(SCENARIO_NAME_RENDERER, MAVEN_SCENARIO_NAME_RENDERER);
        String expectedName = new MavenSurefireScenarioNameRenderer().render(new ScenarioName(methodName, args));

        String actualName = decoratingFrameworkMethod.getName();

        assertThat(actualName, is(expectedName));
    }

    private Method method() throws NoSuchMethodException {
        return pickOneOf(Object.class.getMethods());
    }

    private FrameworkMethod frameworkMethod() throws NoSuchMethodException {
        return new FrameworkMethod(method);
    }

    static Row row(final List<String> args) {
        return new Row() {
            @Override
            public String[] value() {
                return args.toArray(new String[0]);
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        };
    }
}
