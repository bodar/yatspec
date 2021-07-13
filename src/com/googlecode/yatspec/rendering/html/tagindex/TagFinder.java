package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.yatspec.state.TestMethodMetadata;

public interface TagFinder {
    Iterable<String> tags(TestMethodMetadata testMethod);
}
