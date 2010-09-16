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
    @Table({@Row({"9", "3.0"}),
            @Row({"16", "4.0"})})
    public void takeTheSquareRoot(String radicand, String result) throws Exception {
        given(theRadicand(radicand));
        when(weTakeTheSquareRoot());
        then(theResult(), is(result));
    }


    private GivensBuilder theRadicand(final String number) {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                return interestingGivens.add(RADICAND, number);
            }
        };
    }

    private ActionUnderTest weTakeTheSquareRoot() {
        return new ActionUnderTest() {
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) {
                int number = Integer.valueOf(interestingGivens.getType(RADICAND, String.class));
                return capturedInputAndOutputs.add(RESULT, String.valueOf(Math.sqrt(number)));
            }
        };
    }

    private StateExtractor<String> theResult() {
        return new StateExtractor<String>() {
            public String execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return capturedInputAndOutputs.getType(RESULT, String.class);
            }
        };
    }


}
