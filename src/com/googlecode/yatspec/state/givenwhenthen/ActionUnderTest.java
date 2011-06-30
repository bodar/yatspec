package com.googlecode.yatspec.state.givenwhenthen;

public interface ActionUnderTest {
    CapturedInputAndOutputs execute(InterestingGivens givens, CapturedInputAndOutputs capturedInputAndOutputs) throws Exception;
}
