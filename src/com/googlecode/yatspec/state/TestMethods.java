package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;

public class TestMethods {
    public static Callable1<? super TestMethod, Status> status() {
        return new Callable1<TestMethod, Status>() {
            @Override
            public Status call(TestMethod testMethod) throws Exception {
                return testMethod.getStatus();
            }
        };
    }
}
