package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.givenwhenthen.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;

@RunWith(SpecRunner.class)
public class SpecificationExampleTest extends TestState {
    private static final String RADICAND = "Radicand";
    private static final String RESULT = "Result";

    @Test
    public void simpleMathsTest() throws Exception {
        given(theNumber(9));
        when(weTakeTheSquareRoot());
        then(theResult(), is(3));
    }


    private GivensBuilder theNumber(final int number) {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                interestingGivens.add(RADICAND, number);
                return interestingGivens;
            }
        };
    }

    private ActionUnderTest weTakeTheSquareRoot() {
        return new ActionUnderTest() {
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) {
                int number = interestingGivens.getType(RADICAND, Integer.class);
                capturedInputAndOutputs.add(RESULT, (int)Math.sqrt(number));
                return capturedInputAndOutputs;
            }
        };
    }

    private StateExtractor<Integer> theResult() {
        return new StateExtractor<Integer>() {
            public Integer execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return capturedInputAndOutputs.getType(RESULT, Integer.class);
            }
        };
    }


}
