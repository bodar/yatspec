package com.googlecode.yatspec.rendering;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;

import java.util.Map;

public class Renderers {
    public static Callable2<EnhancedStringTemplateGroup, Map.Entry<Class, Renderer>, EnhancedStringTemplateGroup> registerRenderer() {
        return new Callable2<EnhancedStringTemplateGroup, Map.Entry<Class, Renderer>, EnhancedStringTemplateGroup>() {
            @Override
            public EnhancedStringTemplateGroup call(EnhancedStringTemplateGroup group, Map.Entry<Class, Renderer> entry) throws Exception {
                return group.registerRenderer(Predicates.instanceOf(entry.getKey()), HtmlResultRenderer.callable(entry.getValue()));
            }
        };
    }
}
