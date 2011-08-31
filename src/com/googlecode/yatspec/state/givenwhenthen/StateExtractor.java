package com.googlecode.yatspec.state.givenwhenthen;

public interface StateExtractor<ItemOfInterest> {
     ItemOfInterest execute(CapturedInputAndOutputs inputAndOutputs) throws Exception;
}
