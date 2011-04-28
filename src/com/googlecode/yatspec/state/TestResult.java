package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.TestParser;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.Renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.Text.wordify;

public class TestResult implements Result {

    private final Class<?> klass;
    private List<TestMethod> testMethods;
    private Map<Class, Renderer> customRenderers = new HashMap<Class, Renderer>();
    private Content customHeaderContent;

    public TestResult(Class<?> klass) {
        this.klass = klass;
    }

    @Override
    public Class<?> getTestClass() {
        return klass;
    }

    @Override
    public List<TestMethod> getTestMethods() throws Exception {
        if(testMethods == null){
            testMethods = TestParser.parseTestMethods(klass);
        }
        return testMethods;
    }

    @Override
    public Scenario getScenario(String name) throws Exception {
        final Scenario testScenario = findScenario(name);
        testScenario.hasRun(true);
        return testScenario;
    }

    @Override
    public String getName() {
        String className = getTestClass().getSimpleName();
        if(className.endsWith("Test")){
            className = removeTestFrom(className);
        }
        return wordify(className);
    }

    private static String removeTestFrom(String className) {
        final int index = className.lastIndexOf("Test");
        return className.substring(0, index);
    }

    private Scenario findScenario(final String name) throws Exception {
        return sequence(getTestMethods()).filter(hasScenario(name)).head().getScenario(name);
    }

    private static Predicate<TestMethod> hasScenario(final String name) {
        return new Predicate<TestMethod>() {
            public boolean matches(TestMethod testMethod) {
                return testMethod.hasScenario(name);
            }
        };
    }

    @Override
    public Notes getNotes() throws Exception {
        return getTestClass().getAnnotation(Notes.class);
    }

    @Override
    public void mergeCustomRenderers(Map<Class, Renderer> customRenderers) {
        this.customRenderers.putAll(customRenderers);
    }

    @Override
    public Map<Class, Renderer> getCustomRenderers() {
        return customRenderers;
    }

    @Override
    public void mergeCustomHeaderContent(Content customHeaderContent) {
        this.customHeaderContent = customHeaderContent;
    }

    @Override
    public Content getCustomHeaderContent() {
        return customHeaderContent;
    }


}
