package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.yatspec.rendering.Renderer;

import java.util.Map;

public interface WithCustomWikiRendering {
    Map<Class, Renderer> getCustomWikiRenderers();
}
