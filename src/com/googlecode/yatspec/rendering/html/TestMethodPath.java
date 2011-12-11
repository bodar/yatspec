package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.state.TestMethod;

import java.io.File;

import static java.lang.String.format;

public class TestMethodPath {
    public static String testMethodPath(TestMethod testMethod, File resultFile) {
        return format("file://%s#%s", resultFile, testMethod.getName());
    }
}
