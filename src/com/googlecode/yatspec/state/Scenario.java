package com.googlecode.yatspec.state;

import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.join;

public class Scenario {
    private TestState testState = new TestState();
    private final String name;
    private final List<String> specification;
    private Throwable exception;
    private boolean wasRun = false;


    public Scenario(String name, List<String> specification) {
        this.name = name;
        this.specification = specification;
    }

    public String getName() {
        return name;
    }

    public void setTestState(TestState testState) {
        this.testState = testState;
    }

    public Map<String, Object> getLogs() {
        return testState.capturedInputAndOutputs;
    }

    public Map<String, Object> getInterestingGivens() {
        return testState.interestingGivens;
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

    public List<String> getSpecification() {
        return specification;
    }

    public boolean wasRun() {
        return wasRun;
    }

    public void hasRun(boolean value) {
        wasRun = value;
    }

    public String getMessage() {
        String result = "Test not run";
        if(wasRun()){
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
        if(wasRun()){
            return Status.Passed;
        }
        return Status.NotRun;
    }
}
