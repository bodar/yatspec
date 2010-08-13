package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.parsing.TestParser;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Status;
import org.antlr.stringtemplate.StringTemplate;
import org.jdom.Document;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.yatspec.rendering.EnhancedStringTemplateGroup.getTemplateRelativeTo;

public class ResultRenderer implements Renderer<Result> {
    public String render(Result result) throws Exception {
        final EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup("randomName");
        group.registerDefaultRenderer(new XmlStringRenderer() );
        group.registerRenderer(Document.class, new DocumentRenderer());
        final StringTemplate template = group.getInstanceOf(getTemplateRelativeTo(this.getClass(), "TestOutput"));
        template.setAttribute("cssClass", getCssMap());
        template.setAttribute("testSuite", result.getName());
        template.setAttribute("testMethods", result.getTestMethods());
        return template.toString();
    }

    private Map<Status, String> getCssMap() {
        return new HashMap<Status, String>() {{
            put(Status.Passed, "test-passed" );
            put(Status.Failed, "test-failed" );
            put(Status.NotRun, "test-not-run" );
        }};
    }

}
