package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.TestResult;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Index {
    private final Map<File, Result> files = new ConcurrentHashMap<File, Result>();

    public Index put(File file, Result result) {
        files.put(file, result);
        return this;
    }

    public Sequence<Pair<File, Result>> entries() {
        return Maps.pairs(files);
    }
}
