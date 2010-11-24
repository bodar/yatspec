package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Status;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.jdom.Document;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.yatspec.rendering.Resources.getResourceRelativeTo;


public class ResultRenderer implements Renderer<Result> {

    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup("randomName");
        group.registerDefaultRenderer(new XmlStringRenderer() );
        group.registerRenderer(Document.class, new DocumentRenderer());
        group.registerRenderer(Content.class, new ToStringRenderer<Content>());
        register(group, result.getCustomRenderers());
        final StringTemplate template = group.getInstanceOf(getResourceRelativeTo(this.getClass(), "yatspec"));
        template.setAttribute("script", loadContent("yatspec.js"));
        template.setAttribute("stylesheet", loadContent("yatspec.css"));
        template.setAttribute("cssClass", getCssMap());
        template.setAttribute("testResult", result);
        StringWriter writer = new StringWriter();
        template.write(new NoIndentWriter(writer));
        return writer.toString();
    }

    private void register(EnhancedStringTemplateGroup group, Map<Class, Renderer> renderers) {
        for (Class rendererType : renderers.keySet()) {
            group.registerRenderer(rendererType, renderers.get(rendererType));
        }
    }

    private Content loadContent(final String resource) throws IOException {
        return new Content(getClass().getResource(resource));
    }

    private Map<Status, String> getCssMap() {
        return new HashMap<Status, String>() {{
            put(Status.Passed, "test-passed" );
            put(Status.Failed, "test-failed" );
            put(Status.NotRun, "test-not-run" );
        }};
    }
}