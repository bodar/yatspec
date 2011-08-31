package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Math.sqrt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpecRunner.class)
public abstract class AbstractTestCase extends TestState {
    @Test
    public void testInParentClass() throws Exception {
        assertThat(sqrt(9), is(3.0));
    }

}
