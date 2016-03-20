package com.googlecode.yatspec.junit;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

import static com.googlecode.totallylazy.Sequences.sequence;

public class VarargsParameterResolver implements ParameterResolver {

    @Override
    public Object[] resolveParameters(Row row, Class<?> testClass, Method testMethod) throws Exception {
        final Object[] suppliedParams = row.value();
        final Class<?>[] requiredParams = testMethod.getParameterTypes();

        if (!testMethod.isVarArgs()) {
            return suppliedParams;
        }

        if (isMissingRequiredArguments(suppliedParams, requiredParams)) {
            throw new IllegalArgumentException("Missing parameters.");
        }

        return sequence(suppliedParams).take(Math.min(requiredParams.length - 1, suppliedParams.length)).append(getVarargsFrom(suppliedParams, requiredParams)).toArray();
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
}
