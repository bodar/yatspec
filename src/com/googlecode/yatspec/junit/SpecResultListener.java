package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.Result;

import java.io.File;

public interface SpecResultListener {
    void complete(File yatspecOutputDir, Result result) throws Exception;
}
