package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.rendering.ScenarioNameRenderer;
import com.googlecode.yatspec.rendering.junit.HumanReadableScenarioNameRenderer;
import com.googlecode.yatspec.state.ScenarioName;
import org.junit.runners.model.FrameworkMethod;

import static com.googlecode.yatspec.Creator.create;
import static java.lang.Class.forName;
import static java.lang.System.getProperty;
import static java.util.Arrays.asList;

public class DecoratingFrameworkMethod extends FrameworkMethod {
    private final Row row;

    public DecoratingFrameworkMethod(FrameworkMethod method, Row row) {
        super(method.getMethod());
        this.row = row;
    }

    @Override
    public Object invokeExplosively(Object target, Object... params) throws Throwable {
        return super.invokeExplosively(target, (Object[]) row.value());
    }

    @Override
    public String getName() {
        ScenarioName scenarioName = new ScenarioName(super.getName(), asList(row.value()));
        return renderer().render(scenarioName);
    }

    public static ScenarioNameRenderer renderer() {
        ScenarioNameRenderer renderer;
        try {
            renderer = create(forName(getProperty(SpecRunner.SCENARIO_NAME_RENDERER, HumanReadableScenarioNameRenderer.class.getName())));
        } catch (Exception e) {
            renderer = new HumanReadableScenarioNameRenderer();
        }
        return renderer;
    }
}
