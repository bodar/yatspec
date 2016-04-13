package com.googlecode.yatspec.rendering.html;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Xml;
import com.googlecode.yatspec.Creator;
import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.parsing.Files;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.rendering.*;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.ScenarioTableHeader;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.Files.overwrite;
import static com.googlecode.yatspec.rendering.Renderers.registerRenderer;
import static java.lang.String.format;


public class HtmlResultRenderer implements SpecResultListener {
    private final List<Pair<Predicate, Renderer>> customRenderers = new ArrayList<Pair<Predicate, Renderer>>();

    private List<Content> customScripts = Collections.emptyList();
    private List<Content> customHeaderContents = Collections.emptyList();

    @Override
    public void complete(File yatspecOutputDir, Result result) throws Exception {
        overwrite(htmlResultFile(yatspecOutputDir, result.getTestClass()), render(result));
    }

    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        group.registerRenderer(always().and(not(instanceOf(Number.class))), Xml.escape());
        group.registerRenderer(instanceOf(ScenarioTableHeader.class), callable(new ScenarioTableHeaderRenderer()));
        group.registerRenderer(instanceOf(JavaSource.class), callable(new JavaSourceRenderer()));
        group.registerRenderer(instanceOf(Notes.class), callable(new NotesRenderer()));
        group.registerRenderer(instanceOf(LinkingNote.class), callable(new LinkingNoteRenderer(result.getTestClass())));
        group.registerRenderer(instanceOf(ContentAtUrl.class), asString());
        sequence(customRenderers).fold(group, registerRenderer());
        for (Class document : Creator.optionalClass("org.jdom.Document")) {
            group.registerRenderer(instanceOf(document), callable(Creator.<Renderer>create(Class.forName("com.googlecode.yatspec.plugin.jdom.DocumentRenderer"))));
        }

        final StringTemplate template = group.getInstanceOf("yatspec");
        template.setAttribute("script", loadContent("xregexp.js"));
        template.setAttribute("script", loadContent("yatspec.js"));
        for (Content customScript : customScripts) {
            template.setAttribute("script", customScript);
        }
        for (Content customHeaderContent : customHeaderContents) {
            template.setAttribute("customHeaderContent", customHeaderContent);
        }
        template.setAttribute("stylesheet", loadContent("yatspec.css"));
        template.setAttribute("cssClass", getCssMap());
        template.setAttribute("testResult", result);
        StringWriter writer = new StringWriter();
        template.write(new NoIndentWriter(writer));
        return writer.toString();
    }

    public <T> HtmlResultRenderer withCustomRenderer(Class<T> klazz, Renderer<T> renderer) {
        return withCustomRenderer((Predicate) instanceOf(klazz), renderer);
    }

    public <T> HtmlResultRenderer withCustomRenderer(Predicate<T> predicate, Renderer<T> renderer) {
        customRenderers.add(Pair.<Predicate, Renderer>pair(predicate, renderer));
        return this;
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
        return new ContentAtUrl(HtmlResultRenderer.class.getResource(resource));
    }

    public static Map<Status, String> getCssMap() {
        return new HashMap<Status, String>() {{
            put(Status.Passed, "test-passed");
            put(Status.Failed, "test-failed");
            put(Status.NotRun, "test-not-run");
        }};
    }

    public static String htmlResultRelativePath(Class resultClass) {
        return Files.toPath(resultClass) + ".html";
    }

    public static File htmlResultFile(File outputDirectory, Class resultClass) {
        return new File(outputDirectory, htmlResultRelativePath(resultClass));
    }

    public static String testMethodRelativePath(TestMethod testMethod) {
        return format("%s#%s",
                htmlResultRelativePath(testMethod.getTestClass()),
                testMethod.getName());
    }

    public HtmlResultRenderer withCustomHeaderContent(Content... content) {
        this.customHeaderContents = Arrays.asList(content);
        return this;
    }

    public HtmlResultRenderer withCustomScripts(Content... scripts) {
        this.customScripts = Arrays.asList(scripts);
        return this;
    }
}