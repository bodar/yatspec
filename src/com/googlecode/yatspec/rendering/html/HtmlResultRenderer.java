package com.googlecode.yatspec.rendering.html;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Xml;
import com.googlecode.yatspec.Creator;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.parsing.Files;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.rendering.*;
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
import static com.googlecode.totallylazy.Maps.entries;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.yatspec.rendering.Renderers.registerRenderer;


public class HtmlResultRenderer implements ContentRenderer<Result> {
    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        for (Class document : Creator.optionalClass("org.jdom.Document")) {
            group.registerRenderer(instanceOf(document), callable(Creator.<Renderer>create(Class.forName("com.googlecode.yatspec.plugin.jdom.DocumentRenderer"))));
        }
        for(WithCustomHtmlRendering withCustomHtmlRendering : result.testInstance(WithCustomHtmlRendering.class)){
            entries(withCustomHtmlRendering.getCustomHtmlRenderers()).fold(group, registerRenderer());
        }
        Content customHeaderContent = result.testInstance(WithCustomHtmlHeaderContent.class).map(getCustomHeader()).getOrNull();

        group.registerRenderer(instanceOf(Content.class), asString());
        group.registerRenderer(instanceOf(Notes.class), callable(new NotesRenderer()));
        group.registerRenderer(instanceOf(JavaSource.class), callable(new JavaSourceRenderer()));
        group.registerRenderer(instanceOf(ScenarioTableHeader.class), callable(new ScenarioTableHeaderRenderer()));
        group.registerRenderer(always().and(not(instanceOf(Number.class))), Xml.escape());
        final StringTemplate template = group.getInstanceOf("yatspec");
        template.setAttribute("script", loadContent("yatspec.js"));
        template.setAttribute("customHeaderContent", customHeaderContent);
        template.setAttribute("stylesheet", loadContent("yatspec.css"));
        template.setAttribute("cssClass", getCssMap());
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

    private static Callable1<? super WithCustomHtmlHeaderContent, Content> getCustomHeader() {
        return new Callable1<WithCustomHtmlHeaderContent, Content>() {
            @Override
            public Content call(WithCustomHtmlHeaderContent withCustomHtmlHeaderContent) throws Exception {
                return withCustomHtmlHeaderContent.getCustomHeaderContent();
            }
        };
    }

}