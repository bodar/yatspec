package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Maps;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.Result;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;

import java.io.StringWriter;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.*;


public class WikiResultRenderer implements Renderer<Result> {
    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.registerRenderer(instanceOf(JavaSource.class), callable(new JavaSourceRenderer()));
        group.registerRenderer(instanceOf(Notes.class), callable(new NotesRenderer()));
        Maps.entries(result.getCustomRenderers()).fold(group, HtmlResultRenderer.registerRenderer());
        final StringTemplate template = group.getInstanceOf("wiki");
        template.setAttribute("testResult", result);
        StringWriter writer = new StringWriter();
        template.write(new NoIndentWriter(writer));
        return writer.toString();
    }

    public static <T> Callable1<T, String> callable(final Renderer<T> value) {
        return new Callable1<T, String>() {
            @Override
            public String call(T o) throws Exception {
                return value.render(o);
            }
        };
    }
}