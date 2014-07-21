package com.googlecode.yatspec.state.givenwhenthen;

import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestStateTest {
    @Test
    public void shouldImplementCorrectInterfaceForSpecRunner() throws Exception {
        final TestState state = new TestState();
        assertTrue(WithTestState.class.isAssignableFrom(state.getClass()));
    }

    @Test
    public void shouldReturnSensibleValueFromInterfaceMethod(){
        final TestState state = new TestState();
        assertSame(state, state.testState());
    }

    @Test
    public void loggerShouldHandleNamingForMultiThreadCalls() {
        final TestState state = new TestState();
        sequence(Sequences.repeat(pair("logTitle", "content")).take(1000)).mapConcurrently(log(state), newFixedThreadPool(1000)).realise();
        assertThat(state.capturedInputAndOutputs.getTypes().size(), is(1000));
    }

    private Block<Pair<String, String>> log(final TestState state) {
        return new Block<Pair<String, String>>() {
            @Override
            protected void execute(Pair<String, String> pair) throws Exception {
                state.log(pair.first(), pair.second());
            }
        };
    }

}
