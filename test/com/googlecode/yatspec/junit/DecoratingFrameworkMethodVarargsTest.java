package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.yatspec.fixture.VarargFixture;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.junit.DecoratingFrameworkMethodTest.row;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;

public class DecoratingFrameworkMethodVarargsTest {

    @Test
    public void shouldSupportMultipleParamsAndVarArgs() throws Throwable {
        testFixtureMethodCalled("someParamsAndVarargs");
    }

    @Test
    public void shouldSupportOneParamAndVarArgs() throws Throwable {
        testFixtureMethodCalled("oneParamAndVarargs");
    }

    @Test
    public void shouldSupportVarArgsOnly() throws Throwable {
        testFixtureMethodCalled("varargsOnly");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfLastArgumentIsAnArray() throws Throwable {
        checkNonVarargs(getMethod("arrayOnly"), singletonList("someParam"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnMissingParams() throws Throwable {
        checkNonVarargs(getMethod("someParamsAndVarargs"), singletonList("someParam"));
    }

    private void testFixtureMethodCalled(String methodName) throws Throwable {
        final Method methodUnderTest = getMethod(methodName);
        testVarargsForMethod(methodUnderTest);
    }

    private Method getMethod(String methodName) {
        return sequence(VarargFixture.class.getDeclaredMethods()).filter(Predicates.where(new Mapper<Method, String>() {
            @Override
            public String call(Method method) throws Exception {
                return method.getName();
            }
        }, Predicates.is(methodName))).headOption().getOrThrow(new RuntimeException("Couldn't find method called " + methodName));
    }

    public void testVarargsForMethod(Method underTest) throws Throwable {
        final List<String> varArgs = asList("a", "b", "c");
        final List<String> params = new ArrayList<String>();
        if (underTest.getParameterTypes().length > 1) {
            params.add("someParam");
        }
        if (underTest.getParameterTypes().length > 2) {
            params.add("anotherParam");
        }

        checkVarargs(underTest, asList("a"), params);

        checkVarargs(underTest, varArgs, params);

        checkNonVarargs(underTest, params);
    }

    private void checkVarargs(Method underTest, List<String> varArgs, List<String> params) throws Throwable {
        final List<String> paramsToUse = sequence(params).join(varArgs).toList();
        final Map<String, Object> result = checkNonVarargs(underTest, paramsToUse);

        assertThat((String[])result.get("lotsOfParams"), arrayContaining(varArgs.toArray()));
    }

    private Map<String, Object> checkNonVarargs(Method underTest, List<String> params) throws Throwable {
        DecoratingFrameworkMethod decoratingFrameworkMethod = new DecoratingFrameworkMethod(new FrameworkMethod(underTest), row(params));

        final Map<String, Object> result = (Map<String, Object>) decoratingFrameworkMethod.invokeExplosively(new VarargFixture());

        if (underTest.getParameterTypes().length > 1) {
            assertThat(result.get("someParam").toString(), is("someParam"));
        }
        if (underTest.getParameterTypes().length > 2) {
            assertThat(result.get("anotherParam").toString(), is("anotherParam"));
        }

        assertThat(result.size(), is(underTest.getParameterTypes().length));

        return result;
    }
}
