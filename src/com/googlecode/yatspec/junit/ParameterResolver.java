package com.googlecode.yatspec.junit;

import java.lang.reflect.Method;

public interface ParameterResolver {
    Object[] resolveParameters(Row row, Class<?> testClass, Method testMethod) throws Exception;
}
