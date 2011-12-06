package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.state.StatusPriority.statusPriority;
import static com.googlecode.yatspec.state.TestMethods.status;

public class Results {
    public static Callable1<Result, Iterable<TestMethod>> testMethods() {
        return new Callable1<Result, Iterable<TestMethod>>() {
            @Override
            public Iterable<TestMethod> call(Result result) throws Exception {
                return result.getTestMethods();
            }
        };
    }

    public static Callable1<Result, String> packageName() {
        return new Callable1<Result, String>() {
            @Override
            public String call(Result result) throws Exception {
                return result.getPackageName();
            }
        };
    }

    public static Callable1<Result, Status> resultStatus(){
        return new Callable1<Result, Status>() {
            @Override
            public Status call(Result result) throws Exception {
                return sequence(result.getTestMethods()).
                        map(status()).
                        sortBy(statusPriority()).
                        headOption().getOrElse(Status.Passed);
            }
        };
    }
}
