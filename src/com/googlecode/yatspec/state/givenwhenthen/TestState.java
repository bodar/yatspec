package com.googlecode.yatspec.state.givenwhenthen;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
public class TestState implements TestLogger, WithTestState {
    public InterestingGivens interestingGivens = new InterestingGivens();
    public CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();

    public TestState given(GivensBuilder builder) throws Exception {
        interestingGivens = builder.build(interestingGivens);
        return this;
    }

    public TestState and(GivensBuilder builder) throws Exception {
        return given(builder);
    }

    public TestState when(ActionUnderTest actionUndertest) throws Exception {
        capturedInputAndOutputs = actionUndertest.execute(interestingGivens, capturedInputAndOutputs);
        return this;
    }

    public <ItemOfInterest> TestState then(StateExtractor<ItemOfInterest> extractor, Matcher<? super ItemOfInterest> matcher) throws Exception {
        assertThat(extractor.execute(capturedInputAndOutputs), matcher);
        return this;
    }

    public synchronized void log(String name, Object value) {
        int count = 1;
        String keyName = name;
        while (capturedInputAndOutputs.contains(keyName)) {
            keyName = name + count;
            count++;
        }
        capturedInputAndOutputs.add(keyName, value);
    }

    public TestState testState() {
        return this;
    }
}