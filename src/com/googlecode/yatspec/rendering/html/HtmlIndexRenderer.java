package com.googlecode.yatspec.rendering.html;

import com.googlecode.funclate.Model;
import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.yatspec.rendering.ContentRenderer;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.loadContent;
import static com.googlecode.yatspec.state.TestMethod.calculateStatus;

public class HtmlIndexRenderer implements ContentRenderer<Index>{
    @Override
    public File outputFile(File outputDirectory, Index index) {
        return new File(outputDirectory, "index.html");
    }

    @Override
    public String render(Index index) throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        StringTemplate template = group.getInstanceOf("index",
                model().
                        add("stylesheet", loadContent("yatspec.css")).
                        add("cssClass", getCssMap()).
                        add("results", index.entries().map(asModel()).toList()).toMap());
        return template.toString();
    }

    private Callable1<? super Pair<File, Result>, Model> asModel() {
        return new Callable1<Pair<File, Result>, Model>() {
            @Override
            public Model call(Pair<File, Result> pair) throws Exception {
                File file = pair.first();
                Result result = pair.second();
                return model().
                        add("name", result.getName()).
                        add("url", "file://" + file).
                        add("status", getStatus(result));
            }
        };
    }

    private Status getStatus(Result result) throws Exception {
        return calculateStatus(sequence(result.getTestMethods()).map(new Callable1<TestMethod, Status>() {
            @Override
            public Status call(TestMethod testMethod) throws Exception {
                return testMethod.getStatus();
            }
        }));
    }
}
