package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Scenario {
    private TestState testState = new TestState();
    private final String name;
    private Throwable exception;
    private boolean wasRun = false;
    private final List<Section> sections;

    public Scenario(String name, List<Section> sections) {
        this.name = name;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public void setTestState(TestState testState) {
        this.testState = testState;
    }

    public Map<LogKey, Object> getLogs() {
        Map<LogKey, Object> result = new LinkedHashMap<LogKey, Object>();
        for (Map.Entry<String, Object> entry : getCapturedInputAndOutputs().entrySet()) {
            result.put(new LogKey(entry.getKey()), entry.getValue());       
        }
        return result;
    }

    public Map<String, Object> getCapturedInputAndOutputs() {
        return testState.capturedInputAndOutputs.getTypes();
    }

    public Map<String, Object> getInterestingGivens() {
        return testState.interestingGivens.getTypes();
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean hasFailed() {
        return exception != null;
    }

    public boolean wasRun() {
        return wasRun;
    }

    public void hasRun(boolean value) {
        wasRun = value;
    }

    public String getMessage() {
        String result = "Test not run";
        if (wasRun()) {
            result = "Test passed";
        }
        if (hasFailed()) {
            result = getException().toString();
        }
        return result;
    }

    public Status getStatus() {
        if (hasFailed()) {
            return Status.Failed;
        }
        if (wasRun()) {
            return Status.Passed;
        }
        return Status.NotRun;
    }

    public String getUid() {
        return Integer.toString(hashCode());
    }

    public List<Section> getSections() {
        return sections;
    }
}
