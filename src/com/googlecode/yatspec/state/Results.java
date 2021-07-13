package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.state.StatusPriority.statusPriority;
import static com.googlecode.yatspec.state.TestMethods.status;

public class Results {
    public static Callable1<ResultMetadata, Iterable<TestMethodMetadata>> testMethods() {
        return new Callable1<ResultMetadata, Iterable<TestMethodMetadata>>() {
            @Override
            public Iterable<TestMethodMetadata> call(ResultMetadata result) throws Exception {
                return result.getTestMethodMetadata();
            }
        };
    }

    public static Callable1<ResultMetadata, String> packageName() {
        return new Callable1<ResultMetadata, String>() {
            @Override
            public String call(ResultMetadata result) throws Exception {
                return result.getPackageName();
            }
        };
    }

    public static Callable1<ResultMetadata, Status> resultStatus(){
        return new Callable1<ResultMetadata, Status>() {
            @Override
            public Status call(ResultMetadata result) throws Exception {
                return sequence(result.getTestMethodMetadata()).
                        map(status()).
                        sortBy(statusPriority()).
                        headOption().getOrElse(Status.Passed);
            }
        };
    }
}
