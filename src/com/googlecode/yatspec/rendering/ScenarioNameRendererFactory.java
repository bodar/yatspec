package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.rendering.junit.HumanReadableScenarioNameRenderer;
import com.googlecode.yatspec.state.ScenarioName;

import static com.googlecode.yatspec.Creator.create;
import static java.lang.Class.forName;
import static java.lang.System.getProperty;

public class ScenarioNameRendererFactory {
    public static final String SCENARIO_NAME_RENDERER = "yatspec.scenario.name.renderer";

    public static void setScenarioNameRenderer(Class<? extends ScenarioNameRenderer> aClass) {
        System.setProperty(SCENARIO_NAME_RENDERER, aClass.getName());
    }

    public static ScenarioNameRenderer renderer() {
        ScenarioNameRenderer renderer;
        try {
            renderer = create(forName(getProperty(SCENARIO_NAME_RENDERER, HumanReadableScenarioNameRenderer.class.getName())));
        } catch (Exception e) {
            renderer = new HumanReadableScenarioNameRenderer();
        }
        return renderer;
    }
}
