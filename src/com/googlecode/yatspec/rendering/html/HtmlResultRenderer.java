package com.googlecode.yatspec.rendering.html;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Xml;
import com.googlecode.yatspec.Creator;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.Files;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.ContentRenderer;
import com.googlecode.yatspec.rendering.ScenarioTableHeaderRenderer;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.ScenarioTableHeader;
import com.googlecode.yatspec.state.Status;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.*;


public class HtmlResultRenderer implements ContentRenderer<Result> {
    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        for (Class document : Creator.optionalClass("org.jdom.Document")) {
            group.registerRenderer(instanceOf(document), callable(Creator.<Renderer>create(Class.forName("com.googlecode.yatspec.plugin.jdom.DocumentRenderer"))));
        }
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

    public static Callable2<EnhancedStringTemplateGroup, Map.Entry<Class, Renderer>, EnhancedStringTemplateGroup> registerRenderer() {
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

    public static Content loadContent(final String resource) throws IOException {
        return new Content(HtmlResultRenderer.class.getResource(resource));
    }

    public static Map<Status, String> getCssMap() {
        return new HashMap<Status, String>() {{
            put(Status.Passed, "test-passed");
            put(Status.Failed, "test-failed");
            put(Status.NotRun, "test-not-run");
        }};
    }

    @Override
    public File outputFile(File outputDirectory, Result result) {
        return new File(outputDirectory, Files.toPath(result.getTestClass()) + ".html");
    }
}