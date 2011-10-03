package com.googlecode.yatspec.rendering;

public class DontHighlightRenderer<T> implements Renderer<T> {
    public String render(T instance) throws Exception {
        return "<div class='nohighlight'>" + new ToStringRenderer().render(instance) + "</div>";
    }
}
