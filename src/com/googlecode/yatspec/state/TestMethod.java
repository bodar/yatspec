package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.TestParser;
import com.googlecode.yatspec.parsing.Text;
import jedi.functional.Functor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jedi.functional.FunctionalPrimitives.collect;
import static com.googlecode.yatspec.state.Scenario.buildDisplayName;
import static com.googlecode.yatspec.state.Scenario.buildName;


public class TestMethod {
    private final String methodName;
    private final ScenarioTable scenarioTable;
    private final List<String> specification;
    private Map<String, Scenario> scenarioResults = new LinkedHashMap<String, Scenario>();

    public TestMethod(String methodName, List<String> methodBody, ScenarioTable scenarioTable) {
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
                scenarioResults.put(buildName(methodName, row), new Scenario(buildDisplayName(scenarioTable.getHeaders(), row), replaceScenarioData(scenarioTable.getHeaders(), row)));
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

}
