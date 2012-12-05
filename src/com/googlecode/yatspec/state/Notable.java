package com.googlecode.yatspec.state;

import java.lang.annotation.Annotation;
import java.util.List;

public interface Notable {
    List<Annotation> getAnnotations() throws Exception;
}
