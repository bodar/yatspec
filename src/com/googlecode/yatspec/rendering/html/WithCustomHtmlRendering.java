package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.rendering.Renderer;

import java.util.Map;

public interface WithCustomHtmlRendering {
    Map<Class, Renderer> getCustomHtmlRenderers();
}
