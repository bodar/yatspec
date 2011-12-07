package com.googlecode.yatspec.rendering.maven;

import com.googlecode.yatspec.state.ScenarioName;

import static com.googlecode.totallylazy.Sequences.sequence;

public class ScenarioNameRenderer implements com.googlecode.yatspec.rendering.ScenarioNameRenderer {

    @Override
    public String render(ScenarioName scenarioName) {
        if (scenarioName.hasEmptyRow()) {
            return scenarioName.getMethodName();
        }
        
        return scenarioName.getMethodName() + "__" + sequence(scenarioName.getRow()).toString("_");
    }
}
