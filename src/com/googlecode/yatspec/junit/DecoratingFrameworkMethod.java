package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.rendering.ScenarioNameRendererFactory;
import com.googlecode.yatspec.state.ScenarioName;
import org.junit.runners.model.FrameworkMethod;

import java.lang.reflect.Array;
import java.util.Arrays;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;

public class DecoratingFrameworkMethod extends FrameworkMethod {
    private final Row row;

    public DecoratingFrameworkMethod(FrameworkMethod method, Row row) {
        super(method.getMethod());
        this.row = row;
    }

    @Override
    public Object invokeExplosively(Object target, Object... params) throws Throwable {
        final Object[] suppliedParams = row.value();
        final Class<?>[] requiredParams = getMethod().getParameterTypes();

        if (requiresNoVarArgs(requiredParams)) {
            return super.invokeExplosively(target, suppliedParams);
        }

        if (isMissingRequiredArguments(suppliedParams, requiredParams)) {
            throw new IllegalArgumentException("Missing parameters.");
        }

        final Sequence<Object> preparedParams = sequence(suppliedParams).take(Math.min(requiredParams.length - 1, suppliedParams.length)).append(getVarargsFrom(suppliedParams, requiredParams));
        return super.invokeExplosively(target, preparedParams.toArray());
    }

    private boolean requiresNoVarArgs(Class<?>[] methodParams) {
        return !methodParams[methodParams.length - 1].isArray();
    }

    private boolean isMissingRequiredArguments(Object[] input, Class<?>[] expected) {
        return input.length < expected.length - 1;
    }

    private boolean hasVarArgsSupplied(Object[] input, Class<?>[] expected) {
        return input.length >= expected.length;
    }

    private Object getVarargsFrom(Object[] suppliedParams, Class<?>[] requiredParams) {
        if (hasVarArgsSupplied(suppliedParams, requiredParams)) {
            return Arrays.copyOfRange(suppliedParams, requiredParams.length - 1, suppliedParams.length);
        }
        return Array.newInstance(requiredParams[requiredParams.length - 1].getComponentType(), 0);
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
