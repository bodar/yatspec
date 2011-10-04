package com.googlecode.yatspec.rendering.html;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Xml;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.DocumentRenderer;
import com.googlecode.yatspec.rendering.JavaSourceRenderer;
import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.ScenarioTableHeaderRenderer;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.ScenarioTableHeader;
import com.googlecode.yatspec.state.Status;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.jdom.Document;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.not;


public class HtmlResultRenderer implements Renderer<Result> {
    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.registerRenderer(instanceOf(Document.class), callable(new DocumentRenderer()));
        group.registerRenderer(instanceOf(Content.class), asString());
        group.registerRenderer(instanceOf(Notes.class), callable(new NotesRenderer()));
        group.registerRenderer(instanceOf(JavaSource.class), callable(new JavaSourceRenderer()));
        group.registerRenderer(instanceOf(ScenarioTableHeader.class), callable(new ScenarioTableHeaderRenderer()));
        Maps.entries(result.getCustomRenderers()).fold(group, registerRenderer());
        group.registerRenderer(always().and(not(instanceOf(Number.class))), Xml.escape());
        final StringTemplate template = group.getInstanceOf("yatspec");
        template.setAttribute("script", loadContent("yatspec.js"));
        template.setAttribute("customHeaderContent", result.getCustomHeaderContent());
        template.setAttribute("stylesheet", loadContent("yatspec.css"));
        template.setAttribute("cssClass", getCssMap());
        template.setAttribute("testResult", result);
        StringWriter writer = new StringWriter();
        template.write(new NoIndentWriter(writer));
        return writer.toString();
    }

    private Callable2<EnhancedStringTemplateGroup, Map.Entry<Class, Renderer>, EnhancedStringTemplateGroup> registerRenderer() {
        return new Callable2<EnhancedStringTemplateGroup, Map.Entry<Class, Renderer>, EnhancedStringTemplateGroup>() {
            @Override
            public EnhancedStringTemplateGroup call(EnhancedStringTemplateGroup group, Map.Entry<Class, Renderer> entry) throws Exception {
                return group.registerRenderer(Predicates.instanceOf(entry.getKey()), callable(entry.getValue()));
            }
        };
    }

    public static <T> Callable1<T, String> callable(final Renderer<T> value) {
        return new Callable1<T, String>() {
            @Override
            public String call(T o) throws Exception {
                return value.render(o);
            }
        };
    }

    private Content loadContent(final String resource) throws IOException {
        return new Content(getClass().getResource(resource));
    }

    private static Map<Status, String> getCssMap() {
        return new HashMap<Status, String>() {{
            put(Status.Passed, "test-passed");
            put(Status.Failed, "test-failed");
            put(Status.NotRun, "test-not-run");
        }};
    }

}