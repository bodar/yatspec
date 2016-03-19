package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.rendering.ScenarioNameRendererFactory;
import com.googlecode.yatspec.state.ScenarioName;
import org.junit.runners.model.FrameworkMethod;

import static com.googlecode.yatspec.junit.ParameterResolverFactory.parameterResolver;
import static java.util.Arrays.asList;

public class DecoratingFrameworkMethod extends FrameworkMethod {

    private final Row row;

    public DecoratingFrameworkMethod(FrameworkMethod method, Row row) {
        super(method.getMethod());
        this.row = row;
    }

    @Override
    public Object invokeExplosively(Object test, Object... ignored) throws Throwable {
        Object[] testMethodParameters = parameterResolver().resolveParameters(row, test.getClass(), getMethod());
        return super.invokeExplosively(test, testMethodParameters);
    }

    @Override
    public String getName() {
        ScenarioName scenarioName = new ScenarioName(super.getName(), asList(row.value()));
        return ScenarioNameRendererFactory.renderer().render(scenarioName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DecoratingFrameworkMethod that = (DecoratingFrameworkMethod) o;

        if (!row.equals(that.row)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + row.hashCode();
        return result;
    }
}