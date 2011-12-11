package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.ContentRenderer;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;

public class HtmlIndexRenderer implements ContentRenderer<Index> {
    @Override
    public File outputFile(File outputDirectory, Index index) {
        return new File(outputDirectory, "index.html");
    }

    @Override
    public String render(Index index) throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        StringTemplate template = group.getInstanceOf("index",
                model().
                        add("script", loadContent("index.js")).
                        add("stylesheet", HtmlResultRenderer.loadContent("yatspec.css")).
                        add("cssClass", getCssMap()).
                        add("result", new IndexModel(index).asModel()).toMap());
        return template.toString();
    }

    private Content loadContent(String name) {
        return new Content(getClass().getResource(name));
    }
}
