package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.ResultMetadata;
import com.googlecode.yatspec.state.ResultMetadataOnly;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

public interface SpecResultMetadataListener extends SpecResultListener {
    void completeMetadata(File yatspecOutputDir, Collection<ResultMetadata> results) throws Exception;
    default void complete(File yatspecOutputDir, Result result) throws Exception {
        completeMetadata(yatspecOutputDir, Collections.singleton(ResultMetadataOnly.fromResult(result)));
    }
}
