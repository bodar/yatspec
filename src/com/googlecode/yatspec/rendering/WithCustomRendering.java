package com.googlecode.yatspec.rendering;

import java.util.Map;

public interface WithCustomRendering {
    Map<Class, Renderer> getCustomRenderers();
}
