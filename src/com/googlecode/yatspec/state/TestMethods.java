package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;

public class TestMethods {
    public static Callable1<? super TestMethodMetadata, Status> status() {
        return new Callable1<TestMethodMetadata, Status>() {
            @Override
            public Status call(TestMethodMetadata testMethod) throws Exception {
                return testMethod.getStatus();
            }
        };
    }
}
