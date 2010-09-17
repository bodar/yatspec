package com.googlecode.yatspec.state;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.TestParser;
import com.googlecode.yatspec.parsing.Text;
import jedi.functional.Functor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.yatspec.state.TestResult.getNotesValue;
import static jedi.functional.FunctionalPrimitives.collect;
import static org.apache.commons.lang.StringUtils.join;


public class TestMethod {
    private final Method method;
    private final String methodName;
    private final ScenarioTable scenarioTable;
    private final List<String> specification;
    private Map<String, Scenario> scenarioResults = new LinkedHashMap<String, Scenario>();

    public TestMethod(Method method, String methodName, List<String> methodBody, ScenarioTable scenarioTable) {
        this.method = method;
        this.methodName = methodName;
        this.scenarioTable = scenarioTable;
        this.specification = methodBody;
        buildUpScenarios();
    }

    private void buildUpScenarios() {
        if (scenarioTable.isEmpty()) {
            scenarioResults.put(methodName, new Scenario("", collect(specification, wordify())));
        } else {
            for (List<String> row : scenarioTable.getRows()) {
                scenarioResults.put(buildName(methodName, row), new Scenario(buildName(methodName, row), replaceScenarioData(scenarioTable.getHeaders(), row)));
            }
        }
    }


    private List<String> replaceScenarioData(final List<String> headers, final List<String> values) {
        final List<String> substituted = collect(specification, new Functor<String, String>() {
            public String execute(String specificationLine) {
                String result = specificationLine;
                for (int i = 0; i < headers.size(); i++) {
                    String header = headers.get(i);
                    String value = values.get(i);
                    result = result.replaceAll("([^\\w\"])" + header + "([^\\w\"])", "$1" + displayValue(value) + "$2" );
                }
                return result;
            }
        });
        return collect(substituted, wordify());
    }

    private String displayValue(String value) {
        if (value.matches("[A-Z0-9]*")) {
            return value;
        }
        return "\"" + value + "\"";
    }

    private Functor<String, String> wordify() {
        return new Functor<String, String>() {
            public String execute(String value) {
                return Text.wordify(value);
            }
        };
    }

    public String getName() {
        return methodName;
    }

    public String getDisplayName() {
        return Text.wordify(methodName);
    }

    public Status getStatus() {
        final List<Status> statuses = collect(getScenarios(), new Functor<Scenario, Status>() {
            public Status execute(Scenario scenario) {
                return scenario.getStatus();
            }
        });
        if (statuses.contains(Status.Failed)) {
            return Status.Failed;
        }
        if (statuses.contains(Status.NotRun)) {
            return Status.NotRun;
        }
        return Status.Passed;
    }

    public List<String> getSpecification() {
        return specification;
    }

    @Override
    public String toString() {
        return getName() + TestParser.LINE_SEPARATOR + getSpecification();
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

    public String getNotes() {
        return getNotesValue(method.getAnnotation(Notes.class));
    }

    public static String buildName(String methodName, List<String> scenarioData) {
        return methodName + "(" + join(scenarioData, ", ") + ")";
    }

}
