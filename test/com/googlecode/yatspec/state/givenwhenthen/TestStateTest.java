package com.googlecode.yatspec.state.givenwhenthen;

import org.junit.Test;

import static org.junit.Assert.assertSame;
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
}
