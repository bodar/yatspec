package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.state.ScenarioName;

import static com.googlecode.totallylazy.Sequences.sequence;

public class ScenarioNameRenderer implements com.googlecode.yatspec.rendering.ScenarioNameRenderer {

    @Override
    public String render(ScenarioName scenarioName) {
        return scenarioName.getMethodName() + "(" + sequence(scenarioName.getRow()).toString(", ") + ")";
    }
}
