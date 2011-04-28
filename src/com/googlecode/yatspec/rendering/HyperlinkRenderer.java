package com.googlecode.yatspec.rendering;

import static java.lang.String.format;

public final class HyperlinkRenderer<T> implements Renderer<T> {
    private final String urlFormat;
    private final Renderer<T> delegateRenderer;
    private static final String HYPERLINK_FORMAT = "<a href=\"%s\">%s</a>";

    public HyperlinkRenderer(String urlFormat, Renderer<T> delegateRenderer) {
        this.urlFormat = urlFormat;
        this.delegateRenderer = delegateRenderer;
    }

    @Override
    public String render(T value) throws Exception {
        String render = delegateRenderer.render(value);
        return isEmpty(render) ? render : format(HYPERLINK_FORMAT, format(urlFormat, render), render);
    }

    private static boolean isEmpty(String render) {
        return render == null || render.isEmpty();
    }
}
