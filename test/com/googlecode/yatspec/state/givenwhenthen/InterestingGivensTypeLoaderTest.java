package com.googlecode.yatspec.state.givenwhenthen;

import org.junit.Before;
import org.junit.Test;

import static com.googlecode.yatspec.state.givenwhenthen.InterestingGivensTypeLoader.interestingGivensType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterestingGivensTypeLoaderTest {
    private InterestingGivens interestingGivens;
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final Class<String> CLASS = String.class;

    @Before
    public void setUp() {
        interestingGivens = new InterestingGivens();
        interestingGivens.add(KEY, VALUE);
        interestingGivens.add(VALUE);
    }

    @Test
    public void shouldGetTypeByClass() throws Exception {
        assertThat(interestingGivensType(interestingGivens, CLASS).call(), is(VALUE));
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsExceptionWhenTypeByClassIsNotFound() throws Exception {
        interestingGivensType(interestingGivens, Integer.class).call();
    }


    @Test
    public void shouldGetTypeByKeyAndClass() throws Exception {
        assertThat(interestingGivensType(interestingGivens, KEY, CLASS).call(), is(VALUE));
    }

    @Test(expected = ClassCastException.class)
    public void throwsExceptionWhenTypeByKeyAndClassIsNotFound() throws Exception {
        interestingGivensType(interestingGivens, KEY, Integer.class).call();
    }

}
