package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.TestMethod;
import org.junit.runners.model.FrameworkMethod;

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
        return TestMethod.buildName(super.getName(), asList(row.value())) ;
    }
}
