package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.givenwhenthen.*;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Double.valueOf;
import static java.lang.Math.sqrt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpecRunner.class)
@Notes("This is a note on the whole class\n" +
        "It will preserve space")
public class SpecificationExampleTest extends TestState {
    private static final String RADICAND = "Radicand";
    private static final String RESULT = "Result";

    @Test
    public void reallySimpleExample() throws Exception {
        assertThat(sqrt(9), is(3.0));
    }

    @Test
    @Table({@Row({"9", "3.0"}),
            @Row({"16", "4.0"})})
    @Notes("This example combines table / row tests with specification and given when then")
    public void takeTheSquareRoot(String radicand, String result) throws Exception {
        given(theRadicand(radicand));
        when(weTakeTheSquareRoot());
        then(theResult(), is(valueOf(result)));
    }

    private GivensBuilder theRadicand(final String number) {
        return theRadicand(Integer.valueOf(number));
    }

    private GivensBuilder theRadicand(final int number) {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                return interestingGivens.add(RADICAND, number);
            }
        };
    }

    private ActionUnderTest weTakeTheSquareRoot() {
        return new ActionUnderTest() {
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) {
                int number = interestingGivens.getType(RADICAND, Integer.class);
                return capturedInputAndOutputs.add(RESULT, sqrt(number));
            }
        };
    }

    private StateExtractor<Double> theResult() {
        return new StateExtractor<Double>() {
            public Double execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return capturedInputAndOutputs.getType(RESULT, Double.class);
            }
        };
    }


}
