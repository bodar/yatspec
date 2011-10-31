package com.googlecode.yatspec.rendering.html;

import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.ToStringRenderer;

public class DontHighlightRenderer<T> implements Renderer<T> {
    public String render(T instance) throws Exception {
        return "<div class='nohighlight'>" + new ToStringRenderer().render(instance) + "</div>";
    }
}
