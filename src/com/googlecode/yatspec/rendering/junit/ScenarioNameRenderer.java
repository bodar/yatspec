package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.state.ScenarioName;

import static com.googlecode.totallylazy.Sequences.sequence;

public class ScenarioNameRenderer implements Renderer<ScenarioName> {

    @Override
    public String render(ScenarioName scenarioName) {
        return scenarioName.getMethodName() + "(" + sequence(scenarioName.getRow()).toString(", ") + ")";
    }
}
