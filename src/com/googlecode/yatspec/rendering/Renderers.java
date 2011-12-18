package com.googlecode.yatspec.rendering;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;

import java.util.Map;

public class Renderers {
    public static Callable2<EnhancedStringTemplateGroup, Pair<Predicate, Renderer>, EnhancedStringTemplateGroup> registerRenderer() {
        return new Callable2<EnhancedStringTemplateGroup, Pair<Predicate, Renderer>, EnhancedStringTemplateGroup>() {
            @Override
            public EnhancedStringTemplateGroup call(EnhancedStringTemplateGroup group, Pair<Predicate, Renderer> entry) throws Exception {
                return group.registerRenderer(entry.first(), HtmlResultRenderer.callable(entry.second()));
            }
        };
    }
}
