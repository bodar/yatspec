package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.Option;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.Creator.create;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Class.forName;
import static java.lang.System.getProperty;

public class DefaultResultListeners implements WithCustomResultListeners {
    public static final String RESULT_RENDER = "yatspec.result.renderer";
    public static final String INDEX_ENABLE = "yatspec.index.enable";
    public static final String INDEX_RENDER = "yatspec.index.renderer";

    @Override
    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return sequence(resultListener()).join(indexListener());
    }

    private SpecResultListener resultListener() throws Exception {
        return create(forName(getProperty(RESULT_RENDER, HtmlResultRenderer.class.getName())));
    }

    private Option<SpecResultListener> indexListener() throws Exception {
        if (!parseBoolean(getProperty(INDEX_ENABLE))) {
            return none();
        }
        return some((SpecResultListener) create(forName(getProperty(INDEX_RENDER, HtmlIndexRenderer.class.getName()))));
    }
}
