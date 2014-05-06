package com.googlecode.yatspec.plugin.yadic;

import com.googlecode.totallylazy.Classes;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.Containers;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestLogger;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import static com.googlecode.totallylazy.Closeables.close;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContainerTestState extends TestState {
    public Container container;

    public static <T> void then(T actual, org.hamcrest.Matcher<? super T> matcher) {
        assertThat(actual, matcher);
    }

    public static <T> void and(T actual, org.hamcrest.Matcher<? super T> matcher) {
        then(actual, matcher);
    }

    @Before
    public void startupApp() {
        container = Containers.closeableContainer();
        container.addInstance(TestState.class, this);
        container.addInstance(TestLogger.class, this);
        container.addInstance(InterestingGivens.class, this.interestingGivens);
        container.addInstance(CapturedInputAndOutputs.class, this.capturedInputAndOutputs);
    }

    @After
    public void shutdownApp() throws IOException {
        close(container);
    }

    public void when(Object result) {
        given(result);
    }

    public void and(Object result) {
        given(result);
    }

    public void given(Object result) {
        if (result == null) return;
        for (Class aClass : Classes.allClasses(result.getClass())) {
            if (container.contains(aClass)) return;
            container.addInstance(aClass, result);
        }
    }

    public <T> T the(Class<T> aClass) {
        return a(aClass);
    }

    public <T> T that(Class<T> aClass) {
        return a(aClass);
    }

    public <T> T a(Class<T> aClass) {
        if (!container.contains(aClass)) container.add(aClass);
        return container.get(aClass);
    }
}
