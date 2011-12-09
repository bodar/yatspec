package com.googlecode.yatspec.state;

import java.util.List;

public class ScenarioName {
    private final String methodName;
    private final List<String> row;

    public ScenarioName(String methodName, List<String> row) {
        this.methodName = methodName;
        this.row = row;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getRow() {
        return row;
    }

    public boolean hasEmptyRow() {
        return getRow().isEmpty();
    }
}
