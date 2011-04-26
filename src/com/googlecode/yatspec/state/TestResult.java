package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.yatspec.junit.Link;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.TestParser;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.Renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;

public class TestResult implements Result {

    private final Class<?> klass;
    private List<TestMethod> testMethods;
    private Map<Class, Renderer> customRenderers = new HashMap<Class, Renderer>();
    private Content customHeaderContent;

    public TestResult(Class<?> klass) {
        this.klass = klass;
    }

    public Class<?> getTestClass() {
        return klass;
    }

    public List<TestMethod> getTestMethods() throws Exception {
        if (testMethods == null) {
            testMethods = TestParser.parseTestMethods(klass);
        }
        return testMethods;
    }

    public Scenario getScenario(String name) throws Exception {
        final Scenario testScenario = findScenario(name);
        testScenario.hasRun(true);
        return testScenario;
    }

    public String getName() {
        String className = getTestClass().getSimpleName();
        if (className.endsWith("Test")) {
            className = removeTestFrom(className);
        }
        return Text.wordify(className);
    }

    private String removeTestFrom(String className) {
        final int index = className.lastIndexOf("Test");
        return className.substring(0, index);
    }

    private Scenario findScenario(final String name) throws Exception {
        return sequence(getTestMethods()).filter(hasScenario(name)).head().getScenario(name);
    }

    private Predicate<TestMethod> hasScenario(final String name) {
        return new Predicate<TestMethod>() {
            public boolean matches(TestMethod testMethod) {
                return testMethod.hasScenario(name);
            }
        };
    }

    public String getNotes() {
        final Notes annotation = getTestClass().getAnnotation(Notes.class);
        return getNotesValue(annotation);
    }

    public void mergeCustomRenderers(Map<Class, Renderer> customRenderers) {
        this.customRenderers.putAll(customRenderers);
    }

    public Map<Class, Renderer> getCustomRenderers() {
        return customRenderers;
    }

    public void mergeCustomHeaderContent(Content customHeaderContent) {
        this.customHeaderContent = customHeaderContent;
    }

    public Content getCustomHeaderContent() {
        return customHeaderContent;
    }

    public static String getNotesValue(Notes annotation) {
        return annotation == null ? null : annotation.value();
    }

    public LinkDetails getLink() {
        Link link = getTestClass().getAnnotation(Link.class);
        if (link == null) {
            return null;
        }
        return new LinkDetails(link);
    }

    public static class LinkDetails {
        private final Link link;

        public LinkDetails(Link link) {
            this.link = link;
        }

        public String getHref() {
            return link.href();
        }

        public String getText() {
            return link.text();
        }
    }
}
