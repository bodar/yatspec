package com.googlecode.yatspec.state;

import java.lang.annotation.Annotation;
import java.util.List;

public interface TestMethodMetadata {
    String getName();

    String getDisplayName();

    String getDisplayLinkName();

    Status getStatus();

    String getUid();

    String getPackageName();

    List<Annotation> getAnnotations();

    String getTestClassName();
}
