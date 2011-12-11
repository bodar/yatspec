package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.state.Result;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Index {
    private final List<Result> files = new CopyOnWriteArrayList<Result>();

    public Index add(Result result) {
        files.add(result);
        return this;
    }

    public Sequence<Result> entries() {
        return sequence(files);
    }
}
