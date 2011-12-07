package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.ScenarioName;

public interface ScenarioNameRenderer extends Renderer<ScenarioName> {
    @Override
    String render(ScenarioName scenarioName);
}
