package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.Result;

import static com.googlecode.yatspec.junit.SpecRunner.outputDirectory;

public interface WithCustomResultListeners {
    default void complete(Result testResult) {
        try {
            for (SpecResultListener resultListener : getResultListeners()) {
                resultListener.complete(outputDirectory(), testResult);
            }
        } catch (Exception e) {
            System.out.println("Error while writing yatspec output");
            e.printStackTrace(System.out);
        }
    }

    Iterable<SpecResultListener> getResultListeners() throws Exception;
}
