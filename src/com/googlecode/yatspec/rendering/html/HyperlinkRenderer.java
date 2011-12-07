package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.rendering.Renderer;

import static java.lang.String.format;


public class HyperlinkRenderer<T> implements Renderer<T> {
    private final Renderer<T> delegateRenderer;
    private final String regexPattern;
    private final String replacementPattern;

    public HyperlinkRenderer(final String urlFormat, Renderer<T> delegateRenderer) {
        this(urlFormat, "[\\w-\\s]+", delegateRenderer);
    }

    public HyperlinkRenderer(final String urlFormat, final String regexPattern, Renderer<T> delegateRenderer) {
        this(delegateRenderer, regexPattern, format("<a href='%s'>$0</a>", urlFormat.replace("%s", "$0")));
    }

    public HyperlinkRenderer(Renderer<T> delegateRenderer, String regexPattern, String replacementPattern) {
        this.delegateRenderer = delegateRenderer;
        this.regexPattern = regexPattern;
        this.replacementPattern = replacementPattern;
    }

    @Override
    public String render(T value) throws Exception {
        String valueStr = delegateRenderer.render(value);
        return valueStr != null ? valueStr.replaceAll(regexPattern, replacementPattern) : null;
    }
}