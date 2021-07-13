package com.googlecode.yatspec.state;

import java.lang.annotation.Annotation;
import java.util.List;

public interface ResultMetadata {
    String getName();

    String getPackageName();

    List<TestMethodMetadata> getTestMethodMetadata();

    String getTestClassName();

    List<Annotation> getAnnotations();
}
