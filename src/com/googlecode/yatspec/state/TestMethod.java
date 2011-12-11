package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.ScenarioNameRendererFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;


@SuppressWarnings({"unused"})
public class TestMethod implements Notable {
    private final Method method;
    private final String methodName;
    private final ScenarioTable scenarioTable;
    private final JavaSource specification;
    private final Map<String, Scenario> scenarioResults = new LinkedHashMap<String, Scenario>();

    public TestMethod(Method method, String methodName, JavaSource methodBody, ScenarioTable scenarioTable) {
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
                scenarioResults.put(name, new Scenario(name,
                        specification.replace(sequence(scenarioTable.getHeaders()).map(value(String.class)).toList(), row)));
            }
        }
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
        return getName() + System.getProperty("line.separator") + getSpecification();
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

    @Override
    public Notes getNotes() {
        return method.getAnnotation(Notes.class);
    }

    public String getUid() {
        return Integer.toString(hashCode());
    }

    public String getPackageName() {
        return method.getDeclaringClass().getPackage().getName();
    }
}