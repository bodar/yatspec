package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.ContentAtUrl;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.Result;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.yatspec.parsing.Files.overwrite;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;

public class HtmlIndexRenderer implements SpecResultListener {
    private final static Index index = new Index();

    @Override
    public void complete(File yatspecOutputDir, Result result) throws Exception {
        index.add(result);
        overwrite(outputFile(yatspecOutputDir), render(yatspecOutputDir, index));
    }

    private String render(File yatspecOutputDir, Index index) throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        StringTemplate template = group.getInstanceOf("index",
                model().
                        add("script", loadContent("index.js")).
                        add("stylesheet", HtmlResultRenderer.loadContent("yatspec.css")).
                        add("cssClass", getCssMap()).
                        add("result", new IndexModel(index, yatspecOutputDir).asModel()).toMap());
        return template.toString();
    }

    public static File outputFile(File outputDirectory) {
        return new File(outputDirectory, "index.html");
    }

    private Content loadContent(String name) {
        return new ContentAtUrl(getClass().getResource(name));
    }
}
