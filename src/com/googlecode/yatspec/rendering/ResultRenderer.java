package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;

import java.io.File;

public interface ResultRenderer extends Renderer<Result> {
    File outputFile(File outputDirectory, Class<?> testClass);
}
