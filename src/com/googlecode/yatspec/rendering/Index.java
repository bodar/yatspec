package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.state.ResultMetadata;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Index {
    private final List<ResultMetadata> files = new CopyOnWriteArrayList<ResultMetadata>();

    public Index addAll(Collection<ResultMetadata> results) {
        files.addAll(results);
        return this;
    }

    public Sequence<ResultMetadata> entries() {
        return sequence(files);
    }
}
