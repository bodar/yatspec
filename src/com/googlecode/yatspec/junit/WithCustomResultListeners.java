package com.googlecode.yatspec.junit;

public interface WithCustomResultListeners {
    Iterable<SpecResultListener> getResultListeners() throws Exception;
}
