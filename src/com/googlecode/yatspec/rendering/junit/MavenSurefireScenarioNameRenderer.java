package com.googlecode.yatspec.rendering.junit;

import com.googlecode.yatspec.rendering.ScenarioNameRenderer;
import com.googlecode.yatspec.state.ScenarioName;

import static com.googlecode.totallylazy.Sequences.sequence;

public class MavenSurefireScenarioNameRenderer implements ScenarioNameRenderer {

    @Override
    public String render(ScenarioName scenarioName) {
        if (scenarioName.hasEmptyRow()) {
            return scenarioName.getMethodName();
        }
        
        return scenarioName.getMethodName() + "__" + sequence(scenarioName.getRow()).toString("_");
    }
}
