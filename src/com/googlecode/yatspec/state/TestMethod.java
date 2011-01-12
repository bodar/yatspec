package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.TestParser;
import com.googlecode.yatspec.parsing.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.state.TestResult.getNotesValue;
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
            scenarioResults.put(methodName, new Scenario("", sequence(specification).map(wordify()).toList()));
        } else {
            for (List<String> row : scenarioTable.getRows()) {
                scenarioResults.put(buildName(methodName, row), new Scenario(buildName(methodName, row), replaceScenarioData(scenarioTable.getHeaders(), row)));
            }
        }
    }


    private List<String> replaceScenarioData(final List<String> headers, final List<String> values) {
        return sequence(specification).map(new Callable1<String, String>() {
            public String call(String specificationLine) {
                String result = specificationLine;
                for (int i = 0; i < headers.size(); i++) {
                    String header = headers.get(i);
                    String value = values.get(i);
                    result = result.replaceAll("([^\\w\"])" + header + "([^\\w\"])", "$1" + displayValue(value) + "$2" );
                }
                return result;
            }
        }).map(wordify()).toList();
    }

    private String displayValue(String value) {
        if (value.matches("[A-Z0-9]*")) {
            return value;
        }
        return "\"" + value + "\"";
    }

    public static Callable1<String, String> wordify() {
        return new Callable1<String, String>() {
            public String call(String value) {
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
        final Sequence<Status> statuses = sequence(getScenarios()).map(new Callable1<Scenario, Status>() {
            public Status call(Scenario scenario) {
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

     public String getUid() {
        return Integer.toString(hashCode());
    }
}
