package com.googlecode.yatspec.rendering.wiki;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.parsing.Files;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.state.Result;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Maps.entries;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.Files.overwrite;
import static com.googlecode.yatspec.rendering.Renderers.registerRenderer;


public class WikiResultRenderer implements SpecResultListener {
    private final List<Pair<Predicate, Renderer>> customRenderers = new ArrayList<Pair<Predicate, Renderer>>();
    @Override
    public void complete(File yatspecOutputDir, Result result) throws Exception {
        overwrite(outputFile(yatspecOutputDir, result), render(result));
    }

    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        sequence(customRenderers).fold(group, registerRenderer());
        group.registerRenderer(instanceOf(JavaSource.class), callable(new JavaSourceRenderer()));
        group.registerRenderer(instanceOf(Notes.class), callable(new NotesRenderer()));
        final StringTemplate template = group.getInstanceOf("wiki");
        template.setAttribute("testResult", result);
        StringWriter writer = new StringWriter();
        template.write(new NoIndentWriter(writer));
        return writer.toString();
    }

    public <T> WikiResultRenderer withCustomRenderer(Class<T> klazz, Renderer<T> renderer){
        return withCustomRenderer((Predicate)instanceOf(klazz), renderer);
    }

    private <T> WikiResultRenderer withCustomRenderer(Predicate<T> predicate, Renderer<T> renderer) {
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

    public static File outputFile(File outputDirectory, Result result) {
        return new File(outputDirectory, Files.toPath(result.getTestClass()).replaceFirst("Test$", "") + ".wiki");
    }
}