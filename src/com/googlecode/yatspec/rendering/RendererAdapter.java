package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;

class RendererAdapter<T> implements AttributeRenderer {
    private final Renderer<T> renderer;

    RendererAdapter(Renderer<T> renderer) {
        this.renderer = renderer;
    }

    @SuppressWarnings({"unchecked"})
    public String toString(Object o) {
        try {
            return renderer.render((T) o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(Object o, String s) {
        return toString(o);
    }
}
