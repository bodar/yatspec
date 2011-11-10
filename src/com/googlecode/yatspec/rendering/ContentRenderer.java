package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;

import java.io.File;

public interface ContentRenderer<T> extends Renderer<T> {
    File outputFile(File outputDirectory, T instance);
}
