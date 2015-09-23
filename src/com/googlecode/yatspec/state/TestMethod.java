package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.ScenarioNameRendererFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.junit.YatspecAnnotation.methods.yatspecAnnotations;
import static java.lang.System.lineSeparator;

@SuppressWarnings({"unused"})
public class TestMethod {
    private final Class testClass;
    private final Method method;
    private final String methodName;
    private final ScenarioTable scenarioTable;
    private final JavaSource specification;
    private final Map<String, Scenario> scenarioResults = new LinkedHashMap<String, Scenario>();

    public TestMethod(Class testClass, Method method, String methodName, JavaSource methodBody, ScenarioTable scenarioTable) {
        this.testClass = testClass;
        this.method = method;
        this.methodName = methodName;
        this.scenarioTable = scenarioTable;
        this.specification = methodBody;
        buildUpScenarios();
    }

    private void buildUpScenarios() {
        if (scenarioTable.isEmpty()) {
            scenarioResults.put(methodName, new Scenario("", specification));
        } else {
            for (List<String> row : scenarioTable.getRows()) {
                ScenarioName scenarioName = new ScenarioName(methodName, row);
                String name = ScenarioNameRendererFactory.renderer().render(scenarioName);
                final List<String> oldValues = sequence(scenarioTable.getHeaders()).map(value(String.class)).toList();
                scenarioResults.put(name, new Scenario(name,
                        specification.replace(oldValues, createPossiblyVarargValueFrom(row, oldValues))));
            }
        }
    }

    private List<String> createPossiblyVarargValueFrom(List<String> newValues, List<String> oldValues) {
        Sequence<String> actualValues = sequence(newValues);
        if (oldValues.size() > newValues.size()) {
            actualValues = sequence(newValues).join(sequence("[]").cycle()).take(oldValues.size());
        } else if (newValues.size() > oldValues.size()) {
            actualValues = actualValues.take(oldValues.size() - 1).append(actualValues.drop(oldValues.size() - 1).toString("[", ", ", "]"));
        }
        return actualValues.toList();
    }

    private static <T> Callable1<? super Value<T>, T> value(Class<T> aClass) {
        return new Callable1<Value<T>, T>() {
            @Override
            public T call(Value<T> instance) throws Exception {
                return instance.value();
            }
        };
    }

    public String getName() {
        return methodName;
    }

    public String getDisplayName() {
        return Text.wordify(methodName);
    }

    public String getDisplayLinkName() {
        return getDisplayName().replace(' ', '_');
    }

    public Status getStatus() {
        return calculateStatus(sequence(getScenarios()).map(new Callable1<Scenario, Status>() {
            public Status call(Scenario scenario) {
                return scenario.getStatus();
            }
        }));
    }

    public static Status calculateStatus(final Sequence<Status> statuses) {
        if (statuses.contains(Status.Failed)) {
            return Status.Failed;
        }
        if (statuses.contains(Status.NotRun)) {
            return Status.NotRun;
        }
        return Status.Passed;
    }

    public JavaSource getSpecification() {
        return specification;
    }

    @Override
    public String toString() {
        return getName() + lineSeparator() + getSpecification();
    }

    public ScenarioTable getScenarioTable() {
        return scenarioTable;
    }

    public Scenario getScenario(String fullName) {
        return scenarioResults.get(fullName);
    }

    public List<Scenario> getScenarios() {
        return new ArrayList<Scenario>(scenarioResults.values());
    }

    public boolean hasScenario(String name) {
        return scenarioResults.get(name) != null;
    }

    public List<Annotation> getAnnotations() {
        return yatspecAnnotations(sequence(method.getAnnotations()));
    }

    public String getUid() {
        return Integer.toString(hashCode());
    }

    public String getPackageName() {
        return testClass.getPackage().getName();
    }

    public Class getTestClass() {
        return testClass;
    }
}
