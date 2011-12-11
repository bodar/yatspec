package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.yatspec.state.TestMethod;

public interface TagFinder {
    Iterable<String> tags(TestMethod testMethod);
}
